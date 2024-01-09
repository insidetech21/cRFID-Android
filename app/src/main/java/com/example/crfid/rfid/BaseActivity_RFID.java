package com.example.crfid.rfid;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.crfid.rfid.events.RFIDTagReadEvent;
import com.example.crfid.rfid.events.RFIDTriggerEvent;
import com.example.crfid.rfid.interfaces_RFID.RFID_Context;
import com.example.crfid.viewmodels.ConnectionStatus_ViewModel;
import com.zebra.rfid.api3.ACCESS_OPERATION_CODE;
import com.zebra.rfid.api3.ACCESS_OPERATION_STATUS;
import com.zebra.rfid.api3.Antennas;
import com.zebra.rfid.api3.ENUM_TRANSPORT;
import com.zebra.rfid.api3.ENUM_TRIGGER_MODE;
import com.zebra.rfid.api3.HANDHELD_TRIGGER_EVENT_TYPE;
import com.zebra.rfid.api3.INVENTORY_STATE;
import com.zebra.rfid.api3.InvalidUsageException;
import com.zebra.rfid.api3.OperationFailureException;
import com.zebra.rfid.api3.RFIDReader;
import com.zebra.rfid.api3.ReaderDevice;
import com.zebra.rfid.api3.Readers;
import com.zebra.rfid.api3.RfidEventsListener;
import com.zebra.rfid.api3.RfidReadEvents;
import com.zebra.rfid.api3.RfidStatusEvents;
import com.zebra.rfid.api3.SESSION;
import com.zebra.rfid.api3.SL_FLAG;
import com.zebra.rfid.api3.START_TRIGGER_TYPE;
import com.zebra.rfid.api3.STATUS_EVENT_TYPE;
import com.zebra.rfid.api3.STOP_TRIGGER_TYPE;
import com.zebra.rfid.api3.TagData;
import com.zebra.rfid.api3.TriggerInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public abstract
class BaseActivity_RFID extends AppCompatActivity implements Readers.RFIDReaderEventHandler {
    final static String TAG = "RFID_SAMPLE";
    // RFID Reader
    private static Readers readers;
    private static ArrayList<ReaderDevice> availableRFIDReaderList;
    private static ReaderDevice readerDevice;
    private static RFIDReader reader;
    // UI and context
//    TextView textView;
    // In case of RFD8500 change reader name with intended device below from list of paired RFD8500
    String readerName = "RFD4031-G10B700-JP";
    private EventHandler eventHandler;
    // general
    private int MAX_POWER = 270;
    Context context;



    public void onCreate(Context activity){
        context=activity;
    }
    protected
    void InitSDK () {
        Log.d ( TAG ,
                "InitSDK" );
        if ( readers == null ) {
            new CreateInstanceTask ( ).execute ( );
        } else
            connectReader ( );
    }

    protected synchronized
    void connectReader () {
        if ( !isReaderConnected ( ) ) {
            new ConnectionTask ( ).execute ( );
        }
    }

    @Override
    public
    void RFIDReaderAppeared ( ReaderDevice readerDevice ) {
        Log.d ( TAG ,
                "RFIDReaderAppeared " + readerDevice.getName ( ) );
        connectReader ( );
    }

    @Override
    public
    void RFIDReaderDisappeared ( ReaderDevice readerDevice ) {
        Log.d ( TAG ,
                "RFIDReaderDisappeared " + readerDevice.getName ( ) );
        if ( readerDevice.getName ( ).equals ( reader.getHostName ( ) ) )
            disconnect ( );
    }

    protected synchronized
    String connect () {
        if ( reader != null ) {
            Log.d ( TAG ,
                    "connect " + reader.getHostName ( ) );
            try {
                if ( !reader.isConnected ( ) ) {
                    // Establish connection to the RFID Reader
                    reader.connect ( );
                    ConfigureReader ( );
                    if ( reader.isConnected ( ) ) {
                        return "Connected: " + reader.getHostName ( );
                    }
                }
            }
            catch ( InvalidUsageException e ) {
                e.printStackTrace ( );
            }
            catch ( OperationFailureException e ) {
                e.printStackTrace ( );
                Log.d ( TAG ,
                        "OperationFailureException " + e.getVendorMessage ( ) );
                String des = e.getResults ( ).toString ( );
                return "Connection failed" + e.getVendorMessage ( ) + " " + des;
            }
        }
        return "";
    }

