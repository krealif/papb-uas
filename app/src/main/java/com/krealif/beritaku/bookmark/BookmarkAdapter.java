package com.krealif.beritaku.bookmark;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krealif.beritaku.R;
import com.krealif.beritaku.model.News;
import com.krealif.beritaku.news.NewsDetailActivity;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder>{
    private final List<News> newsList;
    private final LayoutInflater inflater;

    public BookmarkAdapter(Context context, List<News> newsList) {
        this.newsList = newsList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bookmark, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder holder, int position) {
        final News news = newsList.get(position);
        int imgId = holder.itemView.getResources().getIdentifier("placeholder", "drawable", inflater.getContext().getPackageName());
        holder.txtTitle.setText(news.getTitle());
        holder.txtDate.setText(news.getPublished());
        holder.imgNews.setImageResource(imgId);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle, txtDate;
        ImageView imgNews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNews = itemView.findViewById(R.id.image_news);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDate = itemView.findViewById(R.id.txt_date);

            itemView.findViewById(R.id.btn_unbookmark).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    News news = newsList.get(getAdapterPosition());
                    BookmarkActivity.deleteData(news);
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            News news = newsList.get(position);
            Intent intent = new Intent(view.getContext(), NewsDetailActivity.class);
            intent.putExtra("title", news.getTitle());
            intent.putExtra("published", news.getPublished());
            intent.putExtra("ageRating", String.valueOf(news.getAgeRating()));
            intent.putExtra("body", news.getBody());
            view.getContext().startActivity(intent);
        }
    }
}


