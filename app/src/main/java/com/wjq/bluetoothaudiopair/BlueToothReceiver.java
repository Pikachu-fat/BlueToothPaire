package com.wjq.bluetoothaudiopair;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BlueToothReceiver extends BroadcastReceiver {

    private static final String TAG = "BlueToothReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive:");
        if (intent != null && intent.getAction()!= null){
            String action = intent.getAction();
            String name = intent.getStringExtra(BluetoothAdapter.EXTRA_LOCAL_NAME);
            int curState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,-1);
            int preState = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_STATE,-1);
            Log.d(TAG, "onReceive:name:"+name+";   curState:"+curState+";   preState:"+preState);
            switch (action){
                case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    break;
                case BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED:
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    break;
                case BluetoothDevice.ACTION_UUID:
                    break;
                    default:
                        break;
            }

        }
    }
}
