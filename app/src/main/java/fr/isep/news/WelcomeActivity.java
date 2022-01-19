package fr.isep.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import fr.isep.news.databinding.ActivityWelcomeBinding;


public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;

    private int SPLASH_TIME = 5000; //5 seconds

    //flag
    private final Message message;

    private final Thread thread;
    private final Handler handler;

    public WelcomeActivity() {

        message = new Message();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 1) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else if (message.what == 0) {
                    thread.interrupt();
                }
            }
        };

        thread = new Thread(this::run);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        thread.start();

        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.WbtnSkip.setOnClickListener(this::ClickToSkip);

    }

    private void run() {
        try {
            Thread.sleep(SPLASH_TIME);
            message.what = 1;
            handler.sendMessage(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void ClickToSkip(View v) {
        message.what = 0;
        handler.sendMessage(message);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
