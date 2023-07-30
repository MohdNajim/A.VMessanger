package com.example.avmessanger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Ragistration extends AppCompatActivity {

    TextView longinbut;
    EditText rg_username,rg_passward,rg_email;
    Button rg_signup;
    CircleImageView rg_profileImg;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri;
    String emailpattern="^[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*$";
    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ragistration);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Establishing your account");
        progressDialog.setCancelable(false);

        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();

        longinbut =findViewById(R.id.rglongin);
        rg_username=findViewById(R.id.rgusername);
        rg_passward=findViewById(R.id.rgpassward);
        rg_email=findViewById(R.id.rgemail);
        rg_signup=findViewById(R.id.rgsignup);
        rg_profileImg =findViewById(R.id.profilerg0);


        longinbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Ragistration.this,Login.class);
                startActivity(intent);
                finish();

            }
        });


        rg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String namee=rg_username.getText().toString();
                String emaill=rg_email.getText().toString();
                String passward=rg_passward.getText().toString();
                String status="Hey I'm Using This Application";

                if (TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(passward)){
                    progressDialog.dismiss();
                    Toast.makeText(Ragistration.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                } else if (!emaill.matches(emailpattern)) {
                    progressDialog.dismiss();
                    rg_email.setError("Type A Valid Email Here");

                } else if (passward.length()<6) {
                    progressDialog.dismiss();
                    rg_passward.setError("Passward Must Be 6 Charecter Or More");
                }else {
                    auth.createUserWithEmailAndPassword(emaill,passward).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                if (imageURI != null) {
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri = uri.toString();
                                                        Users users = new Users(id, namee, emaill, passward, imageuri, status);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {
                                                                    progressDialog.show();
                                                                    Intent intent = new Intent(Ragistration.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(Ragistration.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else{
                                    String status="Hey I'm Using This Application";
                                    imageuri="https://firebasestorage.googleapis.com/v0/b/a-vmessanger.appspot.com/o/chat%20man.jpeg?alt=media&token=4e90b463-990b-460c-95da-f597302f81f7";

                                    Users users=new Users(id, namee, emaill,passward,imageuri,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(Ragistration.this, Login.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(Ragistration.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                }

                            }else {
                                Toast.makeText(Ragistration.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });


        rg_profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if (data!=null){
                imageURI=data.getData();
                rg_profileImg.setImageURI(imageURI);
            }
        }
    }
}