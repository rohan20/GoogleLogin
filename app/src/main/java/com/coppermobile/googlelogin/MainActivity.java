package com.coppermobile.googlelogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity {

    SignInButton bSignIn;
    Button bSignOut;
    String signedInEmail;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        bSignIn = (SignInButton) findViewById(R.id.sign_in_button);
        if (bSignIn != null) {
            bSignIn.setSize(SignInButton.SIZE_STANDARD);
        }
        bSignIn.setScopes(gso.getScopeArray());

        bSignOut = (Button) findViewById(R.id.sign_out_button);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.sign_in_button:
                signIn();
//                        break;
//                }
            }
        });

        bSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    private void signIn() {
        Intent i = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(i, Constants.SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                Toast.makeText(MainActivity.this, "Signed out of " + signedInEmail, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();
            signedInEmail = account.getEmail();
            Toast.makeText(MainActivity.this, "Successful" + "\nName: " + account.getDisplayName() + "\nEmail: " + account.getEmail(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
        }

    }
}
