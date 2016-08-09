package com.aptoide.appusage;

/**
 * Created by franciscofariaaleixo on 04-08-2016.
 */
public interface AppInstallationListener {

    void onAppInstalled(int uid);

    void onAppRemoved(int uid);
}
