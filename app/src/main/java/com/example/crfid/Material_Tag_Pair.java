package com.example.crfid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crfid.common.BasicAuthInterceptor;
import com.example.crfid.data.retrofit.ApiService;
import com.example.crfid.materialTagPair_Fragments.MaterialTagPair_ItemDetail;
import com.example.crfid.materialTagPair_Fragments.adapter.MatTagPair_Frag_Adaptor;
import com.example.crfid.model.materialTagPairModel.MaterialTagPair_Item;
import com.example.crfid.model.materialTagPairModel.MaterialTagPair_Response;
import com.example.crfid.rfid.BaseActivity_RFID;
import com.example.crfid.rfid.events.RFIDTagReadEvent;
import com.example.crfid.rfid.events.RFIDTriggerEvent;
import com.example.crfid.viewmodels.ShareTagData_ViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.crfid.R;
import com.zebra.rfid.api3.TagData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public
class Material_Tag_Pair extends BaseActivity_RFID {

    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 100;
    MaterialTagPair_ItemDetail materialTagPairItemDetail;
    Button pair;

    ApiService apiService;
//    RecyclerView recyclerView;

    MaterialTagPairAdaptor adapter;

//    ProgressBar progressBar;
//    TextView pleasewait;

    String username = "ashwin", password = "Crave@2022",
            baseUrl = "https://aincfapim.test.apimanagement.eu10.hana.ondemand.com/ZCIMS_INT_SRV/";

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MatTagPair_Frag_Adaptor fragadapter;
    TextView status;
    String firstTag = "000000000";
    private
    ShareTagData_ViewModel shareTagDataViewModel;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_material_tag_pair );
        pair = findViewById ( R.id.PairButton );
        onCreate ( this );
//        progressBar = findViewById ( R.id.progressBar );
//        pleasewait = findViewById ( R.id.pleasewaitTV );
//        recyclerView = findViewById ( R.id.recyclerv );
//        GridLayoutManager gridLayoutManager = new GridLayoutManager ( this ,
//                2 );
//        recyclerView.setLayoutManager ( new LinearLayoutManager ( this ) );
//      recyclerView.setLayoutManager(gridLayoutManager);

        shareTagDataViewModel = new ViewModelProvider ( this ).get ( ShareTagData_ViewModel.class );

        tabLayout = findViewById ( R.id.tabLayout );
        viewPager2 = findViewById ( R.id.viewPagerTagPAir );
        fragadapter = new MatTagPair_Frag_Adaptor ( this );
        viewPager2.setAdapter ( fragadapter );
        viewPager2.setUserInputEnabled ( false );
        status = findViewById ( R.id.statusTextViewRFID );

        materialTagPairItemDetail = new MaterialTagPair_ItemDetail ( );

        if ( !isReaderConnected ( ) ) {
//            status.setText ( "Disconnected" );
//            status.setTextColor ( Color.RED );
            //Scanner Initializations
            //Handling Runtime BT permissions for Android 12 and higher


            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ) {
                if ( ContextCompat.checkSelfPermission ( this ,
                        Manifest.permission.BLUETOOTH_CONNECT )
                        != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions ( this ,
                            new String[]{Manifest.permission.BLUETOOTH_SCAN , Manifest.permission.BLUETOOTH_CONNECT} ,
                            BLUETOOTH_PERMISSION_REQUEST_CODE );
                } else {
                    InitSDK ( );
                }

            } else {
                InitSDK ( );
            }
        }


        for (int i = 0; i < tabLayout.getTabCount ( ); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt ( i );
            if ( tab != null ) {
                tab.view.setClickable ( false ); // Disable click
                tab.view.setFocusable ( false ); // Disable focus
                // You might also want to change other properties as needed, like background, etc.
            }
        }
        new TabLayoutMediator ( tabLayout ,
                viewPager2 ,
                ( tab , position ) ->
//                        tab.setText("Tab " + (position + 1))
                {

                    switch (position) {
                        case 0:
                            tab.setText ( "List" );
                            break;
                        case 1:
                            tab.setText ( "Item-List" );
                            break;
                        case 2:
                            tab.setText ( "Item-Details" );
                            break;
                        // Add more cases if you have more tabs
                    }
                }
        ).attach ( );


        OkHttpClient client = new OkHttpClient.Builder ( ).addInterceptor ( new BasicAuthInterceptor ( username ,
                password ) ).build ( );
        Retrofit retrofit = new Retrofit.Builder ( ).baseUrl ( baseUrl ).client ( client ).addConverterFactory ( GsonConverterFactory.create ( ) ).build ( );
        apiService = retrofit.create ( ApiService.class );


