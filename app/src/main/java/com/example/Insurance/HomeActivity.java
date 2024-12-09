package com.example.Insurance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    // Declare component variables
    TextView username;
    ImageView field, room;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Access layout activity_home
        setContentView(R.layout.activity_home);

        // Find the component id in the layout
        username = findViewById(R.id.textUsername);
        field = findViewById(R.id.go_field);
        room = findViewById(R.id.go_room);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Get the user ID
        String userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        // Query Firestore to get user data
        Query query = fStore.collection("users").whereEqualTo("User ID", userID);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("Name");
                    if (name != null) {
                        username.setText("Hi " + name);
                    } else {
                        // Handle the case where "Name" is null
                        username.setText("Hi User"); // Set a default value
                    }
                }
            } else {
                // Handle the case where no data exists for the user
                username.setText("Hi User"); // Set a default value
            }
        });

        // Method to go to the field page
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SportActivity.class));
            }
        });

        // Method to go to room page
        room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Productshelf.class));
            }
        });
    }
}