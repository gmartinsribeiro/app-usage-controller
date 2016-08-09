package com.aptoide.appusage;

import java.util.List;

/**
 * Created by franciscofariaaleixo on 04-08-2016.
 */
public interface AppUsageStats {

    /**
     * Requests list of AppUsageInfo ordered by the most used apps in descending order.
     * @return List of AppUsageInfo
     */
    List<AppUsageInfo> getMostUsedApps();

    /**
     * Requests list of AppUsageInfo ordered by the recently used apps in descending order.
     * @return List of AppUsageInfo
     */
    List<AppUsageInfo> getRecentlyUsedApps();

}
