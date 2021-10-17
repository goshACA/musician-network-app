package com.musapp.musicapp.firebase_messaging_notifications;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotifyComment extends AsyncTask<Void, Void, Void> {

    private String receiverToken;
    private String notificationTitle;
    private String notificationBody;
    private String receiverId;
    private String postId;
    private String commenterId;
    private String commenterImageUrl;
    private long date;

    public NotifyComment(String receiverToken, String notificationTitle, String notificationBody, String receiverId, String commenterId, String postId, String commenterImageUrl, long date) {
        this.receiverToken = receiverToken;
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
        this.receiverId = receiverId;
        this.commenterId = commenterId;
        this.postId = postId;
        this.commenterImageUrl = commenterImageUrl;
        this.date = date;
    }

    @Override
    protected Void doInBackground(Void... voids) {


        try {

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=AIzaSyCgGkIpG7sPYu99dcZEuhLWr3KacbjGzzU");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();


            json.put("to", receiverToken);
            json.put("priority", "high");


            JSONObject info = new JSONObject();
            info.put("title", notificationTitle);   // Notification title
            info.put("body", notificationBody); // Notification body
            info.put("tag", receiverId);
            info.put("commenterId", commenterId);
            info.put("postId", postId);
            info.put("commenterImageUrl", commenterImageUrl);
            info.put("date", date);
            info.put("type", "comment");
            json.put("data", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

        } catch (Exception e) {
            Log.d("Error", "" + e);
        }


        return null;
    }
}