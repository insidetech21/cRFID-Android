package com.example.crfid.materialTagUnpair_Fragments.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.crfid.materialTagUnpair_Fragments.MaterialTagUnpair_ItemDetail;
import com.example.crfid.materialTagUnpair_Fragments.MaterialTagUnpair_ItemFrag;
import com.example.crfid.materialTagUnpair_Fragments.MaterialTagUnpair_Lists;

public
class MatTagUnPair_Frag_adaptor extends FragmentStateAdapter {
    public
    MatTagUnPair_Frag_adaptor ( @NonNull FragmentActivity fragmentActivity ) {
        super ( fragmentActivity );
    }

    @NonNull
    @Override
    public
    Fragment createFragment ( int position ) {
        switch ( position ) {
            case 0 : return new MaterialTagUnpair_Lists ();
            case 1 : return new MaterialTagUnpair_ItemFrag () ;
            case 2 : return new MaterialTagUnpair_ItemDetail ();
            default : return new MaterialTagUnpair_Lists ();
        }
    }

    @Override
    public
    int getItemCount () {
        return 3;
    }
}
