package com.example.crfid.materialTagPair_Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crfid.Material_Tag_Pair;
import com.example.crfid.R;
import com.example.crfid.common.BasicAuthInterceptor;
import com.example.crfid.common.CommonFunctions;
import com.example.crfid.data.retrofit.ApiService;
import com.example.crfid.model.materialTagPairModel.MaterialTagPair_Item;
import com.example.crfid.model.materialTagPairModel.MaterialTagPair_Response;
import com.example.crfid.viewmodels.MaterialTagPair_ViewModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public
class MaterialTagPair_ItemFrag extends Fragment {

    private Observer<List<String>> dataObserver;
    String werks;
    String mblnr;
    String ebeln;
    String budat_mkpf;
    String matnr;
    String buzei;
    String meins;
    String menge;
    String zdate;
    String time;
    String userid;
    String status;
    String maktx;
    String bstmg;
    String tag_id;
    String ebelp;



    RecyclerView recyclerView;
    ProgressBar progressBar;

    ApiService apiService;
    String username = "ashwin", password = "Crave@2022",
            baseUrl = "https://aincfapim.test.apimanagement.eu10.hana.ondemand.com/ZCIMS_INT_SRV/";

    MaterialTagPairItemAdaptor adapter;



    MaterialTagPair_ViewModel viewModelget, viewmodelset;
    @Override
    public
    View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                        Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_material_tag_pair__item ,
                container ,
                
                false );
    }

    @Override
    public
    void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view ,
                savedInstanceState );
        viewModelget=new ViewModelProvider ( requireActivity () ).get ( MaterialTagPair_ViewModel.class );
        viewmodelset=new ViewModelProvider ( requireActivity () ).get ( MaterialTagPair_ViewModel.class );

        recyclerView=view.findViewById ( R.id.TagPairItemRecy );
        progressBar=view.findViewById ( R.id.progressBarfragItem );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( getActivity () ) );


//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            werks = bundle.getString("werks");
//            mblnr = bundle.getString("mblnr");
//            ebeln = bundle.getString("ebeln");
//            budat_mkpf = bundle.getString("budat_mkpf");
//            matnr = bundle.getString("matnr");
//            buzei = bundle.getString("buzei");
//            meins = bundle.getString("meins");
//            menge = bundle.getString("menge");
//            zdate = bundle.getString("zdate");
//            time = bundle.getString("time");
//            userid = bundle.getString("userid");
//            status = bundle.getString("status");
//            maktx = bundle.getString("maktx");
//            bstmg = bundle.getString("bstmg");
//            tag_id = bundle.getString("tag_id");
//            ebelp = bundle.getString("ebelp");
//        }
//
//        demo=view.findViewById ( R.id.itemfragDemoTv1235 );
//
//        demo.setText ( werks+mblnr+ebeln+budat_mkpf );

        dataObserver = new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> data) {
                // Update UI with the received list of 10 strings
                // Access individual values using list indexes



                ebeln=data.get ( 2 );
                // ... set remaining 9 text views with data ...
            }
        };
        viewModelget.getData().observe(getViewLifecycleOwner(), dataObserver);


        OkHttpClient client = new OkHttpClient.Builder ( ).addInterceptor ( new BasicAuthInterceptor ( username ,
                password ) ).build ( );
        Retrofit retrofit = new Retrofit.Builder ( ).baseUrl ( baseUrl ).client ( client ).addConverterFactory ( GsonConverterFactory.create ( ) ).build ( );
        apiService = retrofit.create ( ApiService .class );
        MatTagItemFragRetrofit ();

    }

    private
    void MatTagItemFragRetrofit () {
        progressBar.setVisibility ( View.VISIBLE );
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

                    progressBar.setVisibility ( View.INVISIBLE );
//                    pleasewait.setVisibility ( View.INVISIBLE );
                    CommonFunctions.showToast (getContext ( ) ,
                            "Error code: " + response.code ( )   );

                    return;
                }

                MaterialTagPair_Response materialTagPairResponse = response.body ( );
                List<com.example.crfid.model.materialTagPairModel.MaterialTagPair_Item> arr = materialTagPairResponse.getD ( ).getResults ( );
//                ArrayList<MaterialTagPair_Item> result = new ArrayList<> ( arr );
                List<MaterialTagPair_Item> filteredWarehouseOrders = new ArrayList<>();


                for (MaterialTagPair_Item ao : arr) {
                    if (ao.getEbeln ().equalsIgnoreCase(ebeln) ) {
                        filteredWarehouseOrders.add(ao);
                    }
                }

                if (filteredWarehouseOrders.isEmpty()) {
                    Toast.makeText(getContext (), "Item not found", Toast.LENGTH_SHORT).show();
                } else {


                    adapter = new MaterialTagPairItemAdaptor(filteredWarehouseOrders);
                    recyclerView.setAdapter(adapter);
                }


