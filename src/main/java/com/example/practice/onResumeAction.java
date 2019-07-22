package com.example.practice;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Set;

public class onResumeAction {

    //アクションコード
    private static final int REQUEST_ENABLE_BT = 1;

    public void BluetoothCheck(Activity mActivity, BluetoothAdapter mBluetoothAdapter, ArrayAdapter<String> mArrayAdapter, TextView textView){

        //使っている端末がBluetoothに対応しているか確認
        if (mBluetoothAdapter != null) {

            //対応している
            //→Bluetoothが有効になっているか確認
            if (!mBluetoothAdapter.isEnabled()) {

                //(a)有効になっていない
                //有効にするためのポップ表示
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);


            } else {

                //(b)有効になっている
                //接続したことのある端末の情報を取得
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice mBluetoothDevice : pairedDevices) {
                        mArrayAdapter.add(mBluetoothDevice.getName() + "\n" + mBluetoothDevice.getAddress());
                    }
                }

                textView.setText(R.string.BluetoothEnable);

            }

        } else {

            //対応していない
            textView.setText(R.string.BluetoothNO);

        }
    }
}
