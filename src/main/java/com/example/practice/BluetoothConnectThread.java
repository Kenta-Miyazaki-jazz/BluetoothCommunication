package com.example.practice;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnectThread extends Thread {

    private BluetoothSocket mBluetoothSocket;
    private static BluetoothSocket mSocket=null;
    private static InputStream mInputStream;
    private static OutputStream mOutputStream;
    private static InputStream mInStream=null;
    private static OutputStream mOutStream=null;
    private String defaultStr;
    private String displayStr;
    private String str;
    private TextView text;


    public BluetoothConnectThread(BluetoothDevice mBluetoothDevice,TextView textView) {

        str="";
        defaultStr="";
        displayStr="";
        text=textView;

        //選択した端末からSocketを取得する
        try{
            mSocket=mBluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch(IOException m) {
            Log.d("MyApp", "例外処理1");
        } finally {
            mBluetoothSocket=mSocket;
        }
    }

    public void run() {
        byte[] ReceiveBytes = new byte[1024];
        int TheNumberOfBytes;

        try {
            mBluetoothSocket.connect();
            handler2.post(runnable2);
            mInStream = mBluetoothSocket.getInputStream();
            mOutStream = mBluetoothSocket.getOutputStream();
            mInputStream = mInStream;
            mOutputStream = mOutStream;

            while (true) {
                try {
                    if(mInputStream.read(ReceiveBytes)!=-1){
                        str = new String(ReceiveBytes);
                        displayStr = defaultStr.concat(str);
                        handler1.post(runnable1);
                        defaultStr = displayStr;

                    }else {
                        Log.d("MyApp", "例外処理4.5");
                    }

                } catch (IOException m) {
                    Log.d("MyApp", "例外処理5");
                    break;
                }
            }

        } catch (IOException m) {

            try {
                mBluetoothSocket.close();
                handler3.post(runnable3);

            } catch (IOException p) {
                Log.d("MyApp", "例外処理2");
            }
        }

    }

    public void cancel() {
        try {
            mBluetoothSocket.close();
        } catch (IOException e) {
            Log.d("MyApp", "例外処理3");
        }
    }

    public void write(byte[] WriteBytes){
        try {
            mOutputStream.write(WriteBytes);
        } catch (IOException e) { }
    }

    private Handler handler1 = new Handler();
    private Runnable runnable1 = new Runnable() {

        @Override
        public void run() {

            text.setText(displayStr);
        }
    };

    private Handler handler2 = new Handler();
    private Runnable runnable2 = new Runnable() {

        @Override
        public void run() {

            text.setText(R.string.Connected);
        }
    };

    private Handler handler3 = new Handler();
    private Runnable runnable3 = new Runnable() {

        @Override
        public void run() {

            text.setText(R.string.notConnect);
        }
    };


}
