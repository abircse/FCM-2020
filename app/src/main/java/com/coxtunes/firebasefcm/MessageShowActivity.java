package com.coxtunes.firebasefcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MessageShowActivity extends AppCompatActivity {

    private ImageView NotificationImage;
    private TextView Tvtitle,Tvtime,Tvdetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_show);

        NotificationImage = findViewById(R.id.featureGraphics);
        Tvtitle = findViewById(R.id.header);
        Tvtime = findViewById(R.id.timeStamp);
        Tvdetails = findViewById(R.id.article);

        //receive data from MyFirebaseMessagingService class
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String timeStampString = intent.getStringExtra("timestamp");
        String articleString = intent.getStringExtra("article_data");
        String imageUrl = intent.getStringExtra("image");

        //Set data on UI
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(NotificationImage);

        Tvtitle.setText(title);
        Tvtime.setText(timeStampString);
        Tvdetails.setText(articleString);

    }

}
