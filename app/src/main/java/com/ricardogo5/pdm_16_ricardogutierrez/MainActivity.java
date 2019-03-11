package com.ricardogo5.pdm_16_ricardogutierrez;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    //Referencia a BluetoothAdapter
    private android.bluetooth.BluetoothAdapter bluetoothAdapter;
    private Button btnBluetoothSet;
    private BluetoothBroadcastReceiver bbReceiver;
    private static final int    REQUEST_ENABLE_BT   = 1;
    private Spinner spinPariedDevices;
    private ArrayList<BluetoothDevice> pariedDevices;
    private BluetoothDevicesAdapter devicesAdapter;
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Enlazamos el boton con la vista
        btnBluetoothSet=findViewById(R.id.btnBluetoothSet);

        //Enlazamos el spiner con la vista
        spinPariedDevices=findViewById(R.id.spinPairedDevices);
        pariedDevices= new ArrayList<>();

        //Creamos el Adaptador del spinner
        devicesAdapter=new BluetoothDevicesAdapter(this,pariedDevices);

        //Asignamos el adaptador al spinner
        spinPariedDevices.setAdapter(devicesAdapter);

        configurarAdaptadorBluetooth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bluetoothAdapter.isEnabled()) {
            //Carga los dispositivos vinculados
            pariedDevices.addAll(bluetoothAdapter.getBondedDevices());
            devicesAdapter.notifyDataSetChanged();
        }
    }

    public void connectBluetoothDevice(View view){
        Log.d("Bluetooth","aqui se apaga?");
        try {
            //Crea la conexion bluetooth
            btSocket=((BluetoothDevice)spinPariedDevices.getSelectedItem()).createRfcommSocketToServiceRecord(BTMODULEUUID);
        }catch (IOException e){
            Toast.makeText(this,R.string.txtSocketErrorCreate,Toast.LENGTH_SHORT);
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                Toast.makeText(this,R.string.txtSocketErrorClose,Toast.LENGTH_SHORT);
                btSocket.close();
            } catch (IOException e2)
            {
                Toast.makeText(this,R.string.txtSocketErrorClose,Toast.LENGTH_SHORT);
            }
        }
        Log.d("Bluetooth","aqui se apaga?");
        mConnectedThread = new ConnectedThread(btSocket);
        //añadir bandera para no crear threads
        mConnectedThread.start();
        mConnectedThread.write("1");
        mConnectedThread.write("0");
    }
    public void bluetoothSendOn(View view){
        mConnectedThread.write("1");
    }
    public void bluetoothSendOff(View view){
        mConnectedThread.write("0");
    }

    public void configurarAdaptadorBluetooth(){
        /* Obtenemos el adaptador Bluetooth. Si es NULL, significara que el
           dispositivo no posee Bluetooth, por lo que deshabilitamos el boton
           encargado de activar/desactivar esta caracteristica. */
        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            btnBluetoothSet.setEnabled(false);
            return;
        }else{
            //Activamos los elementos de la interfaz
        }
        /* Comprobamos si el Bluetooth esta activo y cambiamos el texto del
           boton dependiendo del estado. */
        if(bluetoothAdapter.isEnabled()){
            btnBluetoothSet.setText(R.string.txtDeactivateBluetooth);
            //Carga los dispositivos vinculados
            pariedDevices.addAll(bluetoothAdapter.getBondedDevices());
            devicesAdapter.notifyDataSetChanged();
            //devicesAdapter.updateDevices(pariedDevices);
        }else{
            btnBluetoothSet.setText(R.string.txtActivateBluetooth);
        }

        /* Instanciamos un BroadcastReceiver que se encargara de detectar si el estado
           del Bluetooth del dispositivo ha cambiado mediante su handler onReceive */
        bbReceiver = new BluetoothBroadcastReceiver(btnBluetoothSet);
        registrarEventosBluetooth();

    }

    private void registrarEventosBluetooth(){
        // Registramos el BroadcastReceiver que instanciamos previamente para
        // detectar los distintos eventos que queremos recibir
        IntentFilter filtro = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(bbReceiver, filtro);
    }

    public void changeBluetooth(View view){
        if(bluetoothAdapter.isEnabled()){
            bluetoothAdapter.disable();
        }else{
            // Lanzamos el Intent que mostrara la interfaz de activacion del
            // Bluetooth. La respuesta de este Intent se manejara en el metodo
            // onActivityResult
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        /**
         * Handler del evento desencadenado al retornar de una actividad. En este caso, se utiliza
         * para comprobar el valor de retorno al lanzar la actividad que activara el Bluetooth.
         * En caso de que el usuario acepte, resultCode sera RESULT_OK
         * En caso de que el usuario no acepte, resultCode valdra RESULT_CANCELED
         */
        switch(requestCode)
        {
            case REQUEST_ENABLE_BT:
            {
                if(resultCode == RESULT_OK)
                {
                    // Acciones adicionales a realizar si el usuario activa el Bluetooth
                    Toast.makeText(this,R.string.txtAllowBluetooth,
                                    Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // Acciones adicionales a realizar si el usuario no activa el Bluetooth
                    Toast.makeText(this,getString(R.string.txtDenyBluetooth),
                                    Toast.LENGTH_SHORT).show();
                }
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        // Ademas de realizar la destruccion de la actividad, eliminamos el registro del
        // BroadcastReceiver.
        super.onDestroy();
        this.unregisterReceiver(bbReceiver);
    }
}
