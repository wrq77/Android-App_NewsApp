package fr.isep.news.API;

import fr.isep.news.model.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET("top-headlines?country=fr")
    Call<News> getNewsByCategory(@Query("category") String category, @Query("apiKey") String apiKey);
}
