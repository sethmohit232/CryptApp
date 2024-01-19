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
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES extends AppCompatActivity {

    EditText inputText;
    TextView outputText;
    Button encryptButton, decryptButton, clearButton, sendButton, resetButton;
    protected static String key = "abcdefg";
    private static final int SPEECH_REQUEST_CODE = 100;
    String outputString = "";
    String inputString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes);

        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.outputText);
        encryptButton = findViewById(R.id.encryptButton);
        decryptButton = findViewById(R.id.decryptButton);
        clearButton = findViewById(R.id.clearButton);
        sendButton = findViewById(R.id.sendButton);
        resetButton = findViewById(R.id.resetButton);

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

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText.setText("");
                outputText.setText("");
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
                    Toast.makeText(AES.this, "No Output String", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AES.this, ResetKey.class);
                startActivity(intent);
            }
        });
    }
    private SecretKeySpec generateKey(String key) throws Exception{
        //Returns a MessageDigest object that implements the specified digest algorithm.
        MessageDigest md = MessageDigest.getInstance("MD5");
        //Updates the digest using the specified array of bytes
        md.update(key.getBytes());
        //Completes the hash computation by performing final operations such as padding.
        byte digest[] = md.digest();
        SecretKeySpec EncryptedKey = new SecretKeySpec(digest, "AES");
        return EncryptedKey;

    }
    private String encrypt(String text, String key) throws Exception{
            SecretKeySpec encryptedKey = generateKey(key);
            //Returns a Cipher object that implements the specified transformation.
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            //Initializes this cipher with a key
            cipher.init(Cipher.ENCRYPT_MODE, encryptedKey);
            //Finishes a multiple-part encryption or decryption operation, depending on how this cipher was initialized.
            byte encrypted[] = cipher.doFinal(text.getBytes());
            //Base64-encode the given data and return a newly allocated String with the result.
            String encryptedValue = Base64.encodeToString(encrypted, Base64.DEFAULT);
            return encryptedValue;
    }

    private String decrypt(String text, String key) throws Exception{
        SecretKeySpec encryptedKey = generateKey(key);
        //Returns a Cipher object that implements the specified transformation.
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        //Initializes this cipher with a key
        cipher.init(Cipher.DECRYPT_MODE, encryptedKey);
        //Undo the Base64-encode
        byte decoded[] = Base64.decode(text, Base64.DEFAULT);
        //Finishes a multiple-part encryption or decryption operation, depending on how this cipher was initialized.
        byte decrypt[] = cipher.doFinal(decoded);
        //Converting to String
        String decryptedValue = new String(decrypt);

        return decryptedValue;
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