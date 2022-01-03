package fr.isep.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fr.isep.news.NewsAPI.RetrofitBuilder;
import fr.isep.news.NewsAPI.RetrofitInterface;
import fr.isep.news.Adapter.CategoryRecyclerVAdapter;
import fr.isep.news.Adapter.NewsRecyclerVAdapter;
import fr.isep.news.Model.Category;
import fr.isep.news.Model.News;
import fr.isep.news.Model.Newsdetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CategoryRecyclerVAdapter.CategoryClickInterface{

    private RecyclerView NewsRV, CategoryRV;

    private TextView hint;

    private ArrayList<Newsdetail> newsdetailArrayList = new ArrayList<>();
    private ArrayList<Category> categoryArrayList = new ArrayList<>();

    private CategoryRecyclerVAdapter categotyRecyclerVAdapter;
    private NewsRecyclerVAdapter newsRecyclerVAdapter;

    private ImageView Click_to_Profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewsRV = findViewById(R.id.News);
        CategoryRV = findViewById(R.id.Category);
        hint = findViewById(R.id.Hint);
        Click_to_Profile = findViewById(R.id.manageAccount);
        Click_to_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });



        categotyRecyclerVAdapter = new CategoryRecyclerVAdapter(categoryArrayList,this,this::onClickCategory);
        newsRecyclerVAdapter = new NewsRecyclerVAdapter(newsdetailArrayList,this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        NewsRV.setLayoutManager(layoutManager);
        NewsRV.setAdapter(newsRecyclerVAdapter);

        CategoryRV.setAdapter(categotyRecyclerVAdapter);
        getCategory();

    }

    @Override
    public void onClickCategory(int position) {
        String categoryName = categoryArrayList.get(position).getCategoryName();
        hint.setVisibility(View.INVISIBLE);
        getNews(categoryName);

    }

    private void getCategory(){
        //TODO change to users to choose the category
        categoryArrayList.add(new Category("technology"));
        categoryArrayList.add(new Category("health"));
        categoryArrayList.add(new Category("entertainment"));
        categoryArrayList.add(new Category("sports"));
        categoryArrayList.add(new Category("business"));
        categoryArrayList.add(new Category("general"));

        categotyRecyclerVAdapter.notifyDataSetChanged();

    }

    private void getNews(String CategoryName){
        newsdetailArrayList.clear();

        String apiKey ="2a75f3dbcae446c4868c3e50e889dab7";

        RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
        Call<News> call = retrofitInterface.getNewsByCategory(CategoryName,apiKey);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                News news = response.body();
                ArrayList<Newsdetail> newsdetails = news.getArticles();
                for (int i = 0; i < newsdetails.size(); i++){
                    newsdetailArrayList.add(new Newsdetail(newsdetails.get(i).getTitle(),newsdetails.get(i).getAuthor(),newsdetails.get(i).getPublishedAt(),
                            newsdetails.get(i).getDescription(),newsdetails.get(i).getUrl(), newsdetails.get(i).getUrlToImage(),newsdetails.get(i).getContent()));
                }
                newsRecyclerVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}