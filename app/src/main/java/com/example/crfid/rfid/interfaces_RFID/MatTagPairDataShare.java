package com.example.crfid.rfid.interfaces_RFID;

import com.zebra.rfid.api3.TagData;


public interface MatTagPairDataShare{

    void handleTriggerPress2 ( boolean pressed );

    void handleTagdata2 ( TagData[] tagData );
}