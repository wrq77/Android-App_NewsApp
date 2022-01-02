package fr.isep.news;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    String NewsTitle;
    String NewsAuthor;
    String NewsPublishAt;
    String NewsContent;
    String NewsImageURL;
    String NewsURL;
    String NewsDescription;

    private TextView titleTV, authorTV, publishTV, descriptionTV, contentTV;
    private ImageView newsImage;

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

        titleTV.setText(NewsTitle);
        authorTV.setText(NewsAuthor);
        publishTV.setText(NewsPublishAt);
        descriptionTV.setText(NewsDescription);
        contentTV.setText(NewsContent);

        Picasso.get().load(NewsImageURL).into(newsImage);
    }
}
