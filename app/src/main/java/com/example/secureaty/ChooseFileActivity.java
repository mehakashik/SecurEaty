package com.example.secureaty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ChooseFileActivity extends AppCompatActivity {

    // stuff to get permission to internal files
    private PermissionResolver permissionResolver;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!permissionResolver.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    // end of stuff to get permission to internal files

    Button nextBtn,deselectAllBtn,selectAllBtn;

    private ListView lv;
    private ArrayList<ListViewItem> listViewItems;
    private PackageListAdapter packageListAdapter;
    private List<PackageInfo> packages;
    public static List<PackageInfo> selectedPackages = new ArrayList<>();
    private PackageManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);
        manager = getPackageManager();
        packages = manager.getInstalledPackages(PackageManager.GET_META_DATA);

        lv = (ListView) findViewById(R.id.lv);
        nextBtn = (Button) findViewById(R.id.btn_next);
        deselectAllBtn = (Button) findViewById(R.id.btn_deselectAll);
        selectAllBtn = (Button) findViewById(R.id.btn_selectAll);

        listViewItems = getFiles(false);
        packageListAdapter = new PackageListAdapter(this, listViewItems);
        lv.setAdapter(packageListAdapter);

        selectAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewItems = getFiles(true);
                packageListAdapter = new PackageListAdapter(ChooseFileActivity.this, listViewItems);
                lv.setAdapter(packageListAdapter);
            }
        });
        deselectAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewItems = getFiles(false);
                packageListAdapter = new PackageListAdapter(ChooseFileActivity.this, listViewItems);
                lv.setAdapter(packageListAdapter);
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseFileActivity.this,NextActivity.class);
                ArrayList<PackageInfo> list = new ArrayList<>();
                for (PackageInfo pkg : packages) {
                    String appName = manager.getApplicationLabel(pkg.applicationInfo).toString();
                    for (ListViewItem item : listViewItems) {
                        if (item.getSelected() && (item.getPackageName() == appName)) {
                            selectedPackages.add(pkg);
                        }
                    }
                }

                startActivity(intent);
            }
        });
        permissionResolver = new PermissionResolver(this);

    }


    private ArrayList<ListViewItem> getFiles(boolean isSelect){
        ArrayList<ListViewItem> list = new ArrayList<>();
        for(PackageInfo p: packages){   //getting apk names from packageInfo
            if((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {   //check if the application is system and exclude if yes
                Log.d("toString", p.packageName);

                String name = manager.getApplicationLabel(p.applicationInfo).toString();

                ListViewItem item = new ListViewItem();
                item.icon = manager.getApplicationIcon(p.applicationInfo);
                item.setSelected(isSelect);
                item.setPackageName(name);
                list.add(item);
            }
        }
        return list;
    }
}



