package com.example.cryptapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class DES extends AppCompatActivity {

    EditText inputText;
    TextView outputText;
    Button encryptButton, decryptButton, sendButton, clearButton;
    protected static String key = "qwertykey";
    public static String ALGO = "DES/ECB/PKCS5Padding";
    String outputString = "";
    String inputString;
    private static final int SPEECH_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des);

        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.outputText);
        encryptButton = findViewById(R.id.encryptButton);
        decryptButton = findViewById(R.id.decryptButton);
        sendButton = findViewById(R.id.sendButton);
        clearButton = findViewById(R.id.clearButton);

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    inputString = inputText.getText().toString();
                    outputString = encrypt(inputString, key);
                    outputText.setText(outputString);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    inputString = inputText.getText().toString();
                    outputString = decrypt(inputString, key);
                    outputText.setText(outputString);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(outputString.length() > 0){
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, outputString);
                    intent.setType("text/plain");
                    startActivity(intent);

                } else {
                    Toast.makeText(DES.this, "No Output Text", Toast.LENGTH_SHORT).show();
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

    private SecretKey getSecretKey(String secretKey) throws Exception{
        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);
        return key;
    }

    private String encrypt(String data, String secretKey) throws Exception{
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(secretKey));
        byte encrypted[] = cipher.doFinal(data.getBytes("UTF-8"));
        String encryptedValue = Base64.encodeToString(encrypted, Base64.DEFAULT);
        return encryptedValue;
    }

    private String decrypt(String encryptedText, String secretKey) throws Exception{
        byte message[] = Base64.decode(encryptedText, Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secretKey));
        byte decrypted[] = cipher.doFinal(message);
        return new String(decrypted, "UTF-8");
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