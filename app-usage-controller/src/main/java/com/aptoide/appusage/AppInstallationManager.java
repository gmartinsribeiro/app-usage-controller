package com.aptoide.appusage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by franciscofariaaleixo on 04-08-2016.
 */
public class AppInstallationManager {
    private static AppInstallationListener listener;

    public static void setAppInstalationListener(AppInstallationListener listener){
        AppInstallationManager.listener = listener;
    }

    public static class InstallationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(listener != null){
                String action = intent.getAction();
                int uid = intent.getIntExtra(Intent.EXTRA_UID, -1);
                if(action.equals(Intent.ACTION_PACKAGE_ADDED)){
                    listener.onAppInstalled(uid);
                }
                else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)){
                    listener.onAppRemoved(uid);
                }
            }
        }

    }
}
