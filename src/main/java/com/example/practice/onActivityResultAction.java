package com.example.practice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class onActivityResultAction {

    //アクションコード
    private static final int REQUEST_ENABLE_BT = 1;

    public void ActionJudge(int requestCode, int resultCode, Intent intent, BluetoothAdapter mBluetoothAdapter, ArrayAdapter<String> mArrayAdapter, TextView textView){

        //どのActivityから戻ってきたかを判別する
        switch (requestCode) {

            case REQUEST_ENABLE_BT:

                //実行結果を判別する
                if (resultCode == RESULT_OK) {

                    //(a)有効化できた
                    //接続したことのある端末の情報を取得
                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice mBluetoothDevice : pairedDevices) {
                            mArrayAdapter.add(mBluetoothDevice.getName() + "\n" + mBluetoothDevice.getAddress());
                        }
                    }
                    textView.setText(R.string.BluetoothEnable);

                } else {

                    //(b)有効化されなかった
                    textView.setText(R.string.BluetoothUnenable);

                }

                break;

            default:
                break;
        }

    }
}
