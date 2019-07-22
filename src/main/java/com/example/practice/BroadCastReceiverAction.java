package com.example.practice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BroadCastReceiverAction {

    public void BluetoothSearch(Intent intent, ArrayAdapter<String> mArrayAdapter,TextView textView) {

        String actionCode = intent.getAction();

        if (actionCode != null) {

            //Bluetooth検索がはじまった
            if (actionCode.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
            }

            //新しいBluetoothが見つかった
            else if (actionCode.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice addDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mArrayAdapter.add(addDevice.getName() + "\n" + addDevice.getAddress());
                textView.setText(R.string.BluetoothFound);
            }

            //Bluetooth検索が終わった
            else if (actionCode.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                textView.setText(R.string.BluetoothFinish);
            } else {
            }
        }
    }
}
