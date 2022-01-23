package com.knaufinator.mini6dofcontroller;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.welie.blessed.BluetoothPeripheral;
import com.welie.blessed.ConnectionState;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private final Context context;
    private final List<DeviceWrapper> bleDeviceList = new ArrayList<>();

    public DeviceAdapter(Context context) {
        this.context = context;
    }

    public void addDevice(DeviceWrapper bleDevice) {

        //only add if not in it

        boolean contains = false;


        for (DeviceWrapper object : bleDeviceList) {
           if(object.Mac.compareTo(bleDevice.Mac) == 0)
               contains = true;
        }

        if(!contains )
        {
            bleDeviceList.add(bleDevice);
            notifyDataSetChanged();

        }

    }

    public void removeDevice(DeviceWrapper bleDevice) {
        for (int i = 0; i < bleDeviceList.size(); i++) {
            DeviceWrapper device = bleDeviceList.get(i);
            if (bleDevice.Mac.equals(device.Mac)) {
                bleDeviceList.remove(i);
            }
        }
    }

    public void clearConnectedDevice() {
        for (int i = 0; i < bleDeviceList.size(); i++) {
            DeviceWrapper device = bleDeviceList.get(i);
            if (device.ConnectionState == ConnectionState.CONNECTED.value) {
                bleDeviceList.remove(i);
            }
        }
    }

    public void clearScanDevice() {
        for (int i = 0; i < bleDeviceList.size(); i++) {
            DeviceWrapper device = bleDeviceList.get(i);
            if (device.ConnectionState == ConnectionState.CONNECTED.value) {
                bleDeviceList.remove(i);
            }
        }
    }

    public void clear() {
        clearConnectedDevice();
        clearScanDevice();
    }

    @Override
    public int getCount() {
        return bleDeviceList.size();
    }

    @Override
    public DeviceWrapper getItem(int position) {
        if (position > bleDeviceList.size())
            return null;
        return bleDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.adapter_device, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.img_blue = (ImageView) convertView.findViewById(R.id.img_blue);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_mac = (TextView) convertView.findViewById(R.id.txt_mac);
            holder.txt_rssi = (TextView) convertView.findViewById(R.id.txt_rssi);
            holder.layout_idle = (LinearLayout) convertView.findViewById(R.id.layout_idle);
            holder.layout_connected = (LinearLayout) convertView.findViewById(R.id.layout_connected);
            holder.btn_disconnect = (Button) convertView.findViewById(R.id.btn_disconnect);
            holder.btn_connect = (Button) convertView.findViewById(R.id.btn_connect);
            holder.btn_detail = (Button) convertView.findViewById(R.id.btn_detail);
        }



        final DeviceWrapper bleDevice = getItem(position);
        if (bleDevice != null) {
            boolean isConnected = bleDevice.ConnectionState == ConnectionState.CONNECTED.value;
            String name = bleDevice.Name;
            String mac = bleDevice.Mac;

            holder.txt_name.setText(name);
            holder.txt_mac.setText(mac);

            if (isConnected) {
                holder.img_blue.setImageResource(R.mipmap.ic_blue_connected);
                holder.txt_name.setTextColor(0xFF1DE9B6);
                holder.txt_mac.setTextColor(0xFF1DE9B6);
                holder.layout_idle.setVisibility(View.GONE);
                holder.layout_connected.setVisibility(View.VISIBLE);
            } else {
                holder.img_blue.setImageResource(R.mipmap.ic_blue_remote);
                holder.txt_name.setTextColor(0xFF000000);
                holder.txt_mac.setTextColor(0xFF000000);
                holder.layout_idle.setVisibility(View.VISIBLE);
                holder.layout_connected.setVisibility(View.GONE);
            }
        }

        holder.btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onConnect(bleDevice);
                }
            }
        });

        holder.btn_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onDisConnect(bleDevice);
                }
            }
        });

        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onDetail(bleDevice);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView img_blue;
        TextView txt_name;
        TextView txt_mac;
        TextView txt_rssi;
        LinearLayout layout_idle;
        LinearLayout layout_connected;
        Button btn_disconnect;
        Button btn_connect;
        Button btn_detail;
    }

    public interface OnDeviceClickListener {
        void onConnect(DeviceWrapper bleDevice);

        void onDisConnect(DeviceWrapper bleDevice);

        void onDetail(DeviceWrapper bleDevice);
    }

    private OnDeviceClickListener mListener;

    public void setOnDeviceClickListener(OnDeviceClickListener listener) {
        this.mListener = listener;
    }

}
