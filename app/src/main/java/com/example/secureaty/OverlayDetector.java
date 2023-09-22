package com.example.secureaty;

import android.util.Log;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OverlayDetector {

    public static boolean detect(String code){

        Matcher Strflags = Pattern.compile("WindowManager\\.LayoutParams\\(.*?,.*?,.*?,(.*?),.*?\\)", Pattern.DOTALL)
                .matcher(code);
        int flags = 0;
        while(Strflags.find()) {
            String numbersStr = Strflags.group(1);
            String[] strArray = numbersStr.split("\\|");

            for (String str : strArray) {
                try {
                    Integer flag = Integer.parseInt(str.trim());
                    flags = flags | flag;
                } catch (NumberFormatException e) {}
            }
        }

        boolean goHome = code.contains("android.intent.category.HOME");
        boolean alertWindow = code.contains("android.permission.SYSTEM_ALERT_WINDOW");
        boolean accessibilityService = code.contains("android.permission.BIND_ACCESSIBILITY_SERVICE");
        boolean overlayPermission = code.contains("new Intent(\"android.settings.action.MANAGE_OVERLAY_PERMISSION\")");
        boolean checkForFlags = ((flags & 8) != 0) &&
                (flags & (16 | 32 | 262144)) != 0;
        return goHome & alertWindow & accessibilityService & overlayPermission & checkForFlags;
    }

}
