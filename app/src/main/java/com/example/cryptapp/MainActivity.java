package com.example.cryptapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Aes(View view){
        Intent intent = new Intent(MainActivity.this, AES.class);
        startActivity(intent);
    }

    public void Des(View view){
        Intent intent = new Intent(MainActivity.this, DES.class);
        startActivity(intent);
    }

    public void Rsa(View view){
        Intent intent = new Intent(MainActivity.this, RSA.class);
        startActivity(intent);
    }

    public void Md5(View view){
        Intent intent = new Intent(MainActivity.this, MD5.class);
        startActivity(intent);
    }
}