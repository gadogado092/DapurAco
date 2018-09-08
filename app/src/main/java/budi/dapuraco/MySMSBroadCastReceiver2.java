package budi.dapuraco;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MySMSBroadCastReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        Object[] pdus = (Object[])bundle.get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[])pdus[0]);
        //System.out.println("SMSTES"+message.getMessageBody());
        //Toast.makeText(context,"TES"+message.getMessageBody(),Toast.LENGTH_SHORT).show();
        Intent smsIntent = new Intent("otp");
        smsIntent.putExtra("message",message.getMessageBody());
        LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);

    }




}
