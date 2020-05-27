import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

public class BluetoothActivity extends Activity {

    private ArrayAdapter<String> listAdapter;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

    public static String EXTRA_DEVICE_NAME = "device_name";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable(); 
        }

        //LAYOUT
        LinearLayout lLayout = new LinearLayout(this);
        lLayout.setOrientation(LinearLayout.VERTICAL);

        //HINWEISTEXT
        TextView tvHint = new TextView(this);
        tvHint.setText("Die hier gezeigten Eintr\u00E4ge sind Ger\u00E4te, die bereits mit dem Handy gekoppelt wurden.\nBitte Bluetooth-Einstellungen nutzen um ein unbekanntes Ger\u00E4t zu koppeln, dann diese App neustarten.");
        LayoutParams params = new LayoutParams(
            LayoutParams.MATCH_PARENT,      
            LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 32);
        tvHint.setLayoutParams(params);
        lLayout.addView(tvHint);

        //LISTE DER GEKOPPELTEN GERÄTE
        ListView list = new ListView(this);

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        updateList();
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(mDeviceClickListener);

        lLayout.addView(list);

        setContentView(lLayout);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Unregister broadcast listeners
        try {
            unregisterReceiver(mReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int position, long arg3) {
            // Create the result Intent and include the MAC address
            BluetoothDevice device = new ArrayList<>(devices).get(position);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, device.getAddress());
            intent.putExtra(EXTRA_DEVICE_NAME, device.getName());

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    void updateList() {
        ArrayList<String> names = new ArrayList<String>();
        for (BluetoothDevice device: devices) {
            names.add(device.getName() + "\n" + device.getAddress());
        }
        if (listAdapter != null) {
            listAdapter.clear();
            listAdapter.addAll(names);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
    
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                                                     BluetoothAdapter.ERROR);
                switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    // Bluetooth has been turned off;
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    // Bluetooth is turning off;
                    break;
                case BluetoothAdapter.STATE_ON:
                    // Bluetooth is on
                    toast("Bluetooth an");
                    devices = mBluetoothAdapter.getBondedDevices();
                    updateList();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    // Bluetooth is turning on
                    toast("Bluetooth wird eingeschaltet...");
                    break;
                }
            }
        }
    };

    void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}