    protected
    void ConfigureReader () {
        Log.d ( TAG ,
                "ConfigureReader " + reader.getHostName ( ) );
        if ( reader.isConnected ( ) ) {
            TriggerInfo triggerInfo = new TriggerInfo ( );
            triggerInfo.StartTrigger.setTriggerType ( START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE );
            triggerInfo.StopTrigger.setTriggerType ( STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE );
            try {
                // receive events from reader
                if ( eventHandler == null )
                    eventHandler = new EventHandler ( );
                reader.Events.addEventsListener ( eventHandler );
                // HH event
                reader.Events.setHandheldEvent ( true );
                // tag event with tag data
                reader.Events.setTagReadEvent ( true );
                reader.Events.setAttachTagDataWithReadEvent ( false );
                // set trigger mode as rfid so scanner beam will not come
                reader.Config.setTriggerMode ( ENUM_TRIGGER_MODE.RFID_MODE ,
                        true );
                // set start and stop triggers
                reader.Config.setStartTrigger ( triggerInfo.StartTrigger );
                reader.Config.setStopTrigger ( triggerInfo.StopTrigger );
                // power levels are index based so maximum power supported get the last one
                MAX_POWER = reader.ReaderCapabilities.getTransmitPowerLevelValues ( ).length - 1;
                // set antenna configurations
                Antennas.AntennaRfConfig config = reader.Config.Antennas.getAntennaRfConfig ( 1 );
                config.setTransmitPowerIndex ( MAX_POWER );
                config.setrfModeTableIndex ( 0 );
                config.setTari ( 0 );
                reader.Config.Antennas.setAntennaRfConfig ( 1 ,
                        config );
                // Set the singulation control
                Antennas.SingulationControl s1_singulationControl = reader.Config.Antennas.getSingulationControl ( 1 );
                s1_singulationControl.setSession ( SESSION.SESSION_S0 );
                s1_singulationControl.Action.setInventoryState ( INVENTORY_STATE.INVENTORY_STATE_A );
                s1_singulationControl.Action.setSLFlag ( SL_FLAG.SL_ALL );
                reader.Config.Antennas.setSingulationControl ( 1 ,
                        s1_singulationControl );
                // delete any prefilters
                reader.Actions.PreFilters.deleteAll ( );
                //
            }
            catch ( InvalidUsageException | OperationFailureException e ) {
                e.printStackTrace ( );
            }
        }
    }

    private synchronized
    void disconnect () {
        Log.d ( TAG ,
                "disconnect " + reader );
        try {
            if ( reader != null ) {
                reader.Events.removeEventsListener ( eventHandler );
                reader.disconnect ( );
                this.runOnUiThread ( new Runnable ( ) {
                    @Override
                    public
                    void run () {
//                        textView.setText ( "Disconnected" );
                    }
                } );
            }
        }
        catch ( InvalidUsageException e ) {
            e.printStackTrace ( );
        }
        catch ( OperationFailureException e ) {
            e.printStackTrace ( );
        }
        catch ( Exception e ) {
            e.printStackTrace ( );
        }
    }

    private synchronized
    void dispose () {
        try {
            if ( readers != null ) {
                reader = null;
                readers.Dispose ( );
                readers = null;
            }
        }
        catch ( Exception e ) {
            e.printStackTrace ( );
        }
    }

    public synchronized
    void performInventory () {
        // check reader connection
        if ( !isReaderConnected ( ) )
            return;
        try {
            reader.Actions.Inventory.perform ( );
        }
        catch ( InvalidUsageException e ) {
            e.printStackTrace ( );
        }
        catch ( OperationFailureException e ) {
            e.printStackTrace ( );
        }
    }

//    protected abstract
//    void handleTriggerPress ( boolean pressed );
//
//    protected abstract
//    void handleTagdata ( TagData[] tagData );

    public synchronized
    void stopInventory () {
        // check reader connection
        if ( !isReaderConnected ( ) )
            return;
        try {
            reader.Actions.Inventory.stop ( );
        }
        catch ( InvalidUsageException e ) {
            e.printStackTrace ( );
        }
        catch ( OperationFailureException e ) {
            e.printStackTrace ( );
        }
    }

