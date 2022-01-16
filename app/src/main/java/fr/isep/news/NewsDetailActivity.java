package fr.isep.news;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import fr.isep.news.databinding.ActivityNewsdetailBinding;

/*
   TODO :
      1. Click the "Heart" to store the news to the database
   */

public class NewsDetailActivity extends AppCompatActivity {

    private ActivityNewsdetailBinding binding;
    private String NewsTitle,NewsAuthor,NewsPublishAt,NewsContent,NewsImageURL,NewsURL,NewsDescription;


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

        String[] temp = NewsContent.split("\\[");

        binding = ActivityNewsdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.home.setOnClickListener(this::ClicktoHomePage);
        binding.manageAccount.setOnClickListener(this::ClicktoProfile);

        binding.NewsTitle.setText(NewsTitle);
        binding.NewsAuthor.setText("Author: "+NewsAuthor);
        binding.NewsPublishAt.setText("Publish Date: "+NewsPublishAt);
        binding.NewsDescription.setText(NewsDescription);
        binding.NewsContent.setText(temp[0]);


        Picasso.get().load(NewsImageURL).into(binding.NewsImage);

        binding.ReadMore.setOnClickListener(this::ReadMore);


    }

    //Jump to show the source and all of the news
    private void ReadMore(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(NewsURL));
        startActivity(intent);
    }

    private void ClicktoProfile(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    private void ClicktoHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


}
