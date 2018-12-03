package com.example.irene.smarthelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity principal de nuestra aplicación.
 * Es una pantalla bastante sencilla en la que se muestra un campo de texto donde el usuario deberá introducir la dirección bluetooth de la MiBand 2 a utilizar.
 */
public class MainActivity extends AppCompatActivity {
    private EditText btlAddress;
    private Button tryConnecion;
    private Button connectButton;
    private TextView connectionState;
    private BluetoothDevice device;
    private BluetoothAdapter bluetoothAdapter;
    private boolean exito;

    /**
     * Método onCreate de la aplicación. En él se inicializan los diferentes componentes del layout y se lanzan los procesos principales.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btlAddress = findViewById(R.id.btlAddress);
        tryConnecion = findViewById(R.id.tryConnection);
        connectButton = findViewById(R.id.connect);
        connectionState = findViewById(R.id.connectionState);
        exito = false;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        onClicTryConnection();
        onClicStartConnection();
    }

    /**
     * Método que comprueba si es posible realizar una conexión con la dirección bluetooth introducida.
     * Por defecto, el botón "Connect" está deshabilitado. Sólo se habilitará si la conexión puede realizarse.
     */
    public void onClicTryConnection(){
        tryConnecion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = btlAddress.getText().toString();
                device = bluetoothAdapter.getRemoteDevice(address);

                if(device == null){
                    connectionState.setVisibility(View.VISIBLE);
                    connectionState.setText("Conexión Fallida");
                }else{
                    connectButton.setEnabled(true);
                    connectionState.setVisibility(View.VISIBLE);
                    connectionState.setText("Conexión Exitosa");
                    exito = true;
                }
            }
        });
    }

    /**
     * Método que inicia la conexión con la pulsera. Lo que hacemos será mandar en un objeto de tipo Intent, la dirección bluetooth y el nombre del dispositivo.
     * Será en la siguiente activity (BluetoothDevice) donde se establecerá la conexión en sí.
     */
    public void onClicStartConnection(){
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exito){
                    Intent intent = new Intent(MainActivity.this, com.example.irene.smarthelper.BluetoothDevice.class);
                    intent.putExtra("ADDRESS", device.getAddress());
                    intent.putExtra("NAME", device.getName());
                    startActivity(intent);
                }
            }
        });
    }

}