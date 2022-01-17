package fr.isep.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import fr.isep.news.Adapter.NewsRecyclerVAdapter;
import fr.isep.news.Model.Newsdetail;
import fr.isep.news.databinding.ActivityCollectionBinding;


public class CollectionActivity extends AppCompatActivity {

    private ActivityCollectionBinding binding;

    private ArrayList<Newsdetail> NewsArrayList = new ArrayList<>();
    private NewsRecyclerVAdapter newsRecyclerVAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();


        binding = ActivityCollectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.home.setOnClickListener(this::ClicktoHomePage);
        binding.manageAccount.setOnClickListener(this::ClickToProfile);

        newsRecyclerVAdapter = new NewsRecyclerVAdapter(NewsArrayList,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.News.setLayoutManager(layoutManager);
        binding.News.setAdapter(newsRecyclerVAdapter);

        getNews();

    }

    private void getNews() {

        DocumentReference documentReference = db.collection("user").document(userId);

        documentReference.collection("News").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("tag", document.getId() + " => " + document.getData());
                        NewsArrayList.add(new Newsdetail(document.getString("title"),document.getString("author"),document.getString("publishedAt"),
                                document.getString("description"),document.getString("url"),document.getString("urlToImage"),document.getString("content")));
                    }
                    newsRecyclerVAdapter.notifyDataSetChanged();
                }else {
                    Log.d("tag", "Error getting documents.", task.getException());
                }
            }
        });

    }

    private void ClicktoHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private void ClickToProfile(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }
}