//                adapter = new MaterialTagPairItemAdaptor ( result );
//                recyclerView.setAdapter ( adapter );

                progressBar.setVisibility ( View.INVISIBLE );
//                pleasewait.setVisibility ( View.INVISIBLE );
            }

            @Override
            public
            void onFailure ( Call<MaterialTagPair_Response> call , Throwable t ) {

                progressBar.setVisibility ( View.INVISIBLE );
//                pleasewait.setVisibility ( View.INVISIBLE );
                CommonFunctions.showToast ( getContext ( ) ,
                        "Error code: " + t.getMessage ( )  );
            }
        } );
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
            viewModelget.getData().removeObserver(dataObserver);
            viewmodelset.getData().removeObserver(dataObserver);
        }
    }

    @Override
    public
    void onResume () {
        super.onResume ( );
        ArrayList<MaterialTagPair_Item> result = new ArrayList<> ( );
        result.clear ( );
        adapter = new MaterialTagPairItemAdaptor ( result );
        recyclerView.setAdapter ( adapter );
        MatTagItemFragRetrofit ();
    }

    class MaterialTagPairItemAdaptor extends RecyclerView.Adapter<MaterialTagPairItemAdaptor.ViewHolder> {
        private final List<MaterialTagPair_Item> data;

        MaterialTagPairItemAdaptor ( List<MaterialTagPair_Item> data ) {
            this.data = data;
        }

        @NonNull
        @Override
        public
        MaterialTagPairItemAdaptor.ViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
            View view = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.recy_item_for_mat_tag_pair_frag_item ,
                    parent ,
                    false );
            return new MaterialTagPairItemAdaptor.ViewHolder ( view );
        }

        @Override
        public
        void onBindViewHolder ( @NonNull MaterialTagPairItemAdaptor.ViewHolder holder , int position ) {
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

            TextView purchaseOrder, matDocNo,matNo,postingDate;

            ViewHolder ( View itemView ) {
                super ( itemView );
                purchaseOrder=itemView.findViewById ( R.id.purchaseOrderTV );
                matDocNo=itemView.findViewById ( R.id.matDocNoTV );
                matNo=itemView.findViewById ( R.id.matNoTV );
                postingDate=itemView.findViewById ( R.id.postingDateTV );
//
            }

            void bindData ( MaterialTagPair_Item woTask ) {
                purchaseOrder.setText ( woTask.getEbeln ( ) );
                matDocNo.setText ( woTask.getMblnr ( ) );
                matNo.setText ( woTask.getMatnr ( ) );
                postingDate.setText ( CommonFunctions.convertDate ( woTask.getBudatMkpf ( ) )
                         );
                itemView.setOnClickListener ( new View.OnClickListener ( ) {
                    @Override
                    public
                    void onClick ( View view ) {
                        // Get the selected item from the RecyclerView
                        MaterialTagPair_Item selectedWOTask = data.get ( getAdapterPosition ( ) );

                        String werks = selectedWOTask.getWerks ();
                        String mblnr = selectedWOTask.getMblnr ();
                        String ebeln = selectedWOTask.getEbeln ();
                        String budat_mkpf = selectedWOTask.getBudatMkpf ();
                        String matnr = selectedWOTask.getMatnr ();
                        String buzei = selectedWOTask.getBuzei ();
                        String meins = selectedWOTask.getMeins ();
                        String menge = selectedWOTask.getMenge ();
                        String zdate = selectedWOTask.getZdate ();
                        String time = selectedWOTask.getTime ();
                        String userid = selectedWOTask.getUserid ();
                        String status = selectedWOTask.getStatus ();
                        String maktx = selectedWOTask.getMaktx ();
                        String bstmg = selectedWOTask.getBstmg ();
                        String tag_id = selectedWOTask.getTagId ();
                        String ebelp = selectedWOTask.getEbelp ();

                        List<String> data2 = new ArrayList<>();
                        data2.add(werks);
                        data2.add(mblnr);
                        data2.add(ebeln);
                        data2.add(budat_mkpf);
                        data2.add(matnr);
                        data2.add(buzei);
                        data2.add(meins);
                        data2.add(menge);
                        data2.add(zdate);
                        data2.add(time);
                        data2.add(userid);
                        data2.add(status);
                        data2.add(maktx);
                        data2.add(bstmg);
                        data2.add(tag_id);
                        data2.add(ebelp);
                        viewmodelset.setData ( data2 );


                        ViewPager2 viewPager2 = requireActivity().findViewById(R.id.viewPagerTagPAir);
                        viewPager2.setCurrentItem(3, true);

                    }
                } );
            }
        }
    }

}