package com.ricardogo5.pdm_16_ricardogutierrez;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    //Referencia a BluetoothAdapter
    android.bluetooth.BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void configurarAdaptadorBluetooth(){

    }


}
