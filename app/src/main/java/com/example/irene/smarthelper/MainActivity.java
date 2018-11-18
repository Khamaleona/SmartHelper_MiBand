package com.example.irene.smarthelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<BluetoothDevice> devices;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler hander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        hander = new Handler();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        for (BluetoothDevice b: mBluetoothAdapter.getBondedDevices()) {
            devices.add(b);
        }

        hander.postDelayed((new Runnable() {
            @Override
            public void run() {
                refreshList(MainActivity.this);
            }
        }), 1000);

    }

    public void refreshList(Context context){
        ArrayList<String> list = new ArrayList<>();

        if(mBluetoothAdapter.getBondedDevices().isEmpty()){
            Toast.makeText(context, "No avalaible bluetooth devices", Toast.LENGTH_LONG);
        }else{
            for (BluetoothDevice b : mBluetoothAdapter.getBondedDevices()) {
                Log.i("DEVICES",b.getName() + " - " + b.getAddress());
                list.add(b.getName() + "\n" + b.getAddress());
            }

            listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list));
        }
    }

    public void manageItems(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final BluetoothDevice device = devices.get(position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}