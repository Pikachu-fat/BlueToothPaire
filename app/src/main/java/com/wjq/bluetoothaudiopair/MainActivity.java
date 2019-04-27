package com.wjq.bluetoothaudiopair;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    BluetoothAdapter mBluetoothAdapter = null;
    private BlueToothReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        testAPI();
        initReceiver();
    }

    private void initView() {
        Button first = findViewById(R.id.open);
        Button openDialogBtn = findViewById(R.id.open_dialog);
        Button second = findViewById(R.id.close);
        Button third = findViewById(R.id.discovery);
        Button four = findViewById(R.id.four);

        first.setOnClickListener(this);
        openDialogBtn.setOnClickListener(this);
        second.setOnClickListener(this);
        third.setOnClickListener(this);
        four.setOnClickListener(this);
    }

    private void initReceiver(){
        receiver = new BlueToothReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        registerReceiver(receiver,intentFilter);
    }

    private void testAPI() {
        //作用 : 获取是否可用
        //返回值 : 返回当前的State状态值, STATE_ON, STATE_OFF, STATE_TURNING_ON, STATE_TURNING_OFF;
        //权限 : BLUETOOTH;
        int state = mBluetoothAdapter.getState();
        Log.d(TAG, "state: " + state);

        //作用 : 打开本地蓝牙适配器;
        //返回值 : 如果打开成功则返回true, 如果打开失败返回false;
        //权限 :　BLUETOOTH_ADMIN权限;
        boolean open = mBluetoothAdapter.enable();

        //关闭蓝牙
        boolean close = mBluetoothAdapter.disable();


        //作用 : 开始查找远程蓝牙设备, 先进行12秒的查询扫描(被动可见), 之后进行页面扫描(主动搜索);
        // 搜索过程中不能尝试对远程设备的连接, 同时已连接的设备的带宽也会被压缩, 等待时间变长
        // ; 使用cancelDiscovery()可以终止搜索;，需要注册广播FOUND 在intent里面可以获取到扫面发现的设备信息（name，addres等）
        //返回值 : 如果成功则返回true, 失败返回false;
        //权限 :   BLUETOOTH_ADMIN权限;
        boolean discovery = mBluetoothAdapter.startDiscovery();

        //是否正在扫面中
        boolean isDiscovering = mBluetoothAdapter.isDiscovering();

        //取消扫描
        boolean cancelDiscovery = mBluetoothAdapter.cancelDiscovery();

        //获取扫面方式
        //作用 : 获取当前蓝牙的扫描模式;
        //返回值 : SCAN_MODE_NONE, SCAN_MODE_CONNECTABLE, SCAN_MODE_DISCOVERABLE;
        mBluetoothAdapter.getScanMode();


        //作用 : 检查蓝牙地址是否合法, 蓝牙地址字母必须大写, 例如 : "00:43:A8:23:10:F0";
        //参数 : 17位的字符串, 例如 : "00:43:A8:23:10:F0";
        //返回值 : 如果蓝牙地址合法返回true, 反之返回false;
        mBluetoothAdapter.checkBluetoothAddress("00:43:A8:23:10:F0");

        //获取本地蓝牙地址
        //作用 : 返回本地蓝牙的MAC地址;
        //返回值 : 本地的硬件地址;
        String addres = mBluetoothAdapter.getAddress();

        //获取本地蓝牙名称
        mBluetoothAdapter.getName();

        //作用 : 获取已经配对的蓝牙设备的集合, 如果蓝牙未被打开, 则返回null;
        Set<BluetoothDevice> blueToothLists = mBluetoothAdapter.getBondedDevices();

        //作用 : 根据蓝牙的物理地址获取远程的蓝牙设备, 如果地址不合法, 就会产生异常;
        //返回值 : 获取到的BluetoothDevice对象;
        mBluetoothAdapter.getRemoteDevice("00:43:A8:23:10:F0");
    }

    @Override
    public void onClick(View v) {
        //蓝牙是否可用
        boolean isEnable = mBluetoothAdapter.isEnabled();
        Log.d(TAG, "isEnable: " + isEnable);
        if (isEnable) {
            switch (v.getId()) {
                case R.id.open:
                    //处于关闭状态 或者正在关闭状态
                    if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF || mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_OFF) {
                        boolean enable = mBluetoothAdapter.enable();
                        if (enable) {
                            Toast.makeText(MainActivity.this, "开启成功", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d(TAG, "onClick: 已经处于开启状态");
                        Toast.makeText(MainActivity.this, "已经处于开启状态", Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.open_dialog:
                    Intent intent = new Intent();
                    intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, 1);
                    break;
                case R.id.close:
                    if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON || mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON) {
                        boolean disenable = mBluetoothAdapter.disable();
                        if (disenable) {
                            Log.d(TAG, "onClick: 关闭成功");
                            Toast.makeText(MainActivity.this, "蓝牙关闭成功", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "蓝牙已关闭", Toast.LENGTH_LONG).show();
                    }
                    break;
                //与蓝牙设备相关的方法
                case R.id.discovery:
                    //调用此接口后系统通过广播的形式将扫描的结果返回
                    mBluetoothAdapter.startDiscovery();

                    break;
                case R.id.four:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: 蓝牙开始OK");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
