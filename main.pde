import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.widget.Toast;
import android.view.Gravity;
import android.app.Activity;

Screen currentScreen;
ComponentBtStatus btStatusComponent;

PImage bgImage;
PFont robotoMedium;

Boat currentBoat;

BtManager btManager;

final static Boolean demoMode = false;

void setup() {
    smooth(2);
    fullScreen(P2D);

    bgImage = loadImage("wasser.jpg");

    robotoMedium = createFont("Roboto-Medium.ttf", 32);
    textFont(robotoMedium);

    if (!demoMode) currentScreen = new ScreenConnecting();
    else currentScreen = new ScreenMain();
    btStatusComponent = new ComponentBtStatus();
}


void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!demoMode) {
        btManager = new BtManager(new KetaiBluetooth(this));
        launchDeviceSelectActivity();
    }
}

void launchDeviceSelectActivity() {
    Intent intent = new Intent(this.getActivity(), BluetoothActivity.class);
    getActivity().startActivityForResult(intent, 2);
}

void draw() {

    if (btManager.isConnected()) {
        if (!currentBoat.connected) currentBoat.setConnected(true);
        if (currentScreen instanceof ScreenConnecting) changeScreen(new ScreenMain());
    } else {
        if (currentBoat.connected) currentBoat.setConnected(false);
        if (currentScreen instanceof ScreenMain) {
            changeScreen(new ScreenConnecting());
            launchDeviceSelectActivity();
        }
    }

    clear();
    //Hintergrundbild zeichnen
    image(bgImage, 0, 0, width, height);
    //Aktuellen Bildschirm zeichnen
    currentScreen.draw();
    //Bluetooth-Statusleiste zeichnen
    btStatusComponent.updateBoat(currentBoat);
    btStatusComponent.draw();
}

void changeScreen(Screen nextScreen) {
    currentScreen = nextScreen;
    currentScreen.updateBoat(currentBoat);
}

void onBluetoothDataEvent(String who, byte[] data) {
  btManager.onBluetoothDataEvent(who, data);
}

void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (btManager != null) btManager.onActivityResult(requestCode, resultCode, data);
    //Berechtigung nicht gewährt
    if (requestCode == 1 && resultCode == 0) {
        CharSequence message = "Bluetooth wird ben\u00F6tigt, um das Boot zu steuern. Bitte zulassen.";
        Toast toast = Toast.makeText(this.getActivity().getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER, 0, 0);
        toast.show();
    } else if (requestCode == 2) {
        if (resultCode == Activity.RESULT_OK) {
            String address = data.getStringExtra(BluetoothActivity.EXTRA_DEVICE_ADDRESS);
            if (address != null && btManager != null) {
                currentBoat = new Boat(data.getStringExtra(BluetoothActivity.EXTRA_DEVICE_NAME), address, false);
                btManager.connect(address);
            }
        } else {
            getActivity().finish();
        }
    }
}
