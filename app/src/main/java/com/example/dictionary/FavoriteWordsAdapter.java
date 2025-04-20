package com.example.dictionary;  // No spaces, typos, or extra dots!

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dictionary.R;

import java.util.List;

public class FavoriteWordsAdapter extends RecyclerView.Adapter<FavoriteWordsAdapter.ViewHolder> {

    private List<String> favoriteWords;

    public FavoriteWordsAdapter(List<String> favoriteWords) {
        this.favoriteWords = favoriteWords;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_word, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.wordTextView.setText(favoriteWords.get(position));
    }

    @Override
    public int getItemCount() {
        return favoriteWords.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
        }
    }
}
