package com.aptoide.appusage;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by franciscofariaaleixo on 03-08-2016.
 */
public class AppUsageManager implements AppUsageStats{

    private UsageStatsManager mUsageStatsManager;
    private Context context;

    public enum Filter {SYSTEM, INSTALLED};

    public AppUsageManager(Context context){
        this.context = context;
        this.mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
    }

    @Override
    public List<AppUsageInfo> getMostUsedApps() throws SecurityException{
        List<AppUsageInfo> apps = getUsageStats();
        Collections.sort(apps, new AppUsageInfo.UsageTimeSort());
        return apps;
    }

    @Override
    public List<AppUsageInfo> getRecentlyUsedApps() throws SecurityException{
        List<AppUsageInfo> apps = getUsageStats();
        Collections.sort(apps, new AppUsageInfo.LastUsedSort());
        return apps;
    }

    private List<AppUsageInfo> getUsageStats() throws SecurityException{
        //checkPermissions();
        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.YEAR, 1970);
        Calendar endCal = Calendar.getInstance();
        long startTime = startCal.getTimeInMillis();
        long endTime = endCal.getTimeInMillis();

        List<UsageStats> queryUsageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, startTime, endTime);
        if(queryUsageStats.size() == 0){
            throw new SecurityException();
        }
        List<AppUsageInfo> apps = new ArrayList<>();
        for (UsageStats stats : queryUsageStats) {
            Calendar lastTimeCalendar = Calendar.getInstance();
            lastTimeCalendar.setTimeInMillis(stats.getLastTimeUsed());
            String appName = getPackageAppName(stats.getPackageName());

            apps.add(new AppUsageInfo(stats.getPackageName(), appName, stats.getTotalTimeInForeground(), lastTimeCalendar));
        }
        return apps;
    }


    /**
     * Checks if app has usage permission. Only works on systems where this permission is defined.
     * @param context
     * @return true if app has usage permission.
     */
    public static boolean hasUsagePermission(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), context.getPackageName());
            boolean granted = mode == AppOpsManager.MODE_ALLOWED;
            return granted;
        }
        else{
            return false;
        }
    }

    private String getPackageAppName(String packageName){
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        return (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
    }

    /**
     * Filters app list based on a specific filter.
     * The filter can act as an exclusion filter or as an exclusive filter (e.g. ignore system apps or only show system apps).
     * @param filterOut - true if filter excludes apps or false if filter acts as an exclusive.
     */

    public void filter (boolean filterOut, List<AppUsageInfo> apps, Filter filter){
        PackageManager pm = context.getPackageManager();

        for(int i = 0; i<apps.size(); i++){
            AppUsageInfo app = apps.get(i);
            String packageName = app.getPackageName();

            if(filter == Filter.INSTALLED){
                if((pm.getLaunchIntentForPackage(packageName) != null) && filterOut){
                    apps.remove(i);
                    i--;
                }
                else if(!(pm.getLaunchIntentForPackage(packageName) != null) && !filterOut){
                    apps.remove(i);
                    i--;
                }
            }
            else if(filter == Filter.SYSTEM){
                if(!(pm.getLaunchIntentForPackage(packageName) != null) && filterOut){
                    apps.remove(i);
                    i--;
                }
                else if((pm.getLaunchIntentForPackage(packageName) != null) && !filterOut){
                    apps.remove(i);
                    i--;
                }
            }
        }
    }

    /**
     * Filters app list based on a list of package names.
     * The filter can act as an exclusion filter or as an exclusive filter (e.g. ignore system apps or only show system apps).
     * @param filterOut - true if filter excludes apps or false if filter acts as an exclusive.
     */

    public void filter (boolean filterOut, List<AppUsageInfo> apps, String[] packageNames){
        for(int i = 0; i<apps.size(); i++){
            AppUsageInfo app = apps.get(i);
            String packageName = app.getPackageName();

            if(stringArrayContains(packageName, packageNames) && filterOut){
                apps.remove(i);
                i--;
            }
            else if(!stringArrayContains(packageName, packageNames) && !filterOut){
                apps.remove(i);
                i--;
            }
        }
    }

    private static boolean stringArrayContains(String string, String[] strings){
        for(String s : strings){
            if(s.equals(string)){
                return true;
            }
        }
        return false;
    }

}
