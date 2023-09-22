package com.example.secureaty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PackageListAdapter extends BaseAdapter {

    private Context context;
    public  static ArrayList<ListViewItem> viewItems;

    public PackageListAdapter(Context context, ArrayList<ListViewItem> viewItems){
        this.context=context;
        this.viewItems = viewItems;
    }

    @Override
    public  int getViewTypeCount(){
        return getCount();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @Override
    public int getCount(){
        return viewItems.size();
    }

    @Override
    public Object getItem(int position){
        return viewItems.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder(); LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.text_plus_checkbox, null, true);

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_plus);
            holder.tvFile = (TextView) convertView.findViewById(R.id.file);
            holder.icon = convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }


        holder.tvFile.setText(viewItems.get(position).getPackageName());
        holder.icon.setImageDrawable(viewItems.get(position).icon);
        holder.checkBox.setChecked(viewItems.get(position).getSelected());

        holder.checkBox.setTag(R.integer.btnplusview, convertView);
        holder.checkBox.setTag( position);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View tempview = (View) holder.checkBox.getTag(R.integer.btnplusview);
                TextView tv = (TextView) tempview.findViewById(R.id.file);
                Integer pos = (Integer)  holder.checkBox.getTag();
                Toast.makeText(context, "Checkbox "+(pos+1)+" clicked!", Toast.LENGTH_SHORT).show();

                if(viewItems.get(pos).getSelected()){
                    viewItems.get(pos).setSelected(false);
                }else {
                    viewItems.get(pos).setSelected(true);
                }

            }
        });

        return convertView;
    }

    private class ViewHolder {

        protected CheckBox checkBox;
        private TextView tvFile;
        public ImageView icon;

    }
}
