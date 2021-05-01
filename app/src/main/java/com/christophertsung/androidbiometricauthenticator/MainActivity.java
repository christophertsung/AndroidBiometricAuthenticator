package com.christophertsung.androidbiometricauthenticator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Context context = MainActivity.this;
    private CancellationSignal cancellationSignal;

    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {

        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              CharSequence errString) {
                Toast.makeText(context, "Error: " + errString, Toast.LENGTH_LONG).show();
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationHelp(int helpCode,
                                             CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }

            @Override
            public void onAuthenticationSucceeded(
                    BiometricPrompt.AuthenticationResult result) {
                Toast.makeText(context, "Authentication successful!", Toast.LENGTH_LONG).show();
                super.onAuthenticationSucceeded(result);
            }
        };
    }

    private CancellationSignal getCancellationSignal() {

        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                Toast.makeText(context, "Authentication cancelled.", Toast.LENGTH_LONG).show();
            }
        });
        return cancellationSignal;
    }

    public void authenticateUser(View view) {
        BiometricPrompt prompt = new BiometricPrompt.Builder(context)
                .setTitle("Android Biometric Authenticator")
                .setSubtitle("Please authenticate using your biometrics.")
                .setDescription("This app uses biometric authentication to protect your data.")
                .setNegativeButton("Cancel", this.getMainExecutor(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(context, "Authentication cancelled.", Toast.LENGTH_LONG).show();
                            }
                        })
                .build();

        prompt.authenticate(getCancellationSignal(), getMainExecutor(), getAuthenticationCallback());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = findViewById(R.id.main_text);
        final Button promptButton = findViewById(R.id.bio_prompt);

        promptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                authenticateUser(v);
            }
        });
    }


}