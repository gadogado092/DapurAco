package budi.dapuraco;

import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static final int NOTIFICATION_CHANNEL_ID2 = 1593;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String messageTittle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getBody();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("NotificationData", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(String.valueOf(new Random(NOTIFICATION_CHANNEL_ID2)), messageTittle+" : "+messageBody);
        editor.apply();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("NotificationData", 0);
        Map<String, String> notificationMessages = (Map<String, String>) sharedPref.getAll();

        NotificationHelper notificationHelper= new NotificationHelper(getApplicationContext());

        if (notificationMessages.size()>1){
            notificationHelper.createNotification2(messageTittle, String.valueOf(notificationMessages.size())+" New Message",messageBody);
        }else {
            notificationHelper.createNotification(messageTittle,messageBody);
        }



        /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/
    }
}
