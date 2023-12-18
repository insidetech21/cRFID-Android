package com.example.crfid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crfid.common.BasicAuthInterceptor;
import com.example.crfid.data.retrofit.ApiService;
import com.example.crfid.model.materialTagPairModel.MaterialTagPair_Item;
import com.example.crfid.model.materialTagPairModel.MaterialTagPair_Response;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public
class Material_Tag_Pair_Details_Page extends AppCompatActivity {
//Mohnish

    String werks = "";
    String mblnr = "";
    String ebeln = "";
    String budat_mkpf = "";
    String matnr = "";
    String buzei = "";
    String meins = "";
    String menge = "";
    String zdate = "";
    String time = "";
    String userid = "";
    String status = "";
    String maktx = "";
    String bstmg = "";
    String tag_id = "";
    String ebelp = "";

    TextView planttv,matdocNotv,poNotv,matnumtv,unittv,quantitytv,useridtv,matdescptv,tagidtv;

    String username = "ashwin", password = "Crave@2022",
            baseUrl = "https://aincfapim.test.apimanagement.eu10.hana.ondemand.com/ZCIMS_INT_SRV/";
    ApiService apiService;

    String xcsrftoken="";


    String cookies="";

    Button pairButton;

    ProgressBar progressBar;
    TextView pleasewait;
    @Override
    protected
    void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_material_tag_pair_details_page );

        OkHttpClient client = new OkHttpClient.Builder ( ).addInterceptor ( new BasicAuthInterceptor ( username ,
                password ) ).build ( );
        Retrofit retrofit = new Retrofit.Builder ( ).baseUrl ( baseUrl ).client ( client ).addConverterFactory ( GsonConverterFactory.create ( ) ).build ( );
        apiService = retrofit.create ( ApiService.class );
        pairButton=findViewById ( R.id.PairButtonDetails);
        progressBar=findViewById ( R.id.detailProgressBar );
        pleasewait=findViewById ( R.id.pwairTV );

        progressBar.setVisibility(View.INVISIBLE);
        pleasewait.setVisibility(View.INVISIBLE);

        werks = getIntent().getStringExtra("werks");
        mblnr = getIntent().getStringExtra("mblnr");
        ebeln = getIntent().getStringExtra("ebeln");
        budat_mkpf = getIntent().getStringExtra("budat_mkpf");
        matnr = getIntent().getStringExtra("matnr");
        buzei = getIntent().getStringExtra("buzei");
        meins = getIntent().getStringExtra("meins");
        menge = getIntent().getStringExtra("menge");
        zdate = getIntent().getStringExtra("zdate");
        time = getIntent().getStringExtra("time");
        userid = getIntent().getStringExtra("userid");
        status = getIntent().getStringExtra("status");
        maktx = getIntent().getStringExtra("maktx");
        bstmg = getIntent().getStringExtra("bstmg");
        tag_id = getIntent().getStringExtra("tag_id");
        ebelp = getIntent().getStringExtra("ebelp");

        planttv=findViewById ( R.id.plantTV );
        matdocNotv=findViewById ( R.id.matDocTV);
        poNotv=findViewById ( R.id.poNoTV);
        matnumtv=findViewById ( R.id.matNrTV);
        unittv=findViewById ( R.id.unitTV);
        quantitytv=findViewById ( R.id.quantityTV);
        useridtv=findViewById ( R.id.useridTV);
        matdescptv=findViewById ( R.id.matdescpTV);
        tagidtv=findViewById ( R.id.tagidTV);

        planttv.setText ( werks );
        matdocNotv.setText (mblnr);
        poNotv.setText ( ebeln );
        matnumtv.setText ( matnr );
        unittv.setText(meins);
        quantitytv.setText (menge);
        useridtv.setText ( userid );
        matdescptv.setText ( maktx );
        tagidtv.setText ( tag_id );

        pairButton.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {
//                MatTagDetailFetchRetrofit ();
            }
        } );
    }


}