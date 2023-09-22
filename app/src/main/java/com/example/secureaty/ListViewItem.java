package com.example.secureaty;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private boolean isSelected;
    private String packageName;
    public Drawable icon;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
