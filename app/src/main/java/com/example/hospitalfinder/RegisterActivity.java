package com.example.hospitalfinder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText e1, e2, e3;
    FirebaseAuth mAuth;
    DatabaseReference databaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
        databaseUser= FirebaseDatabase.getInstance("https://group-project-67a77-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                      .getReference("users");
        e1 = (EditText)findViewById(R.id.editText);
        e2 = (EditText)findViewById(R.id.editText1);
        e3 = (EditText)findViewById(R.id.editText2);
        mAuth = FirebaseAuth.getInstance();
    }
    public void createUser(View v){
        if(e1.getText().toString().equals("") && e2.getText().toString().equals("")&& e3.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Blank not allowed", Toast.LENGTH_SHORT).show();
        }else{
            String email = e1.getText().toString();
            String name = e2.getText().toString();
            String password = e3.getText().toString();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                addUser(task.getResult().getUser().getUid(),name);
                                Toast.makeText(getApplicationContext(),"Account registered ",Toast.LENGTH_SHORT).show();
                                finish();
                                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Account fail to be register",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void addUser(String id,String name){
        if (!TextUtils.isEmpty(name)) {
            User user = new User(name.trim());
            databaseUser.child(id).setValue(user);
        }
    }
}

