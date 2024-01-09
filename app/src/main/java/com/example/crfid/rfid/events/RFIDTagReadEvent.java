package com.example.crfid.rfid.events;

import com.zebra.rfid.api3.TagData;

public class RFIDTagReadEvent {

    TagData[] tagData;

    public RFIDTagReadEvent(TagData[] tagData) {
        this.tagData = tagData;
    }

    public TagData[] getTagData() {
        return tagData;
    }

    public void setTagData(TagData[] tagData) {
        this.tagData = tagData;
    }
}
