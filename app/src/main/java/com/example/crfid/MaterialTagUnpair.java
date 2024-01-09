package com.example.crfid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.crfid.materialTagPair_Fragments.adapter.MatTagPair_Frag_Adaptor;
import com.example.crfid.materialTagUnpair_Fragments.adapter.MatTagUnPair_Frag_adaptor;
import com.example.crfid.rfid.BaseActivity_RFID;
import com.example.crfid.rfid.events.RFIDTagReadEvent;
import com.example.crfid.rfid.events.RFIDTriggerEvent;
import com.example.crfid.viewmodels.ShareTagData_ViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.crfid.R;
import com.zebra.rfid.api3.TagData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MaterialTagUnpair extends BaseActivity_RFID {
    TabLayout tabLayout;
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 100;
    ViewPager2 viewPager2;
    Button unpair;

    String firstTag;
    ShareTagData_ViewModel shareTagDataViewModel;
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

        shareTagDataViewModel = new ViewModelProvider ( this ).get ( ShareTagData_ViewModel.class );
        if ( !isReaderConnected ( ) ) {
//            status.setText ( "Disconnected" );
//            status.setTextColor ( Color.RED );
            //Scanner Initializations
            //Handling Runtime BT permissions for Android 12 and higher


            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ) {
                if ( ContextCompat.checkSelfPermission ( this ,
                        android.Manifest.permission.BLUETOOTH_CONNECT )
                        != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions ( this ,
                            new String[]{android.Manifest.permission.BLUETOOTH_SCAN , Manifest.permission.BLUETOOTH_CONNECT} ,
                            BLUETOOTH_PERMISSION_REQUEST_CODE );
                } else {
                    InitSDK ( );
                }

            } else {
                InitSDK ( );
            }
        }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
//    @Override
    public
    void handleTriggerPress ( RFIDTriggerEvent pressed ) {
        if ( pressed.isPressed () ) {
            this.runOnUiThread ( new Runnable ( ) {
                @Override
                public
                void run () {
//                    status.setText ( "" );
                    shareTagDataViewModel.setLiveTagData ( "" );

                }
            } );
            performInventory ( );
//            changeToSingleScanMode ();


        } else {
            stopInventory ( );
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
//    @Override
    public
    void handleTagdata ( RFIDTagReadEvent tagData2 ) {
        final StringBuilder sb = new StringBuilder ( );
        TagData[] tagData=tagData2.getTagData( );
        for (int index = 0; index < tagData.length; index++) {
            sb.append ( tagData[ index ].getTagID ( ) + "\n" );
        }

        firstTag = tagData[ 0 ].getTagID ( );

        this.runOnUiThread ( new Runnable ( ) {
            @Override
            public
            void run () {
//                tagidTV.append(sb.toString());
                shareTagDataViewModel.setLiveTagData ( firstTag );
//                status.setText ( tagData[ 0 ].getTagID ( ) );
            }
        } );
    }

    @Override
    protected
    void onPause () {
        super.onPause ( );
//        onPause2 ( );
    }

    @Override
    protected
    void onStart () {
        super.onStart ( );
        EventBus.getDefault().register(this);
    }

    @Override
    protected
    void onStop () {
        super.onStop ( );
        EventBus.getDefault().unregister (this);
    }
    @Override
    protected
    void onPostResume () {
        super.onPostResume ( );
//        onResume2 ( );
    }
}