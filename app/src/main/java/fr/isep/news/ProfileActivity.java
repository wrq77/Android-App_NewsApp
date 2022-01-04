package fr.isep.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


/*
 TODO
  1. update user's profile(username and email)
  2. show the category and manage it
  3. maybe reset the password
 */

public class ProfileActivity extends AppCompatActivity {

    private Button logout;

    private ImageView ClickToHomePage;

    private TextInputLayout ProfileUserName, ProfileEmail;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilemanagement);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();


        ClickToHomePage = findViewById(R.id.home);
        ClickToHomePage.setOnClickListener(this::ClicktoHomePage);

        logout = findViewById(R.id.LogOut_button);
        logout.setOnClickListener(this::LogOut);

        ProfileUserName = findViewById(R.id.ProfileUserName);
        ProfileEmail = findViewById(R.id.ProfileEmail);
        showUserData();

    }

    private void showUserData() {
        DocumentReference documentReference = db.collection("user").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                ProfileUserName.getEditText().setText(documentSnapshot.getString("userName"));
                ProfileEmail.getEditText().setText(documentSnapshot.getString("email"));
            }
        });

    }

    private void LogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void ClicktoHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
