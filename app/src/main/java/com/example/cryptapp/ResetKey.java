package com.example.cryptapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetKey extends AppCompatActivity {

    EditText oldPassword, newPassword, confirmPassword;
    String oldPass, newPass, confirmPass;
    Button save;
    String password = AES.key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_key);

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        save = findViewById(R.id.saveButton);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPass = oldPassword.getText().toString();
                newPass = newPassword.getText().toString();
                confirmPass = confirmPassword.getText().toString();

                if(oldPass.equals(password)){

                    if(newPass.equals(confirmPass)){
                        AES.key = confirmPass;
                        Toast.makeText(ResetKey.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(ResetKey.this, "Confirm Password does not match with New Password", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ResetKey.this, "Old Password does not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}