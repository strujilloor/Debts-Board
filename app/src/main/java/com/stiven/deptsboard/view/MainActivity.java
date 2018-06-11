package com.stiven.deptsboard.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stiven.deptsboard.R;
import com.stiven.deptsboard.model.Debt;
import com.stiven.deptsboard.view.adapter.MyAdapter;
import com.stiven.deptsboard.view.fragment.BorrowFragment;
import com.stiven.deptsboard.view.fragment.LendFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";
    private static final String DEBT_NODE = "users";
    private Button btnSignOut;
    private FloatingActionButton fab;
    private BottomNavigationView nav;
    private ArrayList<Debt> debts;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;

    private RecyclerView recyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private double totalBorrowed;
    private double totalLent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showToolbar("Debts Board", true);

        totalBorrowed = 0;
        totalLent = 0;

        btnSignOut = (Button) findViewById(R.id.btnSignOut);
        nav = (BottomNavigationView) findViewById(R.id.navigation);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //real-time database
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //Habilitar la persistencia de Firebase cuando este sin internet
        databaseReference = FirebaseDatabase.getInstance().getReference(); // estamos obteniendo: debts-board
        initialize();
        firebaseUser = firebaseAuth.getCurrentUser();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an MyAdapter
        debts = new ArrayList<>();
        mAdapter = new MyAdapter(debts, R.layout.cardview_debt, this);
        recyclerView.setAdapter(mAdapter);

        databaseReference.child(DEBT_NODE)
                .child(firebaseUser.getUid())
                .child("debts")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                debts.clear();
                totalBorrowed = 0;
                totalLent = 0;
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Debt debt = snapshot.getValue(Debt.class);
                        Log.w(TAG, "Debt Name: " +debt.getName());
//                        Toast.makeText(MainActivity.this, debt.getName()+debt.isType(), Toast.LENGTH_SHORT).show();
                        debts.add(debt);
                        // Total borrowed or lent
                        if (debt.isType()){
                            totalBorrowed += debt.getAmount();
                        }else{
                            totalLent += debt.getAmount();
                        }
                    }
                }
                DecimalFormat formatter = new DecimalFormat("#,###,###.#");

                // Total borrowed or lent:
                BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
                bottomNavigationView.getMenu().getItem(0).setTitle("Borrowed:$" + formatter.format(totalBorrowed));
                bottomNavigationView.getMenu().getItem(1).setTitle("Lent:$" + formatter.format(totalLent));
                showDebts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean wrapInScrollView = false;
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Add Debt")
                        .customView(R.layout.custom_dialog, wrapInScrollView)
                        .positiveText("Add")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                View view = dialog.getCustomView();
                                EditText name_dialog = (EditText) view.findViewById(R.id.name_dialog);
                                EditText amount_dialog = (EditText) view.findViewById(R.id.amount_dialog);
                                Switch type_dialog = (Switch) view.findViewById(R.id.type_dialog);

                                String name = name_dialog.getText().toString();
                                Double amount = Double.parseDouble(amount_dialog.getText().toString());
                                boolean type = type_dialog.isChecked();

                                createDebt(name, amount, type);

                                Toast.makeText(MainActivity.this, "Added [" + name +","+amount+"]",
                                        Toast.LENGTH_SHORT).show();

                            }
                        })
                        .show();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_borrow:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container, borrowFragment)
//                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                                .addToBackStack(null).commit();
                        break;
                    case R.id.action_lend:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container, lendFragment)
//                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                                .addToBackStack(null).commit();
                        break;
                }
                return true;
            }
        });

    }

    public void createDebt(String name, Double amount, boolean type){
        Debt debt = new Debt(databaseReference.push().getKey(), name, amount, type, "");
        databaseReference.child(DEBT_NODE)
                .child(firebaseUser.getUid())
                .child("debts")
                .child(debt.getId())
                .setValue(debt);
    }

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser != null) {
//                    TextView tvUserData = (TextView) findViewById(R.id.tvUserData);
//                    tvUserData.setText("UserName: " + firebaseUser.getDisplayName() + " Email: " + firebaseUser.getEmail());
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

    private void signOut() {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Error in Google Sign Out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showDebts(){
        mAdapter.notifyDataSetChanged();
    }

    public void showToolbar(String title, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
        showDebts();
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
