package com.example.crfid.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public
class MaterialTagPair_ViewModel extends ViewModel {
    private MutableLiveData<List<String>> data = new MutableLiveData<> ();

    public void setData(List<String> data) {
        this.data.setValue(data);
    }

    public
    LiveData<List<String>> getData() {
        return data;
    }
}
