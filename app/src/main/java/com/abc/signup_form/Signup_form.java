package com.abc.signup_form;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup_form extends AppCompatActivity {

    EditText txt_fullName,txt_userName,txt_email,txt_password,txt_confirmPassword;
    Button btn_register;
    RadioButton radioGenderMale,radioGenderFemale;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    String fullName="",userName="",
            email="",password="",confirmPassword="";
    String gender="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);
        getSupportActionBar().setTitle("Signup Form");

        //casting views
        txt_fullName =  (EditText)findViewById(R.id.txt_full_name);
        txt_userName =  (EditText)findViewById(R.id.txt_username);
        txt_email = (EditText)findViewById(R.id.txt_email);
        txt_password = (EditText)findViewById(R.id.text_password);
        txt_confirmPassword = (EditText)findViewById(R.id.text_confirm_password);
        btn_register = (Button)findViewById(R.id.btn_register);
        radioGenderMale = (RadioButton)findViewById(R.id.radio_male);
        radioGenderFemale = (RadioButton)findViewById(R.id.radio_female);
        databaseReference = FirebaseDatabase.getInstance().getReference("Student");
        firebaseAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fullName = txt_fullName.getText().toString();
                userName = txt_userName.getText().toString();
                email = txt_email.getText().toString();
                password = txt_password.getText().toString();
                confirmPassword = txt_confirmPassword.getText().toString();
                if (fullName.equals("")) {
                    Toast.makeText(Signup_form.this, "Please Enter Full Name", Toast.LENGTH_LONG).show();
                } else if (userName.equals("")) {
                    Toast.makeText(Signup_form.this, "Please Enter User Name", Toast.LENGTH_LONG).show();
                } else if (email.equals("")) {
                    Toast.makeText(Signup_form.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                } else if (password.equals("")) {
                    Toast.makeText(Signup_form.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else if (confirmPassword.equals("")) {
                    Toast.makeText(Signup_form.this, "Please Enter confirm Password", Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(Signup_form.this, "Password and Confirm Password are not same!", Toast.LENGTH_LONG).show();
                }
                else {

                    if(radioGenderFemale.isChecked())
                        gender = "Female";
                    else
                        gender = "Male";

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup_form.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Student information = new Student(fullName, userName, email, gender);
                                    FirebaseDatabase.getInstance().getReference("Student")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(information).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Signup_form.this, "Registration Complete", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                        }
                                    });
                                } else {
                                    Toast.makeText(Signup_form.this, "Registration Failed", Toast.LENGTH_LONG).show();
                                }

                                // ...
                            }
                        });
            }
            }
        });

    }
}
