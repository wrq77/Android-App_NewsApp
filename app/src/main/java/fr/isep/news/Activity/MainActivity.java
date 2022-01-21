package fr.isep.news.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

import fr.isep.news.NewsAPI.RetrofitBuilder;
import fr.isep.news.NewsAPI.RetrofitInterface;
import fr.isep.news.Adapter.CategoryRecyclerVAdapter;
import fr.isep.news.Adapter.NewsRecyclerVAdapter;
import fr.isep.news.Model.Category;
import fr.isep.news.Model.News;
import fr.isep.news.Model.Newsdetail;
import fr.isep.news.R;
import fr.isep.news.databinding.ActivityMainBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements CategoryRecyclerVAdapter.CategoryClickInterface{

    private ActivityMainBinding binding;

    private ArrayList<Newsdetail> NewsdetailArrayList = new ArrayList<>();
    private ArrayList<Category> CategoryArrayList = new ArrayList<>();

    private CategoryRecyclerVAdapter categoryRecyclerVAdapter;
    private NewsRecyclerVAdapter newsRecyclerVAdapter;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    String userId,CategoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.manageAccount.setOnClickListener(this::ClickToProfile);
        binding.CollectionNews.setOnClickListener(this::ClickToCollection);

        categoryRecyclerVAdapter = new CategoryRecyclerVAdapter(CategoryArrayList,this,this::onClickCategory);
        newsRecyclerVAdapter = new NewsRecyclerVAdapter(NewsdetailArrayList,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.News.setLayoutManager(layoutManager);
        binding.News.setAdapter(newsRecyclerVAdapter);

        binding.Category.setAdapter(categoryRecyclerVAdapter);
        getCategory();

    }

    private void ClickToProfile(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    private void ClickToCollection(View view) {
        startActivity(new Intent(getApplicationContext(), CollectionActivity.class));
    }

    @Override
    public void onClickCategory(int position) {
        String categoryName = CategoryArrayList.get(position).getCategoryName();
        binding.Hint.setVisibility(View.INVISIBLE);
        getNews(categoryName);

    }

    private void getCategory(){

        DocumentReference documentReference = db.collection("category").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e == null && documentSnapshot.exists()) {
                CategoryName = documentSnapshot.getString("categoryName");
                //根据,分割category字符串
                String[] as = CategoryName.split(",");
                for (int i = 0; i < as.length; i++) {
                    if(as[i].equals("Business")){
                        CategoryArrayList.add(new Category("business"));}
                    if(as[i].equals("Entertainment")){
                        CategoryArrayList.add(new Category("entertainment"));}
                    if(as[i].equals("General")){ CategoryArrayList.add(new Category("general"));}
                    if(as[i].equals("Health")){
                        CategoryArrayList.add(new Category("health"));}
                    if(as[i].equals("Science")){
                        CategoryArrayList.add(new Category("science"));}
                    if(as[i].equals("Sports")){ CategoryArrayList.add(new Category("sports"));}
                    if(as[i].equals("Technology")){ CategoryArrayList.add(new Category("technology"));}
                }
                }else{
                    Log.d("tag", "onEvent: Document do not exists");
                }

                //Notifies the attached observers that the underlying data has been changed
                // and any View reflecting the data set should refresh itself.
                categoryRecyclerVAdapter.notifyDataSetChanged();
            }

        });

    }

    private void getNews(String CategoryName){
        NewsdetailArrayList.clear();

        String apiKey ="2a75f3dbcae446c4868c3e50e889dab7";

        if (isOnline()) {

            RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
            Call<News> call = retrofitInterface.getNewsByCategory(CategoryName, apiKey);

            //asynchronous
            call.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    News news = response.body();
                    ArrayList<Newsdetail> newsdetails = news.getArticles();
                    for (int i = 0; i < newsdetails.size(); i++) {
                        NewsdetailArrayList.add(new Newsdetail(newsdetails.get(i).getTitle(), newsdetails.get(i).getAuthor(), newsdetails.get(i).getPublishedAt(),
                                newsdetails.get(i).getDescription(), newsdetails.get(i).getUrl(), newsdetails.get(i).getUrlToImage(), newsdetails.get(i).getContent()));
                    }
                    newsRecyclerVAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    Log.d("tag", "Fail");
                }
            });
        }else{
            binding.Hint.setVisibility(View.VISIBLE);
            String str = "Oops..There is no network connection";
            binding.Hint.setText(str);
        }
    }

    // checking whether a network interface is available
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        Network networkInfo = connMgr.getActiveNetwork();
        return (networkInfo != null);
    }
}