package com.example.dictionary;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class WordLearningActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TextView wordText, meaningText;
    private EditText newWordInput, newMeaningInput;
    private ListView wordsListView;
    private int wordsLearned = 0;
    private String currentWord = "", currentMeaning = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadTheme(); // Applies selected theme before view loads
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_learning);

        // UI Elements
        wordText = findViewById(R.id.wordText);
        meaningText = findViewById(R.id.meaningText);
        newWordInput = findViewById(R.id.newWordInput);
        newMeaningInput = findViewById(R.id.newMeaningInput);
   //     wordsListView = findViewById(R.id.wordsListView);
        Button btnNextWord = findViewById(R.id.btnNextWord);
        Button btnFavorite = findViewById(R.id.btnFavorite);
        Button btnAddWord = findViewById(R.id.btnAddWord);
        Button btnLeaderboard = findViewById(R.id.btnLeaderboard);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDeleteWord = findViewById(R.id.btnDeleteWord);

        dbHelper = new DatabaseHelper(this);

        showRandomWord();
    //    displayAllWords();

        btnNextWord.setOnClickListener(view -> {
            wordsLearned++;
            dbHelper.close(); // Ensure fresh DB instance
            showRandomWord();
        });

        btnDeleteWord.setOnClickListener(view -> {
            deleteCurrentWord();
            showRandomWord();
            updateWordCount(); // if you're using word count
        });



        btnFavorite.setOnClickListener(view -> addWordToFavorites());

        btnAddWord.setOnClickListener(view -> {
            String newWord = newWordInput.getText().toString();
            String newMeaning = newMeaningInput.getText().toString();

            if (!newWord.isEmpty() && !newMeaning.isEmpty()) {
                boolean added = dbHelper.addWord(newWord, newMeaning);
                if (added) {
                    Toast.makeText(WordLearningActivity.this, "Word Added!", Toast.LENGTH_SHORT).show();
                    newWordInput.setText("");
                    newMeaningInput.setText("");
                    updateWordCount();
                } else {
                    Toast.makeText(WordLearningActivity.this, "Word already exists!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(WordLearningActivity.this, "Please enter both word and meaning", Toast.LENGTH_SHORT).show();
            }
        });





        btnLeaderboard.setOnClickListener(view -> {
            Intent intent = new Intent(WordLearningActivity.this, LeaderboardActivity.class);
            intent.putExtra("wordsLearned", wordsLearned);
            intent.putExtra("favoriteWords", getFavoriteWords());
            startActivity(intent);
        });

        btnBack.setOnClickListener(view -> finish());
    }


    @SuppressLint("SetTextI18n")
    private void showRandomWord() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Get a random word from the updated database (after deletions)
        Cursor cursor = db.rawQuery(
                "SELECT word, meaning FROM words ORDER BY RANDOM() LIMIT 1",
                null
        );

        if (cursor.moveToFirst()) {
            currentWord = cursor.getString(0);
            currentMeaning = cursor.getString(1);
            wordText.setText(currentWord);
            meaningText.setText(currentMeaning);
        } else {
            wordText.setText("No words found");
            meaningText.setText("Please add new words!");
        }

        cursor.close();
        db.close();  // Ensure DB is properly closed to avoid stale reads
    }


    private void addWordToFavorites() {
        if (!currentWord.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("INSERT INTO favorite_words (word, meaning) VALUES (?, ?)",
                    new Object[]{currentWord, currentMeaning});
            db.close();
            Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No word to add!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewWord() {
        String newWord = newWordInput.getText().toString().trim();
        String newMeaning = newMeaningInput.getText().toString().trim();

        if (!newWord.isEmpty() && !newMeaning.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("word", newWord);
            values.put("meaning", newMeaning);
            db.insert("words", null, values);
            db.close();

            Toast.makeText(this, "Word added successfully!", Toast.LENGTH_SHORT).show();
            newWordInput.setText("");
            newMeaningInput.setText("");
           // displayAllWords();
        } else {
            Toast.makeText(this, "Please enter both word and meaning!", Toast.LENGTH_SHORT).show();
        }
    }



    private ArrayList<String> getFavoriteWords() {
        ArrayList<String> favoriteWords = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT word FROM favorite_words", null);

        while (cursor.moveToNext()) {
            favoriteWords.add(cursor.getString(0));
        }
        cursor.close();
        return favoriteWords;
    }

    private void loadTheme() {
        SharedPreferences preferences = getSharedPreferences("AppTheme", MODE_PRIVATE);
        String themeName = preferences.getString("SelectedTheme", "Theme.Dictionary");

        Log.d("ThemeDebug", "Loaded theme in WordLearningActivity: " + themeName);

        switch (themeName) {
            case "Theme.Dictionary.Dark":
                setTheme(R.style.Theme_Dictionary_Dark);
                break;
            case "Theme.Dictionary.Blue":
                setTheme(R.style.Theme_Dictionary_Blue);
                break;
            case "Theme.Dictionary.Green":
                setTheme(R.style.Theme_Dictionary_Green);
                break;
            default:
                setTheme(R.style.Theme_Dictionary); // fallback theme
                break;
        }
    }

    private void deleteWord(String wordToDelete) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete("words", "word = ?", new String[]{wordToDelete});
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "Word deleted successfully!", Toast.LENGTH_SHORT).show();
            showRandomWord(); // Refresh word after delete
        } else {
            Toast.makeText(this, "Word not found!", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateWordCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM words", null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            TextView wordCountText = findViewById(R.id.wordCountText);
            wordCountText.setText(getString(R.string.words_left, count));

        }
        cursor.close();
        db.close();
    }


    private void deleteCurrentWord() {
        if (!currentWord.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("words", "word = ?", new String[]{currentWord});
            db.close();
            Toast.makeText(this, "Word deleted!", Toast.LENGTH_SHORT).show();
        }
    }



}
