package com.example.Insurance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    // declare component variables
    TextView log;
    EditText user_email, user_username, user_password, user_passconfirm;
    String email, username, password, passconfirm, userID;
    Button btnRegis;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // access the layout activity
        setContentView(R.layout.activity_register);

        // access the layout component
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        log = findViewById(R.id.go_login);

        // Method to go to the login page
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });

        // access the variables to component IDs
        user_email = findViewById(R.id.email_register);
        user_username = findViewById(R.id.username_register);
        user_password = findViewById(R.id.password_register);
        user_passconfirm = findViewById(R.id.passconf_register);
        btnRegis = findViewById(R.id.btn_register);
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    // Registration method
    private void register() {
        // get the user input
        email = user_email.getText().toString().trim();
        username = user_username.getText().toString().trim();
        password = user_password.getText().toString().trim();
        passconfirm = user_passconfirm.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            user_email.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            user_password.setError("Password is required");
            return;
        }

        if (TextUtils.isEmpty(passconfirm)) {
            user_passconfirm.setError("Password Confirmation is required");
            return;
        }

        // Check if the password is at least 6 characters long
        if (password.length() < 6) {
            user_password.setError("Password must be at least 6 characters long");
            return;
        }

        // Check if the password and password confirmation match
        if (!password.equals(passconfirm)) {
            user_password.setError("Passwords do not match");
            user_passconfirm.setError("Passwords do not match");
            return;
        }

        // Insert into the database
        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_LONG).show();
                            FirebaseUser user = fAuth.getCurrentUser();
                            if (user != null) {
                                sendEmailVerification(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Verification email sent. Please check your inbox.", Toast.LENGTH_LONG).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("UserID", userID);
                            userMap.put("Name", username);
                            userMap.put("Email", email);
                            documentReference.set(userMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "DocumentSnapshot successfully written!");
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("TAG", "Error writing document", e);
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
