package com.aptoide.appinformer;

import java.util.List;

/**
 * Created by franciscofariaaleixo on 04-08-2016.
 */
public interface AppUsageStats {

    List<AppUsageInfo> getMostUsedApps();

    List<AppUsageInfo> getRecentlyUsedApps();

}
