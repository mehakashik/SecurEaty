package com.example.secureaty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NextActivity extends AppCompatActivity {
    public PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Log.d("Next activity", "Loaded");
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        pm = getPackageManager();


        for (PackageInfo p : ChooseFileActivity.selectedPackages) {
            String appName = pm.getApplicationLabel(p.applicationInfo).toString();
            DecompileItem item = new DecompileItem(this, appName);

            parentLayout.addView(item.layoutItem);
            // add progress bar view
//            ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(30, 30, 30, 30);
//            progressBar.setLayoutParams(layoutParams);
//            parentLayout.addView(progressBar);

            Log.d("started Execution:", "started");
            decompileFile(new File(p.applicationInfo.sourceDir), item);
        }
    }

//      ----extract files (for future processing)----
        public List<File> getPackageFiles() {
            List<File> files = new ArrayList<>();
            for (PackageInfo p : ChooseFileActivity.selectedPackages) {
                files.add(new File(p.applicationInfo.sourceDir));
            }
            Log.d("files", "" + files);

            return files;
        }

    public void decompileFile(File file, DecompileItem item){
            DecompileAction action = new DecompileAction();
            action.setItem(item);
            action.execute(file);
    }


}