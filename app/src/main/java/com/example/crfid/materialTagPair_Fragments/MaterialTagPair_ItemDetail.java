package com.example.crfid.materialTagPair_Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crfid.Material_Tag_Pair;
import com.example.crfid.Material_Tag_Pair_Details_Page;
import com.example.crfid.R;
import com.example.crfid.common.BasicAuthInterceptor;
import com.example.crfid.data.retrofit.ApiService;
import com.example.crfid.model.materialTagPairModel.MaterialTagPair_Item;
import com.example.crfid.model.materialTagPairModel.MaterialTagPair_Response;
import com.example.crfid.viewmodels.MaterialTagPair_ViewModel;

import java.util.List;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public
class MaterialTagPair_ItemDetail extends Fragment {

    TextView plantTV,matDovTV,poNoTV,matnrTV,unitTV,quantityTV,useridTV,matdescpTV,tagidTV;
    private Observer<List<String>> dataObserver;
    String werks = "demo";
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
    MaterialTagPair_ViewModel viewModeldetget,viewModedetset;

    Button pairB;

    ProgressBar progressBar;
    TextView pleasewait;

    String username = "ashwin", password = "Crave@2022",
            baseUrl = "https://aincfapim.test.apimanagement.eu10.hana.ondemand.com/ZCIMS_INT_SRV/";
    ApiService apiService;

    String xcsrftoken="";


    String cookies="";

    @Override
    public
    View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                        Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_material_tag_pair__item_detail ,
                container ,
                false );
    }

    @Override
    public
    void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view ,
                savedInstanceState );
        viewModeldetget=new ViewModelProvider ( requireActivity () ).get ( MaterialTagPair_ViewModel.class );

        pairB=(Button)((Material_Tag_Pair) getActivity ()).findViewById ( R.id.PairButton );


        plantTV=view.findViewById( R.id.plantidTV);
        matDovTV=view.findViewById( R.id.matDocidTV);
        poNoTV=view.findViewById( R.id.poNoidTV);
        matnrTV=view.findViewById( R.id.matNridTV);
        unitTV=view.findViewById( R.id.unitidTV);
        quantityTV=view.findViewById( R.id.quantityidTV);
        useridTV=view.findViewById( R.id.userid_IDTV);
        matdescpTV=view.findViewById( R.id.matdescpidTV);
        tagidTV=view.findViewById( R.id.tagid_IDTV);


        progressBar=view.findViewById( R.id.progress_circulardetail);
        pleasewait=view.findViewById( R.id.pleasewait4564csd);
        progressBar.setVisibility(View.INVISIBLE);
        pleasewait.setVisibility(View.INVISIBLE);




        dataObserver = new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> data) {
                // Update UI with the received list of 10 strings
                // Access individual values using list indexes


                werks = data.get( 0 );
                mblnr = data.get( 1 );
                ebeln = data.get(2);
                budat_mkpf = data.get(3);
                matnr = data.get(4);
                buzei = data.get(5);
                meins = data.get(6);
                menge = data.get(7);
                zdate = data.get(8);
                time = data.get(9);
                userid = data.get(10);
                status = data.get(11);
                maktx = data.get(12);
                bstmg = data.get(13);
                tag_id = data.get(14);
                ebelp = data.get(15);
                // ... set remaining 9 text views with data ...

                plantTV.setText ( werks );
                matDovTV.setText ( mblnr );
                poNoTV.setText ( ebeln );
                matnrTV.setText ( matnr );
                unitTV.setText ( meins );
                quantityTV.setText ( menge );
                useridTV.setText ( userid );
                matdescpTV.setText ( maktx );
                tagidTV.setText ( tag_id );
            }
        };
        viewModeldetget.getData().observe(getViewLifecycleOwner(), dataObserver);

        pairB.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public
            void onClick ( View view ) {

                MatTagDetailFetchRetrofit();

//                Toast.makeText ( getContext ( ) ,
//                        "pair button clicked ",
//                        Toast.LENGTH_SHORT ).show ( );
            }
        } );

        OkHttpClient client = new OkHttpClient.Builder ( ).addInterceptor ( new BasicAuthInterceptor ( username ,
                password ) ).build ( );
        Retrofit retrofit = new Retrofit.Builder ( ).baseUrl ( baseUrl ).client ( client ).addConverterFactory ( GsonConverterFactory.create ( ) ).build ( );
        apiService = retrofit.create ( ApiService.class );



    }

    @Override
    public
    void onStart () {
        super.onStart ( );
    }

    @Override
    public
    void onStop () {
        super.onStop ( );
        if (dataObserver != null) {
            viewModeldetget.getData().removeObserver(dataObserver);
        }
    }


    private
    void MatTagDetailFetchRetrofit () {
        progressBar.setVisibility(View.VISIBLE);
        pleasewait.setVisibility(View.VISIBLE);

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


                    progressBar.setVisibility(View.INVISIBLE);
                    pleasewait.setVisibility(View.INVISIBLE);
                    Toast.makeText ( getContext ( ) ,
                            "Error code: " + response.code ( ) ,
                            Toast.LENGTH_SHORT ).show ( );
                    return;
                }

                Headers headers = response.headers();
                xcsrftoken=response.headers ().get ( "x-csrf-token" );
                cookies=response.headers ().get ( "set-cookie" );

                MaterialTagPair_Item materialTagPairItem=new MaterialTagPair_Item();

                materialTagPairItem.setWerks ( werks );
                materialTagPairItem.setMblnr ( mblnr );
                materialTagPairItem.setEbeln ( ebeln );
                materialTagPairItem.setBudatMkpf ( budat_mkpf );
                materialTagPairItem.setMatnr ( matnr );
                materialTagPairItem.setBuzei ( buzei );
                materialTagPairItem.setMeins ( meins );
                materialTagPairItem.setMenge ( menge );
                materialTagPairItem.setZdate ( zdate );
                materialTagPairItem.setTime ( time );
                materialTagPairItem.setUserid ( userid );
                materialTagPairItem.setStatus ( status );
                materialTagPairItem.setMaktx ( maktx );
                materialTagPairItem.setBstmg ( bstmg );
                materialTagPairItem.setTagId ( tag_id );
                materialTagPairItem.setEbelp ( ebelp );

