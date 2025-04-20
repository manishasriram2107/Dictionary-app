package com.example.dictionary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log; // ✅ Import Log
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private static final String TAG = "LeaderboardActivity"; // ✅ Log tag

    private ListView leaderboardListView;
    private DatabaseHelper dbHelper;
    private ArrayList<String> wordsList;
    private ArrayAdapter<String> adapter;

    private boolean isFavoritesTab = true; // To track current tab

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        leaderboardListView = findViewById(R.id.leaderboardListView);
        Button showFavoritesBtn = findViewById(R.id.showFavoritesBtn);
        Button showLearnedBtn = findViewById(R.id.showLearnedBtn);

        dbHelper = new DatabaseHelper(this);

        // Load Favorites by default
        loadFavoriteWords();

        showFavoritesBtn.setOnClickListener(v -> {
            isFavoritesTab = true;
            loadFavoriteWords();
        });

        showLearnedBtn.setOnClickListener(v -> {
            isFavoritesTab = false;
            loadLearnedWords();
        });

        leaderboardListView.setOnItemClickListener((parent, view, position, id) -> {
            String word = wordsList.get(position);

            if (isFavoritesTab) {
                dbHelper.markAsLearned(word);
                Toast.makeText(this, "Marked as learned!", Toast.LENGTH_SHORT).show();
                loadFavoriteWords(); // Optional: refresh list
            } else {
                showEditDialog(word);
            }
        });

        leaderboardListView.setOnItemLongClickListener((parent, view, position, id) -> {
            deleteWord(wordsList.get(position));
            return true;
        });
    }

    private void loadFavoriteWords() {
        wordsList = new ArrayList<>();

        try (Cursor cursor = dbHelper.getAllFavorites()) {
            while (cursor.moveToNext()) {
                wordsList.add(cursor.getString(0));
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsList);
        leaderboardListView.setAdapter(adapter);
    }

    private void loadLearnedWords() {
        wordsList = new ArrayList<>();

        try (Cursor cursor = dbHelper.getLearnedWords()) {
            while (cursor.moveToNext()) {
                wordsList.add(cursor.getString(0));
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsList);
        leaderboardListView.setAdapter(adapter);
    }

    private void deleteWord(String word) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            if (isFavoritesTab) {
                db.delete("favorite_words", "word = ?", new String[]{word});
            } else {
                ContentValues values = new ContentValues();
                values.put("is_learned", 0);
                db.update("words", values, "word = ?", new String[]{word});
            }

            wordsList.remove(word);
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "Word removed successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Error deleting word", e); // ✅ Replaced printStackTrace
            Toast.makeText(this, "Failed to remove word: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showEditDialog(String oldWord) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Edit Word");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_edit_word, null);
        builder.setView(customLayout);

        final EditText editWord = customLayout.findViewById(R.id.editWord);
        editWord.setText(oldWord);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newWord = editWord.getText().toString().trim();

            if (!newWord.isEmpty()) {
                dbHelper.updateLearnedWord(oldWord, newWord);
                Toast.makeText(this, "Word updated!", Toast.LENGTH_SHORT).show();
                loadLearnedWords();
            } else {
                Toast.makeText(this, "Word can't be empty!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
