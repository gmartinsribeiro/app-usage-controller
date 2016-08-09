# App Usage Controller
Library for app usage and events control.
Retrieves system info about app usage (most used/recently used) and detects installation events.

## Installation
You can easily install this module using JCenter, by adding the following dependency to your build.gradle file:

```java
dependencies {
 compile 'com.aptoide.pt:app-usage-controller:1.0.0'
 //...
}
```
## Usage
Apart from this section, a complete example can be downloaded from this repo.

### App Usage
To retrieve information about the apps usage you need to use AppUsageManager class, which implements AppUsageStats.
You have two methods that you can currently use: getMostUsedApps() and getRecentlyUsedApps(). Both return a list of AppUsageInfo ordered by the most used apps and the most recently used apps respectively.

Note that you need to catch SecurityException in case the app has no usage permissions (you can show a settings dialog, for example).

```java
    private void getAppList() {
        AppUsageManager mAppUsage = new AppUsageManager(getActivity());

        try {
            List<AppUsageInfo> mostUsedApps = mAppUsage.getMostUsedApps();
            List<AppUsageInfo> recentlyUsedApps = mAppUsage.getRecentlyUsedApps();
            // do something
        } catch (SecurityException e){
            showSettingsDialog();
        }
    }
    
    // Example of what you can do if the app has no usage permissions
    public void showSettingsDialog(){
        Dialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AppTheme))
                    .setTitle("Usage Settings")
                    .setMessage("For this functionality to work you need to grant app usage permission to this app. Do you want to do it now?")
                    .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent =new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_lock_lock).create();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    // Do something
                }
            });
        dialog.show();

    }
```
### Installation/Unninstallation Listeners
To use the Installation/Unninstallation listeners, you simply need to implement AppInstalationListener and set it on AppInstallationManager, as shown below.

```java
public class MainActivity extends AppCompatActivity implements AppInstallationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set listener
        AppInstallationManager.setAppInstalationListener(this);

    }


    @Override
    public void onAppInstalled(int uid) {
       // Do something
    }

    @Override
    public void onAppRemoved(int uid) {
        // Do something
    }

}

```
