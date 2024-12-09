package com.example.Insurance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // declare variables
    TextView regis;
    EditText user_email, user_password;
    String email, password;
    Button btnLogin;
    private FirebaseAuth fAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize FirebaseAuth and SharedPreferences
        fAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Check if the user is already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

        // Access layout activity_main (login)
        setContentView(R.layout.activity_main);

        // Access the layout components
        regis = findViewById(R.id.go_regis);
        user_email = findViewById(R.id.email_login);
        user_password = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.btn_login);

        // Set click listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekLogin();
            }
        });

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    // Login method
    private void cekLogin() {
        // Get the user input
        email = user_email.getText().toString().trim();
        password = user_password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            user_email.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            user_password.setError("Password is required");
            return;
        }

        // Check the inputted user in the database
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = fAuth.getCurrentUser();
                            if (user != null && user.isEmailVerified()) {
                                // Set the isLoggedIn flag to true
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                // Email is not verified
                                Toast.makeText(MainActivity.this, "Please verify your email.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Login Failed, credentials not found", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