//        MatTagRetrofit ( );


//        pair.setOnClickListener ( new View.OnClickListener ( ) {
//            @Override
//            public
//            void onClick ( View view ) {
//                Intent intent = new Intent ( Material_Tag_Pair.this ,
//                        Material_Tag_Pair_Details_Page.class );
//                startActivity ( intent );
//            }
//        } );

        tabLayout.setOnTabSelectedListener ( new TabLayout.OnTabSelectedListener ( ) {
            @Override
            public
            void onTabSelected ( TabLayout.Tab tab ) {
//                viewPager2.setCurrentItem ( tab.getPosition () );
                int selectedTabPosition = tab.getPosition ( );

                // Enable the "Pair" button only on the third tab (position 2)
                if ( selectedTabPosition == 2 ) {
                    pair.setVisibility ( View.VISIBLE );
                } else {
                    pair.setVisibility ( View.GONE );
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

//        viewPager2.registerOnPageChangeCallback ( new ViewPager2.OnPageChangeCallback ( ) {
//            @Override
//            public
//            void onPageScrolled ( int position , float positionOffset , int positionOffsetPixels ) {
//                super.onPageScrolled ( position ,
//                        positionOffset ,
//                        positionOffsetPixels );
//                tabLayout.getTabAt ( position ).select ();
//            }
//        } );
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
                    status.setText ( "" );
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
                status.setText ( tagData[ 0 ].getTagID ( ) );
            }
        } );
    }

    @Override
    protected
    void onResume () {
        super.onResume ( );
        ArrayList<MaterialTagPair_Item> result = new ArrayList<> ( );
        result.clear ( );
        adapter = new MaterialTagPairAdaptor ( result );
//        recyclerView.setAdapter ( adapter );
//        MatTagRetrofit ( );
    }

    private
    void MatTagRetrofit () {
//        progressBar.setVisibility ( View.VISIBLE );
//        pleasewait.setVisibility ( View.VISIBLE );
        String filter = "budat_mkpf ge datetime'2023-02-01T00:00:00' and budat_mkpf le datetime'2023-10-02T00:00:00'";

        Call<MaterialTagPair_Response> call = apiService.getMaterialTagPair ( filter ,
                "application/json" ,
                "application/json" ,
                "Fetch" );
        call.enqueue ( new Callback<MaterialTagPair_Response> ( ) {
            @Override
            public
            void onResponse ( Call<MaterialTagPair_Response> call , Response<MaterialTagPair_Response> response ) {

                if ( !response.isSuccessful ( ) ) {

//                    progressBar.setVisibility ( View.INVISIBLE );
//                    pleasewait.setVisibility ( View.INVISIBLE );
                    Toast.makeText ( getApplicationContext ( ) ,
                            "Error code: " + response.code ( ) ,
                            Toast.LENGTH_SHORT ).show ( );
                    return;
                }

                MaterialTagPair_Response materialTagPairResponse = response.body ( );
                List<MaterialTagPair_Item> arr = materialTagPairResponse.getD ( ).getResults ( );
                ArrayList<MaterialTagPair_Item> result = new ArrayList<> ( arr );
                adapter = new MaterialTagPairAdaptor ( result );
//                recyclerView.setAdapter ( adapter );
//
//                progressBar.setVisibility ( View.INVISIBLE );
//                pleasewait.setVisibility ( View.INVISIBLE );
            }

            @Override
            public
            void onFailure ( Call<MaterialTagPair_Response> call , Throwable t ) {

//                progressBar.setVisibility ( View.INVISIBLE );
//                pleasewait.setVisibility ( View.INVISIBLE );
                Toast.makeText ( getApplicationContext ( ) ,
                        "Error code: " + t.getMessage ( ) ,
                        Toast.LENGTH_SHORT ).show ( );
            }
        } );
    }

    @Override
    protected
    void onDestroy () {
        super.onDestroy ( );
//        onDestroy2 ( );
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

    public
    interface MatTagPairDataShare {

        void handleTriggerPress ( boolean pressed );

        void handleTagdata ( TagData[] tagData );
    }

    class MaterialTagPairAdaptor extends RecyclerView.Adapter<MaterialTagPairAdaptor.ViewHolder> {
        private final List<MaterialTagPair_Item> data;

        MaterialTagPairAdaptor ( List<MaterialTagPair_Item> data ) {
            this.data = data;
        }

        @NonNull
        @Override
        public
        MaterialTagPairAdaptor.ViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
            View view = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.common_recycler_locate_tag_pair_unpair ,
                    parent ,
                    false );
            return new MaterialTagPairAdaptor.ViewHolder ( view );
        }

        @Override
        public
        void onBindViewHolder ( @NonNull MaterialTagPairAdaptor.ViewHolder holder , int position ) {
            MaterialTagPair_Item woTask = data.get ( position );
            holder.bindData ( woTask );
        }

        @Override
        public
        int getItemCount () {
            return data.size ( );
//            return Math.min(data.size(), 5);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            //            private ImageView icon;
            private final TextView assetID;
            private final TextView maktx;
            private final TextView currentDate;
            private final TextView lastDate;

            ViewHolder ( View itemView ) {
                super ( itemView );
                assetID = itemView.findViewById ( R.id.assetID );
                maktx = itemView.findViewById ( R.id.maktx );
                currentDate = itemView.findViewById ( R.id.dateTimeCurrent );
                lastDate = itemView.findViewById ( R.id.lastDate );
            }

            void bindData ( MaterialTagPair_Item woTask ) {
                assetID.setText ( woTask.getUserid ( ) );
                maktx.setText ( woTask.getMaktx ( ) );
                currentDate.setText ( woTask.getTime ( ) );
                lastDate.setText ( woTask.getBudatMkpf ( ) );
                itemView.setOnClickListener ( new View.OnClickListener ( ) {
                                                  @Override
                                                  public
                                                  void onClick ( View view ) {
                                                      // Get the selected item from the RecyclerView
                                                      MaterialTagPair_Item selectedWOTask = data.get ( getAdapterPosition ( ) );

                                                      String werks = selectedWOTask.getWerks ( );
                                                      String mblnr = selectedWOTask.getMblnr ( );
                                                      String ebeln = selectedWOTask.getEbeln ( );
                                                      String budat_mkpf = selectedWOTask.getBudatMkpf ( );
                                                      String matnr = selectedWOTask.getMatnr ( );
                                                      String buzei = selectedWOTask.getBuzei ( );
                                                      String meins = selectedWOTask.getMeins ( );
                                                      String menge = selectedWOTask.getMenge ( );
                                                      String zdate = selectedWOTask.getZdate ( );
                                                      String time = selectedWOTask.getTime ( );
                                                      String userid = selectedWOTask.getUserid ( );
                                                      String status = selectedWOTask.getStatus ( );
                                                      String maktx = selectedWOTask.getMaktx ( );
                                                      String bstmg = selectedWOTask.getBstmg ( );
                                                      String tag_id = selectedWOTask.getTagId ( );
                                                      String ebelp = selectedWOTask.getEbelp ( );

                                                      Intent intent = new Intent ( Material_Tag_Pair.this ,
                                                              Material_Tag_Pair_Details_Page.class );

                                                      intent.putExtra ( "werks" ,
                                                              werks );
                                                      intent.putExtra ( "mblnr" ,
                                                              mblnr );
                                                      intent.putExtra ( "ebeln" ,
                                                              ebeln );
                                                      intent.putExtra ( "budat_mkpf" ,
                                                              budat_mkpf );
                                                      intent.putExtra ( "matnr" ,
                                                              matnr );
                                                      intent.putExtra ( "buzei" ,
                                                              buzei );
                                                      intent.putExtra ( "meins" ,
                                                              meins );
                                                      intent.putExtra ( "menge" ,
                                                              menge );
                                                      intent.putExtra ( "zdate" ,
                                                              zdate );
                                                      intent.putExtra ( "time" ,
                                                              time );
                                                      intent.putExtra ( "userid" ,
                                                              userid );
                                                      intent.putExtra ( "status" ,
                                                              status );
                                                      intent.putExtra ( "maktx" ,
                                                              maktx );
                                                      intent.putExtra ( "bstmg" ,
                                                              bstmg );
                                                      intent.putExtra ( "tag_id" ,
                                                              tag_id );
                                                      intent.putExtra ( "ebelp" ,
                                                              ebelp );


                                                      startActivity ( intent );
//                                                    finish();
                                                  }
                                              }
                );
            }
        }
    }
}