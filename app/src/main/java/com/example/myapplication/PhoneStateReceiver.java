package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Date;

public   class PhoneStateReceiver extends BroadcastReceiver {
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static boolean isIncoming;
    private static String savedNumber;

    @Override
    public void onReceive(Context context, Intent intent) {
//        try {
//            System.out.println("Receiver start");
//            Toast.makeText(context," Receiver start ",Toast.LENGTH_SHORT).show();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
        }
        else{
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }


            onCallStateChanged(context, state, number);
        }

    }
    public void onCallStateChanged(Context context, int state, String number) {
        if(lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;

                savedNumber = number;
                Toast.makeText(context," Phone Ringing ",Toast.LENGTH_SHORT).show();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if(lastState != TelephonyManager.CALL_STATE_RINGING){
                    isIncoming = false;

                    Toast.makeText(context," State OffHook  isIncoming true",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    isIncoming = true;
                    Toast.makeText(context,"State OffHook Talk Mode",Toast.LENGTH_SHORT).show();
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if(lastState == TelephonyManager.CALL_STATE_RINGING){
                    //Ring but no pickup-  a miss
                    Toast.makeText(context,"Cancelled",Toast.LENGTH_SHORT).show();
                }
                else if(isIncoming){
                    Toast.makeText(context,"Cancelled By Me",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"State IDLE Saved Number",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        lastState = state;
    }
}
