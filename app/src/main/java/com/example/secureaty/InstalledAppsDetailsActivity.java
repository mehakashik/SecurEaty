package com.example.secureaty;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InstalledAppsDetailsActivity extends AppCompatActivity {

    PackageInfo selectedPackageInfo;

    ImageView appLogoImageView;
    TextView appNameTextView;
    TextView appPackageNameTextView;
    TextView allPermissionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installed_apps_details);
        selectedPackageInfo = getIntent().getExtras().getParcelable(AllInstalledAppsActivity.APP_INFO_INTENT);
        initViews();
        setContent();
    }

    private void initViews() {
        appLogoImageView = findViewById(R.id.appLogoImageView);
        appNameTextView = findViewById(R.id.appNameTextView);
        appPackageNameTextView = findViewById(R.id.appPackageNameTextView);
        allPermissionsTextView = findViewById(R.id.allPermissionsTextView);
    }

    private void setContent() {
        appLogoImageView.setImageDrawable(selectedPackageInfo.applicationInfo.loadIcon(getPackageManager()));
        appNameTextView.setText(selectedPackageInfo.applicationInfo.loadLabel(getPackageManager()));
        appPackageNameTextView.setText(selectedPackageInfo.applicationInfo.packageName);

        //Permissions:
        try {
            StringBuilder permissions = new StringBuilder();
            PackageInfo packageInfo = getPackageManager().getPackageInfo(selectedPackageInfo.applicationInfo.packageName, PackageManager.GET_PERMISSIONS);

            String[] requestedPermissions = packageInfo.requestedPermissions;
            if (requestedPermissions != null) {
                for (String requestedPermission : requestedPermissions) {
                    permissions.append("-> ").append(requestedPermission).append("\n\n");
                }
            }
            allPermissionsTextView.setText(permissions.toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

}