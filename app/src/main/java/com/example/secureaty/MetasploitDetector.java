package com.example.secureaty;

import android.util.Log;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MetasploitDetector {

    public static boolean checkPermissions(String code) {
        boolean changeWiFiState = code.contains("android.permission.CHANGE_WIFI_STATE");
        boolean readSMS = code.contains("android.permission.READ_SMS");
        boolean recordAudio = code.contains("android.permission.RECORD_AUDIO");
        boolean accessLocation = code.contains("android.permission.ACCESS_FINE_LOCATION");
        boolean camera = code.contains("android.permission.CAMERA");
        boolean wakeLock = code.contains("android.permission.WAKE_LOCK");

        return changeWiFiState && readSMS && recordAudio && accessLocation && camera && wakeLock;
    }

    public static boolean detect(String code) {
        try {
            Pattern pt = Pattern.compile(".*?\\bString\\b.*?\"\\.jar\".*?\\bString\\b.*?\"\\.dex\".*?\\bnew DexClassLoader\\b\\(.*?\\)\\).*?", Pattern.DOTALL);
            boolean hidesIcon = code.contains("new ComponentName(packageName, resolveInfo.activityInfo.name), 2, 1") || code.contains("new ComponentName(packageName, resolveInfo.activityInfo.name), 2, 1");
            boolean keepsAwake = code.contains("WakeLock newWakeLock = ((PowerManager) b.getSystemService(\"power\")).newWakeLock(1, Payload.class.getSimpleName()");
            boolean loadsStage = pt.matcher(code).matches();
            boolean suspiciousPermissions = checkPermissions(code);

            return hidesIcon && keepsAwake && loadsStage && suspiciousPermissions;
        } catch(Exception e){
            return false;
        }
    }

}
