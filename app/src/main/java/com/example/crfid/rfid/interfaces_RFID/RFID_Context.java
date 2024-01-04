package com.example.crfid.rfid.interfaces_RFID;

import android.content.Context;

import com.zebra.rfid.api3.TagData;

public
interface RFID_Context {

//    void onResume();
    Context getContext2();
    void runOnUiThread2(Runnable action);

    void handleTriggerPress ( boolean b );

    void handleTagdata ( TagData[] param );
}
