package com.stiven.deptsboard.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stiven.deptsboard.R;
import com.stiven.deptsboard.view.fragment.BorrowFragment;
import com.stiven.deptsboard.view.fragment.LendFragment;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "MainActivity";
    private Button btnSignOut;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showToolbar("Depts Board", true);

        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        nav = (BottomNavigationView) findViewById(R.id.navigation);

        initialize();

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

    }

    private void selectFragment (@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.action_borrow:
                BorrowFragment borrowFragment = new BorrowFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, borrowFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null).commit();
                Toast.makeText(MainActivity.this, "Borrow", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_lend:
                LendFragment lendFragment = new LendFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, lendFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null).commit();
                Toast.makeText(MainActivity.this, "Lend", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    TextView tvUserData = (TextView) findViewById(R.id.tvUserData);
                    tvUserData.setText("UserName: " + firebaseUser.getDisplayName() + " Email: " + firebaseUser.getEmail());
                } else {
                    Log.w(TAG, "onAuthStateChanged - signed_out");
                }
            }
        };

        //Inicialización de Google Account
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void signOut(){
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()){
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "Error in Google Sign Out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
