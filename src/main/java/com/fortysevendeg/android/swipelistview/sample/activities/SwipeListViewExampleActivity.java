/*
 * Copyright (C) 2013 47 Degrees, LLC
 *  http://47deg.com
 *  hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.fortysevendeg.android.swipelistview.sample.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.fortysevendeg.android.swipelistview.R;
import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.fortysevendeg.android.swipelistview.SwipeListViewListener;
import com.fortysevendeg.android.swipelistview.sample.adapters.PackageAdapter;
import com.fortysevendeg.android.swipelistview.sample.adapters.PackageItem;
import com.fortysevendeg.android.swipelistview.sample.dialogs.AboutDialog;
import com.fortysevendeg.android.swipelistview.sample.utils.PreferencesManager;
import com.fortysevendeg.android.swipelistview.sample.utils.SettingsManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwipeListViewExampleActivity extends FragmentActivity {

    private static final int REQUEST_CODE_SETTINGS = 0;
    private PackageAdapter adapter;
    private List<PackageItem> data;

    private SwipeListView swipeListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.swipe_list_view_activity);

        data = new ArrayList<PackageItem>();

        PackageManager appInfo = getPackageManager();
        List<ApplicationInfo> listInfo = appInfo.getInstalledApplications(0);
        Collections.sort(listInfo, new ApplicationInfo.DisplayNameComparator(appInfo));


        for (int index=0; index<listInfo.size(); index++) {
            try {
                ApplicationInfo content = listInfo.get(index);
                if ( (content.flags != ApplicationInfo.FLAG_SYSTEM) && content.enabled) {
                    if (content.icon!=0) {
                        PackageItem item = new PackageItem();
                        item.setName(getPackageManager().getApplicationLabel(content).toString());
                        item.setPackageName(content.packageName);
                        item.setIcon(getPackageManager().getDrawable(content.packageName, content.icon, content));
                        data.add(item);
                    }
                }
            } catch (Exception e) {

            }
        }


        adapter = new PackageAdapter(this, data);

        swipeListView = (SwipeListView) findViewById(R.id.example_lv_list);

        swipeListView.setSwipeListViewListener(new SwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", "onClickFrontView " + position);
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", "onClickBackView " + position);
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    data.remove(position);
                }
                adapter.notifyDataSetChanged();
            }
        });

        swipeListView.setAdapter(adapter);

        reload();

        if (PreferencesManager.getInstance(this).getShowAbout()) {
            AboutDialog logOutDialog = new AboutDialog();
            logOutDialog.show(getSupportFragmentManager(), "dialog");
        }


    }

    private void reload() {
        SettingsManager settings = SettingsManager.getInstance();
        swipeListView.setSwipeMode(settings.getSwipeMode());
        swipeListView.setSwipeActionLeft(settings.getSwipeActionLeft());
        swipeListView.setSwipeActionRight(settings.getSwipeActionRight());
        swipeListView.setOffsetLeft(convertDpToPixel(settings.getSwipeOffsetLeft()));
        swipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
        swipeListView.setAnimationTime(settings.getSwipeAnimationTime());
        swipeListView.setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
    }

    public int convertDpToPixel(float dp){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi/160f);
        return (int) px;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        boolean handled = false;
        switch (item.getItemId()) {
            case android.R.id.home: //Actionbar home/up icon
                finish();
                break;
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SETTINGS);
                break;
        }
        return handled;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SETTINGS:
                reload();
        }
    }

}
