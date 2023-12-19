package com.example.crfid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.crfid.materialTagPair_Fragments.adapter.MatTagPair_Frag_Adaptor;
import com.example.crfid.materialTagUnpair_Fragments.adapter.MatTagUnPair_Frag_adaptor;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MaterialTagUnpair extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    Button unpair;

    MatTagUnPair_Frag_adaptor fragadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_tag_unpair);
        unpair = findViewById ( R.id.UnPairButton );
        tabLayout=findViewById ( R.id.tagUnpairTab );
        viewPager2=findViewById ( R.id.viewpagerTagUnpair );
        fragadapter=new MatTagUnPair_Frag_adaptor ( this );
        viewPager2.setAdapter ( fragadapter );
        viewPager2.setUserInputEnabled ( false );
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setClickable(false); // Disable click
                tab.view.setFocusable(false); // Disable focus
                // You might also want to change other properties as needed, like background, etc.
            }
        }
        new TabLayoutMediator (tabLayout, viewPager2,
                (tab, position) ->
//                        tab.setText("Tab " + (position + 1))
                {

                    switch (position) {
                        case 0:
                            tab.setText("List");
                            break;
                        case 1:
                            tab.setText("Item-List");
                            break;
                        case 2:
                            tab.setText("Item-Details");
                            break;
                        // Add more cases if you have more tabs
                    }
                }
        ).attach();
        tabLayout.setOnTabSelectedListener ( new TabLayout.OnTabSelectedListener ( ) {
            @Override
            public
            void onTabSelected ( TabLayout.Tab tab ) {
//                viewPager2.setCurrentItem ( tab.getPosition () );
                int selectedTabPosition = tab.getPosition();

                // Enable the "Pair" button only on the third tab (position 2)
                if (selectedTabPosition == 2) {
                    unpair.setVisibility ( View.VISIBLE );
                } else {
                    unpair.setVisibility(View.GONE);
                }
            }

            @Override
            public
            void onTabUnselected ( TabLayout.Tab tab ) {

            }

            @Override
            public
            void onTabReselected ( TabLayout.Tab tab ) {

            }
        } );
    }
}