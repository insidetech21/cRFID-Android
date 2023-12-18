package com.example.crfid.materialTagPair_Fragments.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.crfid.materialTagPair_Fragments.MaterialTagPair_ItemFrag;
import com.example.crfid.materialTagPair_Fragments.MaterialTagPair_ItemDetail;
import com.example.crfid.materialTagPair_Fragments.MaterialTagPair_Lists;

public
class MatTagPair_Frag_Adaptor extends FragmentStateAdapter {
    public
    MatTagPair_Frag_Adaptor ( @NonNull FragmentActivity fragmentActivity ) {
        super ( fragmentActivity );
    }

    @NonNull
    @Override
    public
    Fragment createFragment ( int position ) {
        switch ( position ) {
            case 0 : return new MaterialTagPair_Lists ();
            case 1 : return new MaterialTagPair_ItemFrag ();
            case 2 : return new MaterialTagPair_ItemDetail ();
            default : return new MaterialTagPair_Lists ();
        }
    }

    @Override
    public
    int getItemCount () {
        return 3;
    }
}
