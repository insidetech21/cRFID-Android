package com.example.crfid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crfid.rfid.BaseActivity_RFID;
import com.example.crfid.rfid.RFIDHandler;
import com.example.crfid.rfid.events.ConnectionStatusEvent;
import com.example.crfid.rfid.events.RFIDTagReadEvent;
import com.example.crfid.rfid.events.RFIDTriggerEvent;
import com.example.crfid.rfid.interfaces_RFID.RFID_Context;
import com.example.crfid.R;
import com.example.crfid.viewmodels.ConnectionStatus_ViewModel;
import com.example.crfid.viewmodels.ShareTagData_ViewModel;
import com.zebra.rfid.api3.TagData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public
class HomePage extends BaseActivity_RFID {


    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 100;
    ConnectionStatus_ViewModel connectionStatusViewModel;
    //    RFIDHandler rfidHandler;
    CardView readMaterial, locateCard,
//            assetInOutCard,
            matTagPair, matTagUnPair, cycleCounting;
    TextView status;

    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_home_page );

        onCreate ( this );
        readMaterial = findViewById ( R.id.readAssets );
        locateCard = findViewById ( R.id.locateAssets );
//        assetInOutCard = findViewById ( R.id.assetInOut );
        cycleCounting = findViewById ( R.id.cycleCounting );
        matTagPair = findViewById ( R.id.assetTagPair );
        matTagUnPair = findViewById ( R.id.assetTagUnpair );
        status = findViewById ( R.id.rfidStatus );

        // RFID Handler
//        rfidHandler = new RFIDHandler();

        connectionStatusViewModel = new ViewModelProvider ( this ).get ( ConnectionStatus_ViewModel.class );

        connectionStatusViewModel.getconnectStatus ( ).observe ( this ,
                new Observer<String> ( ) {
                    @Override
                    public
                    void onChanged ( String s ) {
                        status.setText ( s );
//                status.setTextColor ( Color.GREEN );
                    }
                } );


        if ( !isReaderConnected ( ) ) {

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

        readMaterial.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent = new Intent ( HomePage.this ,
                        ReadMaterial.class );
                startActivity ( intent );
            }
        } );
        locateCard.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent = new Intent ( HomePage.this ,
                        Locate_Asset.class );
                startActivity ( intent );
            }
        } );

//        assetInOutCard.setOnClickListener ( new View.OnClickListener ( ) {
//            @Override
//            public
//            void onClick ( View view ) {
//                Intent intent = new Intent ( HomePage.this ,
//                        Material_In_Out.class );
//                startActivity ( intent );
//            }
//        } );

        cycleCounting.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent = new Intent ( HomePage.this ,
                        CycleCounting.class );
                startActivity ( intent );
            }
        } );

        matTagPair.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent = new Intent ( HomePage.this ,
                        Material_Tag_Pair.class );
                startActivity ( intent );
            }
        } );

        matTagUnPair.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent = new Intent ( HomePage.this ,
                        MaterialTagUnpair.class );
                startActivity ( intent );
            }
        } );
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
    public
    void onPause () {
        super.onPause ( );

//        onPause2 ( );
    }

    @Override
    public
    void onDestroy () {
        super.onDestroy ( );
//        onDestroy2 ( );
    }

    @Override
    public
    void onResume () {
        super.onResume ( );
    }

    @Override
    protected
    void onPostResume () {
        super.onPostResume ( );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
//    @Override
    public
    void handleTagdata ( RFIDTagReadEvent tagData ) {

//        final StringBuilder sb = new StringBuilder();
//        for (int index = 0; index < tagData.length; index++) {
//            sb.append(tagData[index].getTagID() + "\n");
//        }
//
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
////                tagidTV.append(sb.toString());
////                tagidTV.setText(tagData[0].getTagID () );
//            }
//        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void connectionStatus( ConnectionStatusEvent connectionStatusEvent ){
        if( connectionStatusEvent.getStatus ()){
            status.setText ( "Connected" );
            status.setTextColor ( Color.GREEN );
        }
        else{
            status.setText("Disconnected");
            status.setTextColor ( Color.RED );
        }
    }

//    @Override
//    public
//    Context getContext2 () {
//        return this;
//    }
//    @Override
//    public void runOnUiThread2 ( Runnable action ) {
//        this.runOnUiThread ( action );
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleTriggerPress ( RFIDTriggerEvent pressed ) {
        if ( pressed.isPressed () ) {
            this.runOnUiThread ( new Runnable ( ) {
                @Override
                public
                void run () {
//                    tagidTV.setText ( "" );
                    Toast.makeText ( getApplicationContext ( ) ,
                            "Trigger Pressed  !!" ,
                            Toast.LENGTH_SHORT ).show ( );
                }
            } );
            performInventory ( );
        } else {
            stopInventory ( );
        }
    }

}