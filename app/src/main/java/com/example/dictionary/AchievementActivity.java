package com.example.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dictionary.MainActivity;
import com.example.dictionary.R;

public class AchievementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        TextView txtResult = findViewById(R.id.txtResult);
        Button btnExitLobby = findViewById(R.id.btnExitLobby);

        // Show the completion message
        txtResult.setText("Congratulations! 🎉 You have learned 10 words today!");

        btnExitLobby.setOnClickListener(view -> {
            Intent intent = new Intent(AchievementActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
