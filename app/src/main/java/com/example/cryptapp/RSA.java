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

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import javax.crypto.Cipher;

public class RSA extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 100;
    EditText inputText;
    TextView outputText;
    Button encryptButton, decryptButton, sendButton, clearButton;
    String inputString, outputString;

    KeyPair kp = getKeyPair();

    PublicKey publicKey = kp.getPublic();
    byte publicKeyBytes[] = publicKey.getEncoded();
    String publicKeyBytesBase64 = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));

    PrivateKey privateKey = kp.getPrivate();
    byte privateKeyBytes[] = privateKey.getEncoded();
    String privateKeyBytesBase64 = new String(Base64.encode(privateKeyBytes, Base64.DEFAULT));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa);

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
                    outputString = encrypt(inputString, publicKeyBytesBase64);
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
                    outputString = decrypt(inputString, privateKeyBytesBase64);
                    outputText.setText(outputString);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outputString.length() > 0) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_INTENT, outputString);
                    intent.setType("text/plain");
                    startActivity(intent);
                } else {
                    Toast.makeText(RSA.this, "No output" , Toast.LENGTH_SHORT).show();
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
    private KeyPair getKeyPair(){
        KeyPair kp = null;
        try {
            KeyPairGenerator kgp = KeyPairGenerator.getInstance("RSA");
            kgp.initialize(2048);
            kp = kgp.generateKeyPair();
        } catch (Exception e){
            e.printStackTrace();
        }

        return kp;
    }

    private String encrypt(String dataToEncrypt, String publicKey){
        String encryptedString = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePublic(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(dataToEncrypt.getBytes("UTF-8"));
            encryptedString = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString.replaceAll("(\\r|\\n)", "");
    }

    private String decrypt(String dataToDecrypt, String privateKey){
        String decryptedString = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePrivate(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encryptedBytes = Base64.decode(dataToDecrypt, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (Exception e){
            e.printStackTrace();
        }
        return decryptedString;
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