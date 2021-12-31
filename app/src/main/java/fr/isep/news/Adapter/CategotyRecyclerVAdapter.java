package fr.isep.news.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.isep.news.R;
import fr.isep.news.model.Category;

public class CategotyRecyclerVAdapter extends RecyclerView.Adapter<CategotyRecyclerVAdapter.ViewHolder>{
    private ArrayList<Category> DataSet;
    private Context context;
    private CategoryClickInterface categoryClickInterface;

    public CategotyRecyclerVAdapter(ArrayList<Category> dataSet, Context context, CategoryClickInterface categoryClickInterface) {
        DataSet = dataSet;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    public interface CategoryClickInterface{
        void onClickCategory(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView CategoryTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CategoryTextView.findViewById(R.id.CategoryItem);
        }
    }

    @NonNull
    @Override
    public CategotyRecyclerVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategotyRecyclerVAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
           Category CurentCategory = DataSet.get(position);
           holder.CategoryTextView.setText(CurentCategory.getCategoryName());

           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   categoryClickInterface.onClickCategory(position);
               }
           });
    }

    @Override
    public int getItemCount() {
        return DataSet.size();
    }

}
