package com.example.avmessanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class Login extends AppCompatActivity {

    TextView logsignup;
   Button button;
   EditText email,password;
   FirebaseAuth auth;

   String emailpattern="^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*$";
   android.app.ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);

        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        button=findViewById(R.id.Logbutton);
        email=findViewById(R.id.editTextLogEmail);
        password=findViewById(R.id.editTextLogPassword);
        logsignup=findViewById(R.id.logsignup);

        logsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Ragistration.class);
                startActivity(intent);
                finish();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Email=email.getText().toString();
                String pass=password.getText().toString();

                if ((TextUtils.isEmpty(Email))){
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Enter The Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pass)) {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Enter The Passward", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailpattern)) {
                    progressDialog.dismiss();
                    email.setError("Give Proper Email Address");

                } else if (password.length()<6) {
                    progressDialog.dismiss();
                    password.setError("More Than six charecter");

                    Toast.makeText(Login.this, "Passward needs to be longer than six charecters", Toast.LENGTH_SHORT).show();

                }else {
                    auth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                try {
                                    SharedPreferences sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putBoolean("taju",true);
                                    editor.apply();
                                    Intent intent=new Intent(Login.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }catch (Exception e){
                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });


    }
}