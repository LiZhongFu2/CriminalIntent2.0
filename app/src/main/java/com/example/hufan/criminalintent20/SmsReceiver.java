package com.example.hufan.criminalintent20;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by hufan on 2016/9/28.
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            abortBroadcast();
            StringBuilder sb=new StringBuilder();
            Bundle bundle=intent.getExtras();
            if(bundle!=null){
                Object[] pdus=(Object[]) bundle.get("pdus");
                SmsMessage[] messages=new SmsMessage[pdus.length];
                for(int i=0;i<pdus.length;i++){
                    messages[i]=SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                    for(SmsMessage message:messages){
                        sb.append("短信来源：");
                        sb.append(message.getDisplayOriginatingAddress());
                        sb.append("\n--------短信内容--------\n");
                        sb.append(message.getDisplayMessageBody());
                    }

            }
            Toast.makeText(context,sb.toString(),Toast.LENGTH_LONG).show();
        }
    }
}
