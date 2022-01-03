package fr.isep.news;


import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*TODO
   1. Create the user object to optimize the code
   2. Separate the db code
 */

public class SignupActivity extends AppCompatActivity {

    private TextView Btnlogin;
    private EditText EditEmail, EditUsername, EditPassword, EditPasswordAgain;
    private Button BtnSignup;

    public static Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    //"\\w+@(\\w+.)+[a-z]{2,3}"

    private FirebaseAuth mAuth;

    //the db
    private FirebaseFirestore db;

    String userId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //check to see if the user is currently signed in.
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

        EditEmail = findViewById(R.id.EnterEmail);
        EditUsername = findViewById(R.id.EnterUsername);
        EditPassword = findViewById(R.id.EnterPassword);
        EditPasswordAgain = findViewById(R.id.EnterPasswordAgain);

        Btnlogin = findViewById(R.id.Click_to_login);
        Btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        BtnSignup = findViewById(R.id.Lbtn_signup);
        BtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    private void signup() {
        String email = EditEmail.getText().toString();
        String userName = EditUsername.getText().toString();
        String password = EditPassword.getText().toString();
        String passwordAgain = EditPasswordAgain.getText().toString();

        if(!isEmail(email) || email.length() > 31){
            Toast.makeText(this, "Email format error", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(email) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordAgain)){
            Toast.makeText(this, "Please fill in all the boxes",Toast.LENGTH_LONG).show();
        }else if(password.length()<=6){
            Toast.makeText(this, "Password Must be >= 6 Characters",Toast.LENGTH_LONG).show();
        }else if(!password.equals(passwordAgain)){
            Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(SignupActivity.this,"Sign up Success!",Toast.LENGTH_SHORT).show();

                                userId = mAuth.getCurrentUser().getUid();
                                //automatically create the collection
                                DocumentReference documentReference = db.collection("user").document(userId);
                                Map<String, Object> user = new HashMap<>();
                                user.put("userName", userName);
                                user.put("email", email);

                                // insert user
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "user is added with ID: " + userId);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error adding document" + e.toString());
                                    }
                                });

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignupActivity.this, "Error..."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

        }
    }

    public static boolean isEmail(String email){
        if (null==email || "".equals(email))
            return false;
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
