package com.example.crfid.rfid.events;

public class RFIDTriggerEvent {

    boolean pressed;
    public RFIDTriggerEvent(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
