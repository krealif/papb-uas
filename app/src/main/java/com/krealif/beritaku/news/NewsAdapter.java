package com.krealif.beritaku.news;

import static android.content.Context.MODE_PRIVATE;

import static com.krealif.beritaku.auth.LoginActivity.USERNAME;
import static com.krealif.beritaku.auth.LoginActivity.sharedPrefFile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.krealif.beritaku.R;
import com.krealif.beritaku.database.NewsFirebase;
import com.krealif.beritaku.model.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final List<News> newsList;
    private final LayoutInflater inflater;
    public static SharedPreferences sharedPref;
    private String author;

    public NewsAdapter(Context context, List<News> newsList) {
        this.newsList = newsList;
        this.inflater = LayoutInflater.from(context);

        sharedPref = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        this.author = sharedPref.getString(USERNAME, "");
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        final News news = newsList.get(position);
        int imgId = holder.itemView.getResources().getIdentifier("placeholder", "drawable", inflater.getContext().getPackageName());
        holder.txtTitle.setText(news.getTitle());
        holder.txtDate.setText(news.getPublished());
        holder.imgNews.setImageResource(imgId);

        Log.d("DEBU1", author);
        Log.d("DEBUG2", news.getAuthor());

        if (!author.equals(news.getAuthor())) {
            Log.d("DEBUG2", String.valueOf(!author.equals(news.getAuthor())));
            holder.btnEdit.setVisibility(View.GONE);
            holder.spc1.setVisibility(View.GONE);
            holder.btnRemove.setVisibility(View.GONE);
            holder.spc2.setVisibility(View.GONE);
        } else {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.spc1.setVisibility(View.VISIBLE);
            holder.btnRemove.setVisibility(View.VISIBLE);
            holder.spc2.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle, txtDate;
        ImageView imgNews;
        Button btnBookmark, btnEdit, btnRemove;
        Space spc1, spc2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNews = itemView.findViewById(R.id.image_news);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtDate = itemView.findViewById(R.id.txt_date);

            btnBookmark = itemView.findViewById(R.id.btn_bookmark);
            spc1 = itemView.findViewById(R.id.spc1);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            spc2 = itemView.findViewById(R.id.spc2);
            btnRemove = itemView.findViewById(R.id.btn_remove);

            itemView.setOnClickListener(this);

            btnBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    News news = newsList.get(getAdapterPosition());
                    NewsActivity.insertData(news);
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    News news = newsList.get(getAdapterPosition());
                    Intent intent = new Intent(view.getContext(), FormNewsActivity.class);
                    intent.putExtra("MODE", "MODE_EDIT");
                    intent.putExtra("id", news.getId());
                    intent.putExtra("title", news.getTitle());
                    intent.putExtra("category", news.getCategory());
                    intent.putExtra("published", news.getPublished());
                    intent.putExtra("ageRating", news.getAgeRating());
                    intent.putExtra("body", news.getBody());
                    view.getContext().startActivity(intent);
                }
            });

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    News news = newsList.get(getAdapterPosition());
                    NewsFirebase.removeNews(news.getId());
                    Toast.makeText(view.getContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    NewsFirebase.getAllNewsByCategory(NewsActivity.categorySelected);
                }
            });
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
