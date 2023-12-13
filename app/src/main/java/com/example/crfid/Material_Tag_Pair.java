package com.example.crfid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public
class Material_Tag_Pair extends AppCompatActivity {

    Button pair;
    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_material_tag_pair );
        pair = findViewById(R.id.PairButton );
        pair.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
                Intent intent = new Intent(Material_Tag_Pair.this,
                        Material_Tag_Pair_Details_Page.class);
                startActivity ( intent );
            }
        } );
    }
}