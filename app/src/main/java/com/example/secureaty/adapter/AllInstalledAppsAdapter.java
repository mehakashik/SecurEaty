package com.example.secureaty.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureaty.R;

import java.util.List;

public class AllInstalledAppsAdapter extends RecyclerView.Adapter<AllInstalledAppsAdapter.AllInstalledAppsViewHolder> {

    private final Context context;
    private final OnAppItemClickListener onAppItemClickListener;
    private List<PackageInfo> packageInfoList;

    public AllInstalledAppsAdapter(Context context, List<PackageInfo> packageInfoList, OnAppItemClickListener onAppItemClickListener) {
        this.context = context;
        this.packageInfoList = packageInfoList;
        this.onAppItemClickListener = onAppItemClickListener;
    }

    @NonNull
    @Override
    public AllInstalledAppsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_installed_app, parent, false);
        return new AllInstalledAppsAdapter.AllInstalledAppsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllInstalledAppsViewHolder holder, int position) {
        holder.bind(context, packageInfoList.get(position), onAppItemClickListener);

    }

    @Override
    public int getItemCount() {
        if (packageInfoList == null) return 0;
        return packageInfoList.size();
    }

    public void updateApplicationInfoList(List<PackageInfo> packageInfoList) {
        if (packageInfoList != null) {
            this.packageInfoList = packageInfoList;
        }
    }

    public interface OnAppItemClickListener {
        void onAppItemClick(PackageInfo packageInfo);
    }

    static class AllInstalledAppsViewHolder extends RecyclerView.ViewHolder {
        private final ImageView appLogoImageView;
        private final TextView appNameTextView;
        private final TextView appPackageNameTextView;
        private final ConstraintLayout appPermissionConstraintLyt;

        public AllInstalledAppsViewHolder(@NonNull View itemView) {
            super(itemView);
            appLogoImageView = itemView.findViewById(R.id.appLogoImageView);
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
            appPackageNameTextView = itemView.findViewById(R.id.appPackageNameTextView);
            appPermissionConstraintLyt = itemView.findViewById(R.id.appPermissionConstraintLyt);
        }

        public void bind(Context context, final PackageInfo packageInfo, final OnAppItemClickListener onAppItemClickListener) {
            appLogoImageView.setImageDrawable(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
            appNameTextView.setText(packageInfo.applicationInfo.loadLabel(context.getPackageManager()));
            appPackageNameTextView.setText(packageInfo.packageName);
            appPermissionConstraintLyt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAppItemClickListener.onAppItemClick(packageInfo);
                }
            });
        }
    }
}