    public
    String onResume2 () {
        return connect ( );
    }

    public
    void onPause2 () {
        disconnect ( );
    }

    public
    void onDestroy2 () {
        dispose ( );
    }

    private synchronized
    void GetAvailableReader () {
        Log.d ( TAG ,
                "GetAvailableReader" );
        if ( readers != null ) {
            readers.attach ( this );
            try {
                if ( readers.GetAvailableRFIDReaderList ( ) != null ) {
                    availableRFIDReaderList = readers.GetAvailableRFIDReaderList ( );
                    if ( availableRFIDReaderList.size ( ) != 0 ) {
                        // if single reader is available then connect it
                        if ( availableRFIDReaderList.size ( ) == 1 ) {
                            readerDevice = availableRFIDReaderList.get ( 0 );
                            reader = readerDevice.getRFIDReader ( );
                        } else {
                            // search reader specified by name
                            for (ReaderDevice device : availableRFIDReaderList) {
                                if ( device.getName ( ).equals ( readerName ) ) {
                                    readerDevice = device;
                                    reader = readerDevice.getRFIDReader ( );
                                }
                            }
                        }
                    }
                }
            }
            catch ( InvalidUsageException ie ) {

            }
        }
    }

    protected
    boolean isReaderConnected () {
        if ( reader != null && reader.isConnected ( ) )
            return true;
        else {
            Log.d ( TAG ,
                    "reader is not connected" );
            return false;
        }
    }

    public
    void changeToSingleScanMode () {
        try {
            TriggerInfo triggerInfo = new TriggerInfo ( );
            triggerInfo.StopTrigger.setTriggerType ( STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_TAG_OBSERVATION_WITH_TIMEOUT );
            Long tagObservation = Long.parseLong ( "1" );
            triggerInfo.StopTrigger.TagObservation.setN ( tagObservation.shortValue ( ) );
            reader.Config.setStartTrigger ( triggerInfo.StartTrigger );
            reader.Config.setStopTrigger ( triggerInfo.StopTrigger );
        }
        catch ( InvalidUsageException e ) {
            e.printStackTrace ( );
        }
        catch ( OperationFailureException e ) {
            e.printStackTrace ( );
        }

    }

    public
    void changeToBulkScanMode () {
        try {
            TriggerInfo triggerInfo = new TriggerInfo ( );
            triggerInfo.StartTrigger.setTriggerType ( START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE );
            triggerInfo.StopTrigger.setTriggerType ( STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE );
            reader.Config.setStartTrigger ( triggerInfo.StartTrigger );
            reader.Config.setStopTrigger ( triggerInfo.StopTrigger );
        }
        catch ( InvalidUsageException e ) {
            e.printStackTrace ( );
        }
        catch ( OperationFailureException e ) {
            e.printStackTrace ( );
        }

    }

    public
    void setReaderPower ( int power ) {
        AsyncTask task = new AsyncTask ( ) {
            @Override
            protected
            Object doInBackground ( Object[] objects ) {
                try {
                    Antennas.AntennaRfConfig antennaRfConfig = reader.Config.Antennas.getAntennaRfConfig ( 1 );
                    antennaRfConfig.setTransmitPowerIndex ( power );
                    reader.Config.Antennas.setAntennaRfConfig ( 1 ,
                            antennaRfConfig );
//                    reader.Config.Antennas.getAntennaRfConfig(1).setTransmitPowerIndex(power);
                }
                catch ( InvalidUsageException e ) {
                    e.printStackTrace ( );
                }
                catch ( OperationFailureException e ) {
                    e.printStackTrace ( );
                }
                return null;
            }
        };
        task.execute ( );
    }

