package fr.isep.news;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


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


//TODO Toast 不显示问题

public class NewsDetailActivity extends AppCompatActivity {

    private ActivityNewsdetailBinding binding;

    private String NewsTitle,NewsAuthor,NewsPublishAt,NewsContent,NewsImageURL,NewsURL,NewsDescription;

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


        String[] temp = NewsContent.split("\\[");

        binding = ActivityNewsdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.home.setOnClickListener(this::ClicktoHomePage);
        binding.manageAccount.setOnClickListener(this::ClicktoProfile);
        binding.collect.setOnClickListener(this::CollectNews);

        binding.NewsTitle.setText(NewsTitle);
        binding.NewsAuthor.setText("Author: "+NewsAuthor);
        binding.NewsPublishAt.setText("Publish Date: "+NewsPublishAt);
        binding.NewsDescription.setText(NewsDescription);
        binding.NewsContent.setText(temp[0]);


        Picasso.get().load(NewsImageURL).into(binding.NewsImage);

        binding.ReadMore.setOnClickListener(this::ReadMore);



    }


    private void CollectNews(View view) {

        binding.collect.setVisibility(View.GONE);

        Newsdetail News = new Newsdetail(NewsTitle,NewsAuthor,NewsPublishAt,NewsDescription,
                NewsContent,NewsImageURL,NewsImageURL);

        DocumentReference documentReference = db.collection("user").document(userId).collection("News").document(NewsTitle);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e == null && documentSnapshot.exists()) {
                    Log.d("tag", "This News is already collected");
                    Toast.makeText(NewsDetailActivity.this, "This News is already collected", Toast.LENGTH_SHORT).show();
                }else{
                    documentReference.set(News).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("tag", "News is added with ID: " + NewsTitle);
                            Toast.makeText(NewsDetailActivity.this, "Collect success", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "Error adding document" + e.toString());
                        }
                    });

                }
            }

        });

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
