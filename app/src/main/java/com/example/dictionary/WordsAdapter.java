package com.example.dictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WordsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> wordsList;

    public WordsAdapter(Context context, ArrayList<String> wordsList) {
        this.context = context;
        this.wordsList = wordsList;
    }

    @Override
    public int getCount() {
        return wordsList.size();
    }

    @Override
    public Object getItem(int position) {
        return wordsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Inflate the custom layout for each list item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_word, null);
        }

        // Get the word at the given position
        String word = wordsList.get(position);

        // Set the word text
        TextView wordTextView = convertView.findViewById(R.id.wordTextView);
        wordTextView.setText(word);

        return convertView;
    }
}
