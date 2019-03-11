package com.ricardogo5.pdm_16_ricardogutierrez;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothDevicesAdapter extends ArrayAdapter {
    ArrayList<BluetoothDevice> devices;
    public BluetoothDevicesAdapter(Context context, ArrayList<BluetoothDevice> data) {
        super(context,R.layout.spiner_paired_devices,data);
        devices=data;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.spiner_paired_devices, null);

        TextView txtDeviceName=item.findViewById(R.id.txtDeviceName);
        txtDeviceName.setText(devices.get(position).getName());

        TextView txtDeviceMac=item.findViewById(R.id.txtDeviceMac);
        txtDeviceMac.setText(devices.get(position).getAddress());
        return (item);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.spiner_paired_devices, null);

        TextView txtDeviceName=item.findViewById(R.id.txtDeviceName);
        txtDeviceName.setText(devices.get(position).getName());

        TextView txtDeviceMac=item.findViewById(R.id.txtDeviceMac);
        txtDeviceMac.setText(devices.get(position).getAddress());
        return (item);
    }

    public void updateDevices(ArrayList<BluetoothDevice> newArray){
        devices.clear();
        devices.addAll(newArray);
        this.notifyDataSetChanged();
    }
}
