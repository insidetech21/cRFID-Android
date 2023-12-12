package com.example.crfid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {


    CardView locateCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        locateCard=findViewById ( R.id.locateAssets );
        locateCard.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent=new Intent( HomePage.this, Locate_Asset.class );
                startActivity(intent);
            }
        } );
    }
}