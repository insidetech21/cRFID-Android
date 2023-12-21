package com.example.crfid.materialTagUnpair_Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.crfid.R;
import com.example.crfid.common.BasicAuthInterceptor;
import com.example.crfid.common.CommonFunctions;
import com.example.crfid.data.retrofit.ApiService;
import com.example.crfid.materialTagPair_Fragments.MaterialTagPair_Lists;
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
class MaterialTagUnpair_Lists extends Fragment {


    RecyclerView recyclerView;
    ApiService apiService;
    String username = "ashwin", password = "Crave@2022",
            baseUrl = "https://aincfapim.test.apimanagement.eu10.hana.ondemand.com/ZCIMS_INT_SRV/";
    MaterialTagUnPairListAdaptor adapter;

    ProgressBar progressBar;

    MaterialTagPair_ViewModel viewModel;

    @Override
    public
    View onCreateView ( LayoutInflater inflater , ViewGroup container ,
                        Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_material_tag_unpair__lists ,
                container ,
                false );
    }

    @Override
    public
    void onViewCreated ( @NonNull View view , @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view ,
                savedInstanceState );
        recyclerView=view.findViewById ( R.id.TagUnpairListRecy );
        recyclerView.setLayoutManager ( new LinearLayoutManager ( getActivity () ) );
        progressBar=view.findViewById ( R.id.progressListUnpair );
        viewModel =  new ViewModelProvider (requireActivity ()).get(MaterialTagPair_ViewModel.class);
        OkHttpClient client = new OkHttpClient.Builder ( ).addInterceptor ( new BasicAuthInterceptor ( username ,
                password ) ).build ( );
        Retrofit retrofit = new Retrofit.Builder ( ).baseUrl ( baseUrl ).client ( client ).addConverterFactory ( GsonConverterFactory.create ( ) ).build ( );
        apiService = retrofit.create ( ApiService.class );
        MatTagRetrofit ();
    }
    private
    void MatTagRetrofit () {
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

                    CommonFunctions.showToast ( getContext (),"Error code: " + response.code ( ) );
                    return;
                }

                MaterialTagPair_Response materialTagPairResponse = response.body ( );
                List<MaterialTagPair_Item> arr = materialTagPairResponse.getD ( ).getResults ( );
                ArrayList<MaterialTagPair_Item> result = new ArrayList<> ( arr );
                adapter = new MaterialTagUnPairListAdaptor ( result );
                recyclerView.setAdapter ( adapter );

                progressBar.setVisibility ( View.INVISIBLE );
//                pleasewait.setVisibility ( View.INVISIBLE );
            }

            @Override
            public
            void onFailure ( Call<MaterialTagPair_Response> call , Throwable t ) {

                progressBar.setVisibility ( View.INVISIBLE );
//                pleasewait.setVisibility ( View.INVISIBLE );
                CommonFunctions.showToast ( getContext ( ) ,
                        "Error code: " + t.getMessage ( ) );
            }
        } );
    }

    @Override
    public
    void onResume () {
        super.onResume ( );
        ArrayList<MaterialTagPair_Item> result = new ArrayList<> ( );
        result.clear ( );
        adapter = new MaterialTagUnPairListAdaptor ( result );
        recyclerView.setAdapter ( adapter );
        MatTagRetrofit ();
    }
    class MaterialTagUnPairListAdaptor extends RecyclerView.Adapter<MaterialTagUnPairListAdaptor.ViewHolder> {
        private final List<MaterialTagPair_Item> data;

        MaterialTagUnPairListAdaptor ( List<MaterialTagPair_Item> data ) {
            this.data = data;
        }

        @NonNull
        @Override
        public
        MaterialTagUnPairListAdaptor.ViewHolder onCreateViewHolder ( @NonNull ViewGroup parent , int viewType ) {
            View view = LayoutInflater.from ( parent.getContext ( ) ).inflate ( R.layout.common_recycler_locate_tag_pair_unpair ,
                    parent ,
                    false );
            return new MaterialTagUnPairListAdaptor.ViewHolder ( view );
        }

        @Override
        public
        void onBindViewHolder ( @NonNull MaterialTagUnPairListAdaptor.ViewHolder holder , int position ) {
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
                currentDate.setText (CommonFunctions.convertDuration ( woTask.getTime ( ) )
                );
                lastDate.setText (CommonFunctions.convertDate (  woTask.getBudatMkpf ( )) );
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

                        List<String> data = new ArrayList<>();
                        data.add(werks);
                        data.add(mblnr);
                        data.add(ebeln);
                        data.add(budat_mkpf);
                        data.add(matnr);
                        data.add(buzei);
                        data.add(meins);
                        data.add(menge);
                        data.add(zdate);
                        data.add(time);
                        data.add(userid);
                        data.add(status);
                        data.add(maktx);
                        data.add(bstmg);
                        data.add(tag_id);
                        data.add(ebelp);

                        viewModel.setData ( data );

                        ViewPager2 viewPager2 = requireActivity().findViewById(R.id.viewpagerTagUnpair);
                        viewPager2.setCurrentItem(1, true);

                    }
                } );
            }
        }
    }

}