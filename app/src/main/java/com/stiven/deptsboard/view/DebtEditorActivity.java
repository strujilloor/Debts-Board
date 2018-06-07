package com.stiven.deptsboard.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class DebtEditorActivity extends AppCompatActivity {
    String id_debt;
    TextInputEditText lender;
    TextInputEditText debtor;
    TextInputEditText amount;
    TextInputEditText details;
    Button btn_update;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;

    Debt debt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_editor);

        showToolbar("Dept Editor", true);

        Intent intent = getIntent();
        id_debt = intent.getStringExtra(MyAdapter.EXTRA_ID);

        lender = (TextInputEditText) findViewById(R.id.lender);
        debtor = (TextInputEditText) findViewById(R.id.debtor);
        amount = (TextInputEditText) findViewById(R.id.amount);
        details = (TextInputEditText) findViewById(R.id.details);
        btn_update = (Button) findViewById(R.id.btn_update);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference(); // estamos obteniendo: debts-board
        firebaseUser = firebaseAuth.getCurrentUser();

        debt = new Debt();

        databaseReference.child("users")
                .child(firebaseUser.getUid())
                .child("debts")
                .child(id_debt)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                            debt = dataSnapshot.getValue(Debt.class);
                        }
                        // SETTEAR CAMPOS
                        if (debt.isType()){
                            lender.setText(debt.getName());
                            debtor.setText("Me");
                            amount.setText(debt.getAmount());
                            details.setText(debt.getDetails());
                        }else{
                            lender.setText("Me");
                            debtor.setText(debt.getName());
                            amount.setText(debt.getAmount());
                            details.setText(debt.getDetails());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capturando nuevos valores
                if (debt.isType()){
                    debt.setName(lender.getText().toString());
                    debt.setAmount(amount.getText().toString());
                    debt.setDetails(details.getText().toString());
                }else{
                    debt.setName(debtor.getText().toString());
                    debt.setAmount(amount.getText().toString());
                    debt.setDetails(details.getText().toString());
                }

                databaseReference.child("users")
                        .child(firebaseUser.getUid())
                        .child("debts")
                        .child(id_debt)
                        .setValue(debt);

                Toast.makeText(DebtEditorActivity.this, "Dept Updated", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DebtEditorActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void showToolbar(String title, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }
}
