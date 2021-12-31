package fr.isep.news;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailActivity extends AppCompatActivity {

    String NewsTitle;
    String NewsContent;
    String NewsImage;
    String NewsURL;
    String NewsDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetail);

        NewsTitle = getIntent().getStringExtra("NewsTitle");
        NewsDescription = getIntent().getStringExtra("NewsDescription");
        NewsContent = getIntent().getStringExtra("NewsContent");
        NewsImage = getIntent().getStringExtra("NewsImage");
        NewsURL = getIntent().getStringExtra("NewsURL");
    }
}
