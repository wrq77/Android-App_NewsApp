package fr.isep.news;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import fr.isep.news.Model.Category;
import fr.isep.news.databinding.ActivitySelectcategoryBinding;

public class CategoryActivity  extends AppCompatActivity {

    private ActivitySelectcategoryBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String userId,CategoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcategory);

        binding = ActivitySelectcategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        binding.btnCategory.setOnClickListener(this::Selcategory);

        showCategory();

    }

    private void showCategory() {
        //获取category数据库内容
        DocumentReference documentReference = db.collection("Category").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e == null && documentSnapshot.exists()) {
                    CategoryName = documentSnapshot.getString("categoryName");
                    //根据,分割category字符串

                    String[] as = CategoryName.split(",");
                    for (int i = 0; i < as.length; i++) {

                        if (as[i].equals("Business")) {
                            binding.business.setChecked(true);
                        }
                        if (as[i].equals("Entertainment")) {
                            binding.entertainment.setChecked(true);
                        }
                        if (as[i].equals("General")) {
                            binding.general.setChecked(true);
                        }
                        if (as[i].equals("Health")) {
                            binding.health.setChecked(true);
                        }
                        if (as[i].equals("Science")) {
                            binding.science.setChecked(true);
                        }
                        if (as[i].equals("Sports")) {
                            binding.sports.setChecked(true);
                        }
                        if (as[i].equals("Technology")) {
                            binding.technology.setChecked(true);
                        }
                    }
                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });
    }


    private void Selcategory(View v) {


        /**【所有复选框id数组】**/
        int chk_id[] = {R.id.business, R.id.entertainment, R.id.general, R.id.health,
                R.id.science, R.id.sports, R.id.technology};


        /**【循环为所有复选框注册监听事件】**/
        String category = "";
        for (int id : chk_id) {
            CheckBox chk = findViewById(id);
            if (chk.isChecked()) {
                if(category.equals("")){
                    category = chk.getText().toString().trim();
                }
                else {
                    category = category + "," + chk.getText().toString().trim();
                }
            }
        }

        DocumentReference documentReference = db.collection("Category").document(userId);
        Category categoryObj = new Category(category);


        // insert category
        documentReference.set(categoryObj).addOnSuccessListener(new OnSuccessListener<Void>() {
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


        Toast.makeText(CategoryActivity.this, "select success", Toast.LENGTH_SHORT).show();


    }

}
