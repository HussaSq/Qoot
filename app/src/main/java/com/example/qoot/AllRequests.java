package com.example.qoot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class AllRequests extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem tab3, tab4;
    public pageAdapter1 pagerAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_requests);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tab3 = (TabItem) findViewById(R.id.Tab3);
        tab4 = (TabItem) findViewById(R.id.Tab4);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter1 = new pageAdapter1(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter1);
        tabLayout.getTabAt(0).setIcon(R.drawable.clock2);
        tabLayout.getTabAt(1).setIcon(R.drawable.schedule2);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    pagerAdapter1.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    pagerAdapter1.notifyDataSetChanged();
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
        Intent intent1 = getIntent();
        String userId = intent1.getStringExtra("user");
        Intent intent = new Intent(AllRequests.this,tab3.class);
        intent.putExtra("user",userId);
        startActivity(intent);
        //startActivity(new Intent(AllRequests.this, tab3.class));
    }

    public void openScheduleForm(View view) {
        startActivity(new Intent(AllRequests.this,tab4.class));
    }

    public void OpenVolunteerRequests(View view) {
        Intent intent1 = getIntent();
        String userId = intent1.getStringExtra("user");
        Intent intent = new Intent(AllRequests.this,VolunteerRequests.class);
        intent.putExtra("user",userId);
        startActivity(intent);
       // startActivity(new Intent(AllRequests.this,VolunteerRequests.class));
    }
}