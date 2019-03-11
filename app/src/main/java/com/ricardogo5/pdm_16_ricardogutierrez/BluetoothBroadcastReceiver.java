package com.ricardogo5.pdm_16_ricardogutierrez;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

public class BluetoothBroadcastReceiver extends BroadcastReceiver {
    Button btnBluetooth;
    public BluetoothBroadcastReceiver(Button btn){
        super();
        this.btnBluetooth=btn;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        // Filtramos por la accion. Nos interesa detectar BluetoothAdapter.ACTION_STATE_CHANGED
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
        {
            /* Solicitamos la informacion extra del intent etiquetada como BluetoothAdapter.EXTRA_STATE
               El segundo parametro indicara el valor por defecto que se obtendra si el dato extra no existe */
            final int estado = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);

            switch (estado)
            {
                // Apagado
                case BluetoothAdapter.STATE_OFF:
                {
                    btnBluetooth.setText(R.string.txtActivateBluetooth);
                    break;
                }

                // Encendido
                case BluetoothAdapter.STATE_ON:
                {
                    btnBluetooth.setText(R.string.txtDeactivateBluetooth);

                    // Lanzamos un Intent de solicitud de visibilidad Bluetooth, al que añadimos un par
                    // clave-valor que indicara la duracion de este estado, en este caso 120 segundos
                    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
                    btnBluetooth.getContext().startActivity(discoverableIntent);

                    break;
                }
                default:
                    break;
            }

        }
    }
}
