package com.example.dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 4; // Increased due to schema change

    private static final String TABLE_WORDS = "words";
    private static final String TABLE_FAVORITES = "favorite_words";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_WORD = "word";
    private static final String COLUMN_MEANING = "meaning";
    private static final String COLUMN_LEARNED = "is_learned";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_WORDS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_WORD + " TEXT UNIQUE, "
                + COLUMN_MEANING + " TEXT, "
                + COLUMN_LEARNED + " INTEGER DEFAULT 0)";

        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_WORD + " TEXT UNIQUE, "
                + COLUMN_MEANING + " TEXT)";

        db.execSQL(CREATE_WORDS_TABLE);
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public boolean addWord(String word, String meaning) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (wordExists(word)) return false;

        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, word);
        values.put(COLUMN_MEANING, meaning);
        values.put(COLUMN_LEARNED, 0);

        long result = db.insert(TABLE_WORDS, null, values);
        return result != -1;
    }

    public boolean wordExists(String word) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_WORDS + " WHERE " + COLUMN_WORD + "=?", new String[]{word});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean addWordToFavorites(String word, String meaning) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORD, word);
        values.put(COLUMN_MEANING, meaning);
        long result = db.insert(TABLE_FAVORITES, null, values);
        return result != -1;
    }

    public Cursor getAllFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT word FROM " + TABLE_FAVORITES, null);
    }

    public void markAsLearned(String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LEARNED, 1);
        db.update(TABLE_WORDS, values, COLUMN_WORD + "=?", new String[]{word});
        db.close();
    }

    public Cursor getLearnedWords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT word FROM " + TABLE_WORDS + " WHERE " + COLUMN_LEARNED + " = 1", null);
    }
    public void updateLearnedWord(String oldWord, String newWord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word", newWord);

        // ✅ Fixed: use correct table name
        db.update("words", values, "word = ?", new String[]{oldWord});
    }




}
