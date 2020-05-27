import ketai.net.bluetooth.*;

class BtManager {

    KetaiBluetooth kBt;
    Boolean connected = false;

    BtManager(KetaiBluetooth kBt) {
        this.kBt = kBt;
        kBt.start();
    }

    void connect(String address) {
        connected = kBt.connectDevice(address);
    }

    void send() {}

    void send(String data) {
        byte[] byteArrayData = new byte[data.length()];
        for (int i = 0; i < data.length(); i++) {
            byteArrayData[i] = byte(data.charAt(i));
        }
        send(byteArrayData);
    }

    void send(byte[] data) {
        kBt.broadcast(data);
    }

    Boolean isConnected() {
        return kBt.getConnectedDeviceAddresses().size() > 0;
    }

    void onBluetoothDataEvent(String who, byte[] data) {
        println("Received message from " + who);

        String response = "";

        for (byte dataByte: data) {
            response += String.valueOf((char) dataByte);
        }

        println(response);
    }

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        kBt.onActivityResult(requestCode, requestCode, data);
    }
}