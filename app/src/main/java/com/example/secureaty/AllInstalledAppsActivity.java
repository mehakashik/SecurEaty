package com.example.secureaty;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureaty.adapter.AllInstalledAppsAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllInstalledAppsActivity extends AppCompatActivity implements AllInstalledAppsAdapter.OnAppItemClickListener{

    public static final String APP_INFO_INTENT = "com.example.secureaty.allinstalledappsactivity.APP_INFO_INTENT";
    private FrameLayout allAppsFrameLayout;
    private RecyclerView allAppsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_installed_apps);

        initViews();
        bindAdapterViewRecyclerView();
    }

    private void initViews() {
        allAppsFrameLayout = (FrameLayout) findViewById(R.id.allAppsFrameLayout);
        allAppsRecyclerView = (RecyclerView) findViewById(R.id.allAppsRecyclerView);
    }

    private void bindAdapterViewRecyclerView() {
        List<PackageInfo> packagesList = getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
        List<PackageInfo> validPackageInfoList = new ArrayList<>();
        for(PackageInfo currentPackageInfo: packagesList){
            if(currentPackageInfo.packageName != null){
                validPackageInfoList.add(currentPackageInfo);
            }
        }

        AllInstalledAppsAdapter allInstalledAppsAdapter =
                new AllInstalledAppsAdapter(AllInstalledAppsActivity.this, validPackageInfoList, this);
        allInstalledAppsAdapter.setHasStableIds(true);
        allAppsRecyclerView.setAdapter(allInstalledAppsAdapter);
        allAppsRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onAppItemClick(PackageInfo packageInfo) {
        Intent appsDetailsIntent = new Intent(AllInstalledAppsActivity.this, InstalledAppsDetailsActivity.class);
        appsDetailsIntent.putExtra(APP_INFO_INTENT, packageInfo);
        startActivity(appsDetailsIntent);
    }
}