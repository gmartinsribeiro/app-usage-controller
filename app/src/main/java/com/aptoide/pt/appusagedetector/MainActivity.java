package com.aptoide.pt.appusagedetector;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.aptoide.appinformer.AppInstallationListener;
import com.aptoide.appinformer.AppInstallationManager;
import com.aptoide.appinformer.AppUsageManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AppInstallationListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private boolean showedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Set listener
        AppInstallationManager.setAppInstalationListener(this);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MostUsedFragment(), "Most Used");
        adapter.addFragment(new LastUsedFragment(), "Last Used");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onAppInstalled(int uid) {
        /*PackageManager pm = getPackageManager();
        String[] packages = pm.getPackagesForUid(uid);
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(packages[0], 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }*/

        Toast.makeText(getApplicationContext(), "AppUsage: An app has been installed.", Toast.LENGTH_LONG).show(); //"+ pm.getApplicationLabel(ai)+
        System.out.println("An app has been installed.");
    }

    @Override
    public void onAppRemoved(int uid) {
        Toast.makeText(getApplicationContext(), "AppUsage: An app has been removed.", Toast.LENGTH_LONG).show(); //"+ pm.getApplicationLabel(ai)+
        System.out.println("An app has been removed.");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void openUsageMenu(View view){
        Intent intent=new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    public void showSettingsDialog(){
        if(!showedDialog){
            showedDialog = true;
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
                    showedDialog = false;
                    String perm = "no permission.";
                    if(AppUsageManager.hasUsagePermission(getApplicationContext())){
                        perm = "permission.";
                    }
                    Toast.makeText(getApplicationContext(), "User dismissed dialog with "+ perm, Toast.LENGTH_LONG).show();
                }
            });
            dialog.show();
        }
    }
}
