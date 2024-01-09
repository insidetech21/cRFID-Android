package com.example.crfid.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public
class ConnectionStatus_ViewModel extends ViewModel {
    private MutableLiveData<String> connectStatus = new MutableLiveData<> ( );

    public
    MutableLiveData<String> getconnectStatus () {
        return connectStatus;
    }

    public
    void setconnectStatus ( String data ) {
        connectStatus.setValue ( data );
    }
}
