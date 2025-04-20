package com.example.dictionary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditWordActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText editWordInput, editMeaningInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        dbHelper = new DatabaseHelper(this);

        editWordInput = findViewById(R.id.editWordInput);
        editMeaningInput = findViewById(R.id.editMeaningInput);

        String word = getIntent().getStringExtra("word");
        // Fetch the current meaning of the word
        String currentMeaning = getCurrentMeaning(word);

        // Set the values to the input fields
        editWordInput.setText(word);
        editMeaningInput.setText(currentMeaning);
    }

    private String getCurrentMeaning(String word) {
        // Fetch the current meaning of the word from the database
        String meaning = "";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT meaning FROM words WHERE word = ?", new String[]{word});
        if (cursor.moveToFirst()) {
            meaning = cursor.getString(0);
        }
        cursor.close();
        return meaning;
    }

    public void onSave(View view) {
        String updatedMeaning = editMeaningInput.getText().toString().trim();
        if (!updatedMeaning.isEmpty()) {
            String word = editWordInput.getText().toString().trim();
            updateWordInDatabase(word, updatedMeaning);
            Toast.makeText(this, "Word updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Meaning cannot be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateWordInDatabase(String word, String meaning) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("meaning", meaning);
        db.update("words", values, "word = ?", new String[]{word});
        db.close();
    }
}
