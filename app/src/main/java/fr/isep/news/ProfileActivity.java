package fr.isep.news;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;



/*
 TODO
  1. show the category and manage it
  2. maybe reset the password
 */

public class ProfileActivity extends AppCompatActivity {

    private Button logout, update;

    private ImageView ClickToHomePage;

    private TextInputLayout ProfileUserName, ProfileEmail;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //a document location in a Firestore database
    private  DocumentReference documentReference;

    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilemanagement);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();
        documentReference = db.collection("user").document(userId);


        ClickToHomePage = findViewById(R.id.home);
        ClickToHomePage.setOnClickListener(this::ClicktoHomePage);

        logout = findViewById(R.id.LogOut_button);
        logout.setOnClickListener(this::LogOut);

        update = findViewById(R.id.Update_button);
        update.setOnClickListener(this::UpdateProfile);

        ProfileUserName = findViewById(R.id.ProfileUserName);
        ProfileEmail = findViewById(R.id.ProfileEmail);
        showUserData();

    }


    private void showUserData() {
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                ProfileUserName.getEditText().setText(documentSnapshot.getString("userName"));
                ProfileEmail.getEditText().setText(documentSnapshot.getString("email"));
            }
        });

    }

    private void UpdateProfile(View view) {
        final String email = ProfileEmail.getEditText().getText().toString().trim();
        mAuth.getCurrentUser().updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Map<String,Object> user = new HashMap<>();
                user.put("email",email);
                user.put("userName",ProfileUserName.getEditText().getText().toString());
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ProfileActivity.this, "Update Success!" , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating" + e.toString());
                Toast.makeText(ProfileActivity.this, "Error..."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }



    private void LogOut(View view) {
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void ClicktoHomePage(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
