package fr.isep.news.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import fr.isep.news.R;
import fr.isep.news.model.Newsdetail;

public class NewsRecyclerVAdapter extends RecyclerView.Adapter<NewsRecyclerVAdapter.ViewHolder>{

    private ArrayList<Newsdetail> DataSet;
    private Context context;

    public NewsRecyclerVAdapter(ArrayList<Newsdetail> dataSet, Context context) {
        DataSet = dataSet;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView NewTitle;
        private ImageView Image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            NewTitle = itemView.findViewById(R.id.titleNews);
            Image = itemView.findViewById(R.id.imageNews);
        }
    }

    @NonNull
    @Override
    public NewsRecyclerVAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view to define the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Replace the contents of a view
        Newsdetail CurrentArticle = DataSet.get(position);
        holder.NewTitle.setText(CurrentArticle.getTitle());
        Picasso.get().load(CurrentArticle.getUrlToImage()).into(holder.Image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Newsdetail.class);
                intent.putExtra("NewsTitle",CurrentArticle.getTitle());
                intent.putExtra("NewsContent",CurrentArticle.getContent());
                intent.putExtra("NewsImage",CurrentArticle.getUrlToImage());
                intent.putExtra("NewsURL",CurrentArticle.getUrl());

                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return DataSet.size();
    }


}
