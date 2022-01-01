package fr.isep.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import fr.isep.news.API.RetrofitBuilder;
import fr.isep.news.API.RetrofitInterface;
import fr.isep.news.Adapter.CategoryRecyclerVAdapter;
import fr.isep.news.Adapter.NewsRecyclerVAdapter;
import fr.isep.news.model.Category;
import fr.isep.news.model.News;
import fr.isep.news.model.Newsdetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRecyclerVAdapter.CategoryClickInterface{

    private RecyclerView NewsRV, CategoryRV;

    private ArrayList<Newsdetail> newsdetailArrayList = new ArrayList<>();
    private ArrayList<Category> categoryArrayList = new ArrayList<>();

    private CategoryRecyclerVAdapter categotyRecyclerVAdapter;
    private NewsRecyclerVAdapter newsRecyclerVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewsRV = findViewById(R.id.News);
        CategoryRV = findViewById(R.id.Category);

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
                    newsdetailArrayList.add(new Newsdetail(newsdetails.get(i).getTitle(),newsdetails.get(i).getUrl(),
                            newsdetails.get(i).getUrlToImage(),newsdetails.get(i).getDescription(),newsdetails.get(i).getContent()));
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