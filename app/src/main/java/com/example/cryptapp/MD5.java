package com.example.cryptapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.List;
import java.util.Locale;

public class MD5 extends AppCompatActivity {

    String password = "";
    EditText inputText;
    TextView outputText;
    Button hashButton, clearButton;
    private static final int SPEECH_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md5);

        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.outputText);
        hashButton = findViewById(R.id.hashButton);
        clearButton = findViewById(R.id.clearButton);

        hashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = inputText.getText().toString();
                if (password.length() > 0) {
                    outputText.setText(generatePassword(password));
                } else {
                    Toast.makeText(MD5.this, "Password is Empty", Toast.LENGTH_LONG).show();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText.setText("");
                outputText.setText("");
            }
        });
    }

    private String generatePassword(String password) {

        String generatedPassword = null;
        try {
            //Returns a MessageDigest object that implements the specified digest algorithm.
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Updates the digest using the specified array of bytes
            md.update(password.getBytes());
            //Completes the hash computation by performing final operations such as padding.
            byte digest[] = md.digest();
            //Generates the hash string
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                str.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = str.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }


    // Create an intent that can start the Speech Recognizer activity
    public void SpeechRecognizer(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // This starts the activity and populates the intent with the speech text.
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            inputText.setText(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}