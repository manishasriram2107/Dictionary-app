package com.example.dictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dictionary.LoginActivity;

public class SignupActivity extends AppCompatActivity {

    EditText edtUsername, edtPassword;
    Button btnSignup, btnLogin;
    SQLiteHelper dbHelper;
    ImageView imgMascot;
    TextView textViewWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Init DB helper
        dbHelper = new SQLiteHelper(this);

        // Views
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin = findViewById(R.id.btnLogin);
        imgMascot = findViewById(R.id.imgMascot);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        // Animations
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        imgMascot.startAnimation(bounce);
        textViewWelcome.startAnimation(fadeIn);
        btnSignup.startAnimation(bounce);
        btnLogin.startAnimation(fadeIn);

        // Signup Logic
        btnSignup.setOnClickListener(view -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (dbHelper.insertUser(username, password)) {
                    Toast.makeText(SignupActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Redirect to Login
        btnLogin.setOnClickListener(view -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }
}
