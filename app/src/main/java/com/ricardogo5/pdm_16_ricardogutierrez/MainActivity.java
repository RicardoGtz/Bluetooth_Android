package com.ricardogo5.pdm_16_ricardogutierrez;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //Referencia a BluetoothAdapter
    private android.bluetooth.BluetoothAdapter bluetoothAdapter;
    private Button btnBluetoothSet;
    private BluetoothBroadcastReceiver bbReceiver;
    private static final int    REQUEST_ENABLE_BT   = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Enlazamos el boton con la vista
        btnBluetoothSet=findViewById(R.id.btnBluetoothSet);
        configurarAdaptadorBluetooth();

    }

    public void configurarAdaptadorBluetooth(){
        /* Obtenemos el adaptador Bluetooth. Si es NULL, significara que el
           dispositivo no posee Bluetooth, por lo que deshabilitamos el boton
           encargado de activar/desactivar esta caracteristica. */
        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter==null){
            btnBluetoothSet.setEnabled(false);
            return;
        }
        /* Comprobamos si el Bluetooth esta activo y cambiamos el texto del
           boton dependiendo del estado. */
        if(bluetoothAdapter.isEnabled()){
            btnBluetoothSet.setText(R.string.txtDesactivarBluetooth);
        }else{
            btnBluetoothSet.setText(R.string.txtActivarBluetooth);
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

    /**
     * Handler del evento desencadenado al retornar de una actividad. En este caso, se utiliza
     * para comprobar el valor de retorno al lanzar la actividad que activara el Bluetooth.
     * En caso de que el usuario acepte, resultCode sera RESULT_OK
     * En caso de que el usuario no acepte, resultCode valdra RESULT_CANCELED
     */
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case REQUEST_ENABLE_BT:
            {
                if(resultCode == RESULT_OK)
                {
                    // Acciones adicionales a realizar si el usuario activa el Bluetooth
                }
                else
                {
                    // Acciones adicionales a realizar si el usuario no activa el Bluetooth
                }
                break;
            }

            default:
                break;
        }
    }

    // Ademas de realizar la destruccion de la actividad, eliminamos el registro del
    // BroadcastReceiver.
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(bbReceiver);
    }
}
