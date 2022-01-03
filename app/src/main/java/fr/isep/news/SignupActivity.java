package fr.isep.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private TextView Btnlogin;
    private EditText EditEmail, EditUsername, EditPassword, EditPasswordAgain;
    private Button BtnSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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
            }
        });

        BtnSignup = findViewById(R.id.Lbtn_signup);
    }
}
