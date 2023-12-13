package com.example.crfid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {


    CardView locateCard,assetInOutCard,matTagPair,cycleCounting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        locateCard=findViewById ( R.id.locateAssets );
        assetInOutCard=findViewById ( R.id.assetInOut );
        cycleCounting =findViewById(R.id.cycleCounting);
        matTagPair=findViewById ( R.id.assetTagPair );
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
    }
}