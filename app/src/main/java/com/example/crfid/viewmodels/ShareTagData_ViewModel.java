package com.example.crfid.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public
class ShareTagData_ViewModel extends ViewModel {
    private MutableLiveData<String> liveTagData = new MutableLiveData<> ( );

    public
    MutableLiveData<String> getLiveTagData () {
        return liveTagData;
    }

    public
    void setLiveTagData ( String data ) {
        liveTagData.setValue ( data );
    }
}
