package com.example.irene.smarthelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class BluetoothDevice extends AppCompatActivity {
    private TextView medicion;
    private TextView conexion;
    private String name;
    private String address;
    private android.bluetooth.BluetoothDevice device;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private String heartValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device);

        medicion = findViewById(R.id.medicion);
        conexion = findViewById(R.id.conexion);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        getBondedDevices();
        setRepeatingAsyncTask();
    }

    public void setRepeatingAsyncTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            HTTPAsyncTask medTask = new HTTPAsyncTask();
                            medTask.execute();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(heartValue != null && !heartValue.equals("0")){
                            medicion.setText(heartValue);
                        }
                    }
                });
            }
        };

        timer.schedule(task, 0, 60 * 500);
    }

    public String sendDataToServer() {

        String response = "";

        if (heartValue != null & !heartValue.equals("0")) {
            Gson gson = new Gson();
            Medicion medicion = new Medicion(heartValue, "12345678B");
            String code = gson.toJson(medicion);
            Log.i("KHAMALEONA", code);

            HttpURLConnection client = null;
            try {
                URL url = new URL("http://192.168.1.103:8080/v1/mediciones");
                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("POST");
                client.setRequestProperty("Content-Type", "application/json; charset=utf-8");

                OutputStream os = client.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(code);
                writer.flush();
                writer.close();
                os.close();

                client.connect();
                Log.i("KHAMALEONA", client.getResponseMessage());

                response = client.getResponseMessage();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (client != null) {
                    client.disconnect();
                }
            }
//            response = "OK.";
        } else {
            Log.i("KHAMALEONA", "HeartValue es nulo.");
        }

        return response;
    }

    public void getBondedDevices() {
        name = getIntent().getStringExtra("NAME");
        address = getIntent().getStringExtra("ADDRESS");
        conexion.setText(name + ": " + address);

        device = bluetoothAdapter.getRemoteDevice(address);
        bluetoothGatt = device.connectGatt(this, true, bluetoothGattCallback);
    }

    void stateConnected() {
        bluetoothGatt.discoverServices();
    }

    void stateDisconnected() {
        bluetoothGatt.disconnect();
    }


    public void startScanHeartRate() {
        BluetoothGattCharacteristic bchar = bluetoothGatt.getService(CustomBluetoothProfile.HeartRate.service)
                .getCharacteristic(CustomBluetoothProfile.HeartRate.controlCharacteristic);
        bchar.setValue(new byte[]{21, 2, 1});
        bluetoothGatt.writeCharacteristic(bchar);
    }

    public void listenHeartRate() {
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

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                stateConnected();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
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

    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            startScanHeartRate();
            return sendDataToServer();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("KHAMALEONA", result);
        }
    }

}
