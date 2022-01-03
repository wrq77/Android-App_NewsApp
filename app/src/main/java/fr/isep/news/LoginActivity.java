package fr.isep.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity{

    private TextView Btnsignup;
    private EditText EnterEmail, EnterPassword;
    private Button BtnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Btnsignup = findViewById(R.id.Click_to_SignUp);
        Btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        EnterEmail = findViewById(R.id.EnterEmail);
        EnterPassword = findViewById(R.id.EnterPassword);

        BtnLogin = findViewById(R.id.Lbtn_login);


    }
}
