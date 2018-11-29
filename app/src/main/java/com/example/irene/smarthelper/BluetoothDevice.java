package com.example.irene.smarthelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class BluetoothDevice extends AppCompatActivity {
    private TextView medicion;
    private TextView conexion;
//    private Button boton;
    private String name;
    private String address;
    private android.bluetooth.BluetoothDevice device;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private Handler handler;
    private String heartValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device);

        medicion = findViewById(R.id.medicion);
//        boton = findViewById(R.id.boton);
        conexion = findViewById(R.id.conexion);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handler = new Handler();

        getBondedDevices();
//        initializeButton();

        Thread t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try{
                        Thread.sleep(20000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startScanHeartRate();
                                updateHeartValue();
                            }
                        });
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();
    }

    public void updateHeartValue(){
        if(heartValue != null){
            Log.i("KHAMALEONA", heartValue);
            medicion.setText(heartValue);
            sendDataToServer();
        }else{
            Log.i("KHAMALEONA", "HeartValue es nulo.");
        }
    }

    public void sendDataToServer(){
        Gson gson = new Gson();
        Medicion medicion = new Medicion(heartValue, "76048517Y");
        String code = gson.toJson(medicion);

        HttpURLConnection client = null;
        try {
            URL url = new URL("http://exampleurl.com");
            client = (HttpURLConnection) url.openConnection();

            client.setRequestMethod("POST");
            client.setRequestProperty("MEDICION",code);

            client.setDoOutput(true);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(client != null){
                client.disconnect();
            }
        }
    }

    public void getBondedDevices(){
        name = getIntent().getStringExtra("NAME");
        address = getIntent().getStringExtra("ADDRESS");
        conexion.setText(name + ": " + address);

        device = bluetoothAdapter.getRemoteDevice(address);
        bluetoothGatt = device.connectGatt(this, true, bluetoothGattCallback);
    }

//    public void initializeButton(){
//        boton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startScanHeartRate();
//            }
//        });
//    }

    void stateConnected() {
        bluetoothGatt.discoverServices();
    }

    void stateDisconnected() {
        bluetoothGatt.disconnect();
    }


    public void startScanHeartRate(){
        BluetoothGattCharacteristic bchar = bluetoothGatt.getService(CustomBluetoothProfile.HeartRate.service)
                .getCharacteristic(CustomBluetoothProfile.HeartRate.controlCharacteristic);
        bchar.setValue(new byte[]{21, 2, 1});
        bluetoothGatt.writeCharacteristic(bchar);
    }

    public void listenHeartRate(){
        BluetoothGattCharacteristic bchar = bluetoothGatt.getService(CustomBluetoothProfile.HeartRate.service)
                .getCharacteristic(CustomBluetoothProfile.HeartRate.measurementCharacteristic);
        bluetoothGatt.setCharacteristicNotification(bchar, true);
        BluetoothGattDescriptor descriptor = bchar.getDescriptor(CustomBluetoothProfile.HeartRate.descriptor);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);
    }

    final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.i("test", "onConnectionStateChange");

            if(newState == BluetoothProfile.STATE_CONNECTED){
                stateConnected();
            }else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                stateDisconnected();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.i("test", "onServicesDiscovered");
            listenHeartRate();
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.i("test", "onCharacteristicRead");
            byte[] data = characteristic.getValue();
            medicion.setText(Arrays.toString(data));
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.i("test", "onCharacteristicWrite");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.i("test", "onCharacteristicChanged");
            byte[] data = characteristic.getValue();
            Log.i("KHAMALEONA", Byte.toString(data[1]));
            heartValue = Byte.toString(data[1]);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.i("test", "onDescriptorRead");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.i("test", "onDescriptorWrite");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Log.i("test", "onReliableWriteCompleted");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            Log.i("test", "onReadRemoteRssi");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.i("test", "onMtuChanged");
        }
    };

}