    public
    class EventHandler implements RfidEventsListener {
        // Read Event Notification
        public
        void eventReadNotify ( RfidReadEvents e ) {
            // Recommended to use new method getReadTagsEx for better performance in case of large tag population
            TagData[] myTags = reader.Actions.getReadTags ( 100 );
            if ( myTags != null ) {
                for (int index = 0; index < myTags.length; index++) {
                    Log.d ( TAG ,
                            "Tag ID " + myTags[ index ].getTagID ( ) );
                    if ( myTags[ index ].getOpCode ( ) == ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ &&
                            myTags[ index ].getOpStatus ( ) == ACCESS_OPERATION_STATUS.ACCESS_SUCCESS ) {
                        if ( myTags[ index ].getMemoryBankData ( ).length ( ) > 0 ) {
                            Log.d ( TAG ,
                                    " Mem Bank Data " + myTags[ index ].getMemoryBankData ( ) );
                        }
                    }
                    if ( myTags[ index ].isContainsLocationInfo ( ) ) {
                        short dist = myTags[ index ].LocationInfo.getRelativeDistance ( );
                        Log.d ( TAG ,
                                "Tag relative distance " + dist );
                    }
                }
                // possibly if operation was invoked from async task and still busy
                // handle tag data responses on parallel thread thus THREAD_POOL_EXECUTOR
                new EventHandler.AsyncDataUpdate ( ).executeOnExecutor ( AsyncTask.THREAD_POOL_EXECUTOR ,
                        myTags );
            }
        }

        // Status Event Notification
        public
        void eventStatusNotify ( RfidStatusEvents rfidStatusEvents ) {
            Log.d ( TAG ,
                    "Status Notification: " + rfidStatusEvents.StatusEventData.getStatusEventType ( ) );
            if ( rfidStatusEvents.StatusEventData.getStatusEventType ( ) == STATUS_EVENT_TYPE.HANDHELD_TRIGGER_EVENT ) {
                if ( rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent ( ) == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED ) {
                    new AsyncTask<Void, Void, Void> ( ) {
                        @Override
                        protected
                        Void doInBackground ( Void... voids ) {

                            EventBus.getDefault ().post ( new RFIDTriggerEvent ( true ) );
//                            handleTriggerPress ( true );
//                            performInventory ();
                            return null;
                        }
                    }.execute ( );
                }
                if ( rfidStatusEvents.StatusEventData.HandheldTriggerEventData.getHandheldEvent ( ) == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED ) {
                    new AsyncTask<Void, Void, Void> ( ) {
                        @Override
                        protected
                        Void doInBackground ( Void... voids ) {
                            EventBus.getDefault ().post ( new RFIDTriggerEvent ( false ) );
//                            handleTriggerPress ( false );
//                            stopInventory ();
                            return null;
                        }
                    }.execute ( );
                }
            }
        }

        private
        class AsyncDataUpdate extends AsyncTask<TagData[], Void, Void> {
            @Override
            protected
            Void doInBackground ( TagData[]... params ) {
                EventBus.getDefault ().post ( new RFIDTagReadEvent ( params[0] ) );
//                handleTagdata ( params[ 0 ] );
                return null;
            }
        }
    }

    protected
    class ConnectionTask extends AsyncTask<Void, Void, String> {
        @Override
        protected
        String doInBackground ( Void... voids ) {
            Log.d ( TAG ,
                    "ConnectionTask" );
            GetAvailableReader ( );
            if ( reader != null )
                return connect ( );
            return "Failed to find or connect reader";
        }

        @Override
        protected
        void onPostExecute ( String result ) {
            super.onPostExecute ( result );
//            textView.setText ( result );
        }
    }

    protected
    class CreateInstanceTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected
        Void doInBackground ( Void... voids ) {
            Log.d ( TAG ,
                    "CreateInstanceTask" );
            // Based on support available on host device choose the reader type
            InvalidUsageException invalidUsageException = null;
            readers = new Readers ( context ,
                    ENUM_TRANSPORT.ALL );
            try {
                availableRFIDReaderList = readers.GetAvailableRFIDReaderList ( );
            }
            catch ( InvalidUsageException e ) {
                e.printStackTrace ( );
            }
            if ( invalidUsageException != null ) {
                readers.Dispose ( );
                readers = null;
                if ( readers == null ) {
                    readers = new Readers ( context,
                            ENUM_TRANSPORT.BLUETOOTH );
                }
            }
            return null;
        }

        @Override
        protected
        void onPostExecute ( Void aVoid ) {
            super.onPostExecute ( aVoid );
            connectReader ( );
        }
    }
}
