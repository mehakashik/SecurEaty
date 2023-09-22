package com.example.secureaty;

import android.os.FileUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.regex.Pattern;

public class DOADetector {

    public static boolean manipulatedAndroidManifest(String code){
        boolean AndroidManifest = code.contains("AndroidManifest.xml");
        boolean pack = code.contains("javax.xml.") || code.contains("android.axml.");
        return AndroidManifest && pack;
    }

    public static boolean detect(String code) {

                Pattern createApkPattern = Pattern.compile(".*?\\bnew Intent\\b.*?\\bsetDataAndType\\b.*?\\bapplication\\/vnd.android.package-archive\\b.*?", Pattern.DOTALL);

                    boolean createdApp = createApkPattern.matcher(code).matches();
                    boolean permissionToInstall = code.contains("android.permission.REQUEST_INSTALL_PACKAGES");
                    boolean permissionToWrite = code.contains("android.permission.WRITE_EXTERNAL_STORAGE");
                    boolean manipulatedAndroidManifest = manipulatedAndroidManifest(code);

        return createdApp && !permissionToInstall && permissionToWrite && manipulatedAndroidManifest;
    }
}
