package msm.MemeSena.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONObject;

import msm.MemeSena.AppStickerActivity.StickerPackDetailsActivity;
import msm.MemeSena.R;

import static android.app.Notification.VISIBILITY_PUBLIC;

/**
 * Created by: Husnain Ali on 3/20/2018.
 * Company: Rapidzz Solutions
 * Email: husnain.ali02@gmail.com
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG ="MyFirebaseMessaging" ;

    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try{
            JSONObject data = new JSONObject(remoteMessage.getData().get("data"));

            String message_id = remoteMessage.getData().get("message_id");
            String detail_id = data.getString("detail_id");
            String title = data.getString("title");
            String message = data.getString("message");

            Log.e(TAG, "message_id: " + remoteMessage.getData().get("message_id"));
            Log.e(TAG, "detail_id: " + data.get("detail_id"));
            Log.e(TAG, "title: " + data.get("title"));
            Log.e(TAG, "message: " + data.get("message"));
                sendNotification(this,message,title,message_id,detail_id);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


    public static void sendNotification(Context context, String message, String title, String message_id, String detail_id) {
        Log.e("-------","-----sendNotification--"+message);
        Intent intent = new Intent(context, StickerPackDetailsActivity.class);
        intent.putExtra("message_id",message_id);
        intent.putExtra("id",detail_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        long[] v = new long[] { 1000, 1000, 1000, 1000, 1000 };
//        String channelId = getString(R.string.default_notification_channel_id);
        String channelId = message_id;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, "All Notification")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setVibrate(v)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("All Notification",
                    "All Categories", NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(VISIBILITY_PUBLIC);
            channel.enableLights(true);
//            channel.setLightColor(context.getResources().getColor(R.color.colorAccent));
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//            channel.setSound(defaultSoundUri);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        notificationManager.notify(Integer.parseInt(message_id) /* ID of notification */, notificationBuilder.build());
    }

}
