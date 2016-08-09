package com.aptoide.pt.appusagedetector;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptoide.appusage.AppUsageInfo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by franciscofariaaleixo on 03-08-2016.
 */
public class AppUsageInfoArrayAdapter extends ArrayAdapter<AppUsageInfo> {

    public enum FragmentType { MOSTUSED, LASTUSED };

    private Context context;
    private List<AppUsageInfo> apps;
    private FragmentType frag;
    private int resource;

    public AppUsageInfoArrayAdapter(Context context, int resource, List<AppUsageInfo> apps) {
        super(context, resource, apps);
        this.context = context;
        this.resource = resource;
        this.apps = apps;

    }

    public void setFragmentUser(FragmentType frag){
        this.frag = frag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppUsageInfo app = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
        }
        TextView appName = (TextView) convertView.findViewById(R.id.appNameTextView);
        TextView info = (TextView) convertView.findViewById(R.id.infoTextView);
        ImageView appImg = (ImageView) convertView.findViewById(R.id.imageView);

        appName.setText(app.getAppName());
        if(frag == FragmentType.MOSTUSED){
            String infoText = "Spent time: " + getTime(app.getUsageTime());
            info.setText(infoText);
        }
        else if (frag == FragmentType.LASTUSED){
            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
            String date = format.format(app.getLastUsedTime().getTime());
            info.setText(date);
        }

        // Set Icon
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(app.getPackageName(), 0);
            Drawable icon = pm.getApplicationIcon(ai);
            appImg.setImageDrawable(icon);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        if(ai == null){
            Drawable default_icon = pm.getDefaultActivityIcon();
            appImg.setImageDrawable(default_icon);
        }
        return convertView;
    }

    private String getTime(long ms){
        long fullseconds = ms/1000;
        int hours = (int) (fullseconds/3600);
        int minutes = (int) ((fullseconds%3600)/60);
        int seconds = (int) (fullseconds%60);

        return hours + "h " + minutes + "m " + seconds + "s";
    }
}
