package com.example.qoot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

 public class requestForm extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab1, tab2;
    public pageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_form);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tab1 = (TabItem) findViewById(R.id.Tab1);
        tab2 = (TabItem) findViewById(R.id.Tab2);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
      pagerAdapter = new pageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
      viewPager.setAdapter(pagerAdapter);
        tabLayout.getTabAt(0).setIcon(R.drawable.clock2);
        tabLayout.getTabAt(1).setIcon(R.drawable.schedule2);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    pagerAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void openUrgentForm(View view) {
        startActivity(new Intent(requestForm.this, tab1.class));
    }

    public void openScheduleForm(View view) {
        startActivity(new Intent(requestForm.this,tab2.class));
    }

     public void OpenDonatorRequest(View view) {
         startActivity(new Intent(requestForm.this,DonatorRequests.class));
     }
 }