package com.example.practice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import java.lang.String;

public class MainActivity extends AppCompatActivity {

    public TextView textView;
    public Spinner spinnerView;
    public EditText editText;
    public ArrayAdapter<String> mArrayAdapter;

    public BluetoothAdapter mBluetoothAdapter;
    public String address;
    public byte[] Bytes = new byte[1024];

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //レイアウトの配置
        setContentView(R.layout.activity_main);

        //ビューの対応付け
        textView= findViewById(R.id.text1);
        editText=findViewById(R.id.edit);
        spinnerView=findViewById(R.id.spinner);

        //スピナーとアダプターの対応付け
        mArrayAdapter = new ArrayAdapter<>(this, R.layout.list);
        spinnerView.setAdapter(mArrayAdapter);

        //初期化
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //ボタン１の定義
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {

            //端末を検索する機能
            public void onClick(View v) {

                //既に検索中なら検索中止
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                    textView.setText(R.string.BluetoothFinish);
                }

                //検索開始
                mBluetoothAdapter.startDiscovery();
                IntentFilter intent = new IntentFilter();
                intent.addAction(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mBroadcastReceiver, intent);
                textView.setText(R.string.BluetoothSearch);
            }
        });

        //ボタン２の定義
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            //端末と接続する機能
            public void onClick(View v) {

                //検索中なら検索中止
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                    textView.setText(R.string.BluetoothFinish);
                }

                //接続開始
                BluetoothConnectThread mBluetoothConnectThread = new BluetoothConnectThread(mBluetoothAdapter.getRemoteDevice(address),textView);
                mBluetoothConnectThread.start();
            }
        });


        //ボタン3の定義
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {

            //端末に文字列を送信する機能
            public void onClick(View v) {

                //検索中なら検索中止
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                    textView.setText(R.string.BluetoothFinish);
                }

                //editTextから文字列を取得
                String text = editText.getText().toString();
                Bytes = text.getBytes();

                //文字列を送信
                BluetoothConnectThread mBluetoothConnectThread = new BluetoothConnectThread(mBluetoothAdapter.getRemoteDevice(address),textView);
                mBluetoothConnectThread.write(Bytes);
            }
        });

        //スピナーの定義
        spinnerView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //通信する端末を選択する機能
            public void onItemSelected(AdapterView parent, View view, int position, long id) {

                //選択した端末情報を取得
                String str = parent.getSelectedItem().toString();
                address = str.substring(str.length() - 17);
            }

            //何も選択されていない時
            public void onNothingSelected(AdapterView parent) {
                address=null;
            }
        });
    }

    public void onResume(){

        super.onResume();

        //使っている端末がBluetoothに対応しているか確認
        onResumeAction monResumeAction=new onResumeAction();
        monResumeAction.BluetoothCheck(this, mBluetoothAdapter, mArrayAdapter,textView);

    }

    protected void onDestroy(){

        super.onDestroy();

        //検索と接続を終了
        mBluetoothAdapter.cancelDiscovery();
        BluetoothConnectThread mBluetoothConnectThread = new BluetoothConnectThread(mBluetoothAdapter.getRemoteDevice(address),textView);
        mBluetoothConnectThread.cancel();
    }

    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        //インスタンスの保存
        Editable editable = editText.getText();
        outState.putString("KEY", editable.toString() );
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        //インスタンスの復帰
        String str = savedInstanceState.getString("KEY");
        editText.setText(str);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        //どのActivityから戻ってきたかを判別する
        onActivityResultAction monActivityResultAction=new onActivityResultAction();
        monActivityResultAction.ActionJudge(requestCode, resultCode, intent, mBluetoothAdapter, mArrayAdapter,textView);

    }

    public  BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {

            //検索結果の判別
            BroadCastReceiverAction mBroadCastReceiverAction=new BroadCastReceiverAction();
            mBroadCastReceiverAction.BluetoothSearch(intent, mArrayAdapter,textView);
        }
    };
}