//                MaterialTagPair_Response materialTagPairResponse = response.body ( );
//                List<MaterialTagPair_Item> arr = materialTagPairResponse.getD ( ).getResults ( );
//                ArrayList<MaterialTagPair_Item> result = new ArrayList<> ( arr );

                postMaterialTagPair ( xcsrftoken,cookies, materialTagPairItem);

            }


            @Override
            public
            void onFailure ( Call<MaterialTagPair_Response> call , Throwable t ) {


                progressBar.setVisibility(View.INVISIBLE);
                pleasewait.setVisibility(View.INVISIBLE);
                Toast.makeText ( getContext ( ) ,
                        "Error code: " + t.getMessage ( ) ,
                        Toast.LENGTH_SHORT ).show ( );
            }
        } );
    }

    private void postMaterialTagPair(
            String token, String cookie, MaterialTagPair_Item pick
    ){
        String finalCookie = cookie.split(";")[0];
        Call<MaterialTagPair_Item> call=apiService.postMatTagPair (
                pick,
                "application/json",
                "application/json",
                token,
                finalCookie
        );
        call.enqueue ( new Callback<MaterialTagPair_Item> ( ) {
            @Override
            public
            void onResponse ( Call<MaterialTagPair_Item> call , Response<MaterialTagPair_Item> response ) {

                if(!response.isSuccessful ()){
                    progressBar.setVisibility(View.INVISIBLE);
                    pleasewait.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext (), "Error in posting: " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.INVISIBLE);
                pleasewait.setVisibility(View.INVISIBLE);

                AlertDialog.Builder builder = new AlertDialog.Builder( getActivity ());
                builder.setTitle("Data Posted!!");
                builder.setMessage("Data posted successfully!!");

                // Add an "OK" button to dismiss the dialog
                builder.setPositiveButton("OK", (dialog, which) -> {


                    // Dismiss the dialog
                    getActivity ().finish();
                });
                // Create and show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            @Override
            public
            void onFailure ( Call<MaterialTagPair_Item> call , Throwable t ) {
                progressBar.setVisibility(View.INVISIBLE);
                pleasewait.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext (), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } );
    }
}