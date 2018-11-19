package com.example.irene.smarthelper;

import java.util.UUID;

public class CustomBluetoothProfile {

    public static class HeartRate {
        public static UUID service = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
        public static UUID measurementCharacteristic = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
        public static UUID descriptor = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        public static UUID controlCharacteristic = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    }
}
