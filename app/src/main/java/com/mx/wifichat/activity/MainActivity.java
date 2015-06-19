package com.mx.wifichat.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mx.wifichat.R;
import com.mx.wifichat.adapter.MainFragmentAdapter;
import com.mx.wifichat.fragment.ChatFragment;
import com.mx.wifichat.fragment.TransferFragment;
import com.mx.wifichat.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements ActionBar.TabListener {

    List<Fragment> mFragments;
    List<String> mFragmentTitles;
    MainFragmentAdapter mainFragmentAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mFragments = new ArrayList<Fragment>();
        mFragmentTitles = new ArrayList<String>();

        mFragments.add(new ChatFragment());
        mFragmentTitles.add("聊天");
        mFragments.add(new TransferFragment());
        mFragmentTitles.add("传文件");
        mFragments.add(new UserFragment());
        mFragmentTitles.add("我的");

        mainFragmentAdapter = new MainFragmentAdapter(getFragmentManager(), mFragments, mFragmentTitles);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mainFragmentAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mainFragmentAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mainFragmentAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

}
