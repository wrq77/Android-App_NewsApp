package fr.isep.news;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import fr.isep.news.Model.Newsdetail;
import fr.isep.news.databinding.ActivityNewsdetailBinding;



public class NewsDetailActivity extends AppCompatActivity {

    private ActivityNewsdetailBinding binding;

    private String NewsTitle, NewsAuthor, NewsPublishAt, NewsContent, NewsImageURL, NewsURL, NewsDescription;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String userId;


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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        if (NewsContent == null) {
            NewsContent = "There is no content for this news...Click the button below to get more...";
        }
        String[] temp = NewsContent.split("\\[");

        binding = ActivityNewsdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.home.setOnClickListener(this::ClicktoHomePage);
        binding.manageAccount.setOnClickListener(this::ClicktoProfile);
        binding.collect.setOnClickListener(this::CollectNews);
        binding.NotCollect.setOnClickListener(this::NotCollectNews);

        binding.NewsTitle.setText(NewsTitle);
        binding.NewsAuthor.setText("Author: " + NewsAuthor);
        binding.NewsPublishAt.setText("Publish Date: " + NewsPublishAt);
        binding.NewsDescription.setText(NewsDescription);
        binding.NewsContent.setText(temp[0]);


        Picasso.get().load(NewsImageURL).into(binding.NewsImage);

        binding.ReadMore.setOnClickListener(this::ReadMore);

        setImageState();

    }


    private void setImageState() {

        DocumentReference documentReference = db.collection("user").document(userId).collection("News").document(NewsTitle);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e == null && documentSnapshot.exists()) {
                    binding.NotCollect.setVisibility(View.VISIBLE);
                    binding.collect.setVisibility(View.GONE);
                } else {
                    binding.NotCollect.setVisibility(View.GONE);
                    binding.collect.setVisibility(View.VISIBLE);
                }
            }

        });

    }


    private void CollectNews(View view) {

        Newsdetail News = new Newsdetail(NewsTitle, NewsAuthor, NewsPublishAt, NewsDescription,
                NewsContent, NewsImageURL, NewsImageURL);

        DocumentReference documentReference = db.collection("user").document(userId).collection("News").document(NewsTitle);

        documentReference.set(News).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("tag", "News is added with ID: " + NewsTitle);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("tag", "Error adding document" + e.toString());
            }
        });

        setImageState();

    }


    private void NotCollectNews(View view) {
        DocumentReference documentReference = db.collection("user").document(userId).collection("News").document(NewsTitle);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("tag", "Remove this news from the collection "+NewsTitle);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("d", "Error deleting", e);
            }
        });

        setImageState();
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
