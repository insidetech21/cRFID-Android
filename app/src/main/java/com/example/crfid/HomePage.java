package com.example.crfid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.crfid.rfid.RFIDHandler;
import com.example.crfid.rfid.interfaces_RFID.RFID_Context;
import com.example.user.crfid.R;
import com.zebra.rfid.api3.TagData;

public class HomePage extends AppCompatActivity implements RFIDHandler.ResponseHandlerInterface, RFID_Context {


    RFIDHandler rfidHandler;
    CardView readMaterial,locateCard,assetInOutCard,matTagPair,matTagUnPair,cycleCounting;
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 100;
    TextView status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        readMaterial=findViewById(R.id.readAssets);
        locateCard=findViewById ( R.id.locateAssets );
        assetInOutCard=findViewById ( R.id.assetInOut );
        cycleCounting =findViewById(R.id.cycleCounting);
        matTagPair=findViewById ( R.id.assetTagPair );
        matTagUnPair=findViewById(R.id.assetTagUnpair);
        status=findViewById ( R.id.rfidStatus );

        // RFID Handler
        rfidHandler = new RFIDHandler();

        if(!rfidHandler.isReaderConnected ()){

            //Scanner Initializations
            //Handling Runtime BT permissions for Android 12 and higher

            if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                if ( ContextCompat.checkSelfPermission(this,
                        Manifest.permission.BLUETOOTH_CONNECT)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT},
                            BLUETOOTH_PERMISSION_REQUEST_CODE);
                }else{
                    rfidHandler.onCreate( this );
                }
            }else{
                rfidHandler.onCreate(this);
            }
        }

        readMaterial.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent=new Intent( HomePage.this, ReadMaterial.class );
                startActivity(intent);
            }
        } );
        locateCard.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent=new Intent( HomePage.this, Locate_Asset.class );
                startActivity(intent);
            }
        } );

        assetInOutCard.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent=new Intent( HomePage.this, Material_In_Out.class );
                startActivity(intent);
            }
        } );

        cycleCounting.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent=new Intent( HomePage.this, CycleCounting.class );
                startActivity(intent);
            }
        } );

        matTagPair.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent=new Intent( HomePage.this,Material_Tag_Pair.class);
                startActivity(intent);
            }
        } );

        matTagUnPair.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent=new Intent( HomePage.this,MaterialTagUnpair.class);
                startActivity(intent);
            }
        } );
    }

    @Override
    public
    void onPause () {
        super.onPause ( );
//        rfidHandler.onPause ();
//        rfidHandler.stopInventory ();

    }
//
    @Override
    public
    void onDestroy () {
        super.onDestroy ( );
//        rfidHandler.onDestroy ();
//        rfidHandler.stopInventory ();
    }
//
    @Override
    public
    void onResume () {
        super.onResume ( );
        rfidHandler.onResume ();
    }

    @Override
    public
    void handleTagdata ( TagData[] tagData ) {

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

    @Override
    public
    Context getContext2 () {
        return this;
    }
    @Override
    public void runOnUiThread2 ( Runnable action ) {
        this.runOnUiThread ( action );
    }

    @Override
    public
    void handleTriggerPress ( boolean pressed ) {
        if ( pressed ) {
            this.runOnUiThread ( new Runnable ( ) {
                @Override
                public
                void run () {
//                    tagidTV.setText ( "" );
                }
            } );
            rfidHandler.performInventory ( );
        } else
            rfidHandler.stopInventory ( );
    }

}