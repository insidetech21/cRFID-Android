package com.example.crfid.rfid.events;

public
class ConnectionStatusEvent {
    public boolean connectionStatus;

    public  ConnectionStatusEvent(boolean connectionStatus){
        this.connectionStatus=connectionStatus;
    }

    public boolean getStatus(){
        return connectionStatus;
    }

    public void setStatus(boolean connectionStatus){
        this.connectionStatus=connectionStatus;
    }
}
