package com.aptoide.appusage;

import java.util.Calendar;
import java.util.Comparator;

/**
 * Created by franciscofariaaleixo on 03-08-2016.
 */
public class AppUsageInfo{
    private String packageName;
    private String appName;
    private long usageTime; // ms
    private Calendar lastUsedTime;

    public AppUsageInfo(String packageName,String appName, long usageTime, Calendar lastUsedTime) {
        this.setPackageName(packageName);
        this.setAppName(appName);
        this.setUsageTime(usageTime);
        this.setLastUsedTime(lastUsedTime);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public long getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(long usageTime) {
        this.usageTime = usageTime;
    }

    public Calendar getLastUsedTime() {
        return lastUsedTime;
    }

    public void setLastUsedTime(Calendar lastUsedTime) {
        this.lastUsedTime = lastUsedTime;
    }


    public static class UsageTimeSort implements Comparator<AppUsageInfo>{

        @Override
        public int compare(AppUsageInfo t1, AppUsageInfo t2) {
            return (int) (t2.getUsageTime() - t1.getUsageTime());
        }

    }

    public static class LastUsedSort implements Comparator<AppUsageInfo> {

        @Override
        public int compare(AppUsageInfo t1, AppUsageInfo t2) {
            long c = t2.getLastUsedTime().getTimeInMillis() - t1.getLastUsedTime().getTimeInMillis();
            if(c > 0 ){
                return 1;
            }
            else if(c < 0 ){
                return -1;
            }
            return 0;
        }
    }
}
