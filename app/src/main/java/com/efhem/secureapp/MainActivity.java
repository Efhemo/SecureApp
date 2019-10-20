package com.efhem.secureapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    final static String TAG = MainActivity.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create a thread pool with a single thread//
        Executor newExecutor = Executors.newSingleThreadExecutor();

        FragmentActivity activity = this;
        //Start listening for authentication events//
        final BiometricPrompt myBiometricPrompt =
                new BiometricPrompt(activity, newExecutor, new BiometricPrompt.AuthenticationCallback() {

                    @Override
                    //onAuthenticationError is called when a fatal error occurrs
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {

                        } else {

                            //Print a message to Logcat//
                            Log.d(TAG, "An unrecoverable error occurred");
                        }
                    }

                    //onAuthenticationSucceeded is called when a fingerprint is matched successfully//
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);

                        //Print a message to Logcat//
                        Log.d(TAG, "Fingerprint recognised successfully");
                        runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Fingerprint recognised successfully", Toast.LENGTH_SHORT).show();
                            }
                        }));
                    }

                    //onAuthenticationFailed is called when the fingerprint doesn’t match//
                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();

                        //Print a message to Logcat//
                        Log.d(TAG, "Fingerprint not recognised");
                    }
                });

        //Create the BiometricPrompt instance//
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                //Add some text to the dialog//
                .setTitle("Title text goes here")
                .setSubtitle("Subtitle goes here")
                .setDescription("This is the description")
                .setNegativeButtonText("Cancel")
                //Build the dialog//
                .build();

        //Assign an onClickListener to the app’s “Authentication” button//
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBiometricPrompt.authenticate(promptInfo);
            }
        });

    }

}

    /*@TargetApi(Build.VERSION_CODES.P)
    private void displayBiometricPrompt(final BiometricCallback biometricCallback) {
        new BiometricPrompt.Builder(context)
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButton(negativeButtonText, context.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        biometricCallback.onAuthenticationCancelled();
                    }
                })
                .build();
    }*/
//}
