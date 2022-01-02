package fr.isep.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    /* TODO :
       1. HOW to get the full content of the truncated text of api
       2. Click the "Heart" to store the news to the database
       3. Click the button to the profile management
    */

    private String NewsTitle,NewsAuthor,NewsPublishAt,NewsContent,NewsImageURL,NewsURL,NewsDescription;

    private TextView titleTV, authorTV, publishTV, descriptionTV, contentTV;
    private ImageView newsImage, ClickToHomePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);

        NewsTitle = getIntent().getStringExtra("NewsTitle");
        NewsAuthor = getIntent().getStringExtra("NewsAuthor");
        NewsPublishAt = getIntent().getStringExtra("NewsPulishedAt");
        NewsDescription = getIntent().getStringExtra("NewsDescription");
        NewsContent = getIntent().getStringExtra("NewsContent");
        NewsImageURL = getIntent().getStringExtra("NewsImageURL");
        NewsURL = getIntent().getStringExtra("NewsURL");

        titleTV = findViewById(R.id.NewsTitle);
        authorTV = findViewById(R.id.NewsAuthor);
        publishTV = findViewById(R.id.NewsPublishAt);
        descriptionTV = findViewById(R.id.NewsDescription);
        contentTV = findViewById(R.id.NewsContent);

        newsImage = findViewById(R.id.NewsImage);
        ClickToHomePage = findViewById(R.id.home);
        ClickToHomePage.setOnClickListener(this::ClicktoHomePage);

        titleTV.setText(NewsTitle);
        authorTV.setText("Author: "+NewsAuthor);
        publishTV.setText("Publish Date: "+NewsPublishAt);
        descriptionTV.setText(NewsDescription);
        contentTV.setText(NewsContent);

        Picasso.get().load(NewsImageURL).into(newsImage);
    }

    private void ClicktoHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


}
