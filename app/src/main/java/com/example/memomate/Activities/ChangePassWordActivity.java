package com.example.memomate.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memomate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePassWordActivity extends AppCompatActivity {

    ImageButton btnReturn;
    TextView btnSave, notification;
    EditText edtNewPass, edtConfirmPass, edtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);

        getFormWidgets();
        addEvents();
    }

    public void getFormWidgets()
    {
        btnReturn = findViewById(R.id.btnReturn);
        btnSave = findViewById(R.id.btnSave);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        notification = findViewById(R.id.notification);
        edtPass = findViewById(R.id.edtPass);
    }
    public void addEvents()
    {
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSave.setVisibility(View.GONE);
        notification.setVisibility(View.GONE);
        edtNewPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hideBtnSave();

            }
        });
        edtConfirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                hideBtnSave();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String currentPassword = dataSnapshot.child("passWord").getValue(String.class);
                            // So sánh mật khẩu
                            if (edtPass.getText().toString().equals(currentPassword))
                            {
                                if (edtNewPass.getText().toString().equals(edtConfirmPass.getText().toString()))
                                {
                                    Intent i = getIntent();
                                    String newPass = edtConfirmPass.getText().toString();
                                    i.putExtra("newPass", newPass);
                                    setResult(65, i);
                                    finish();
                                }
                                else
                                {
                                    notification.setVisibility(View.VISIBLE);
                                    edtNewPass.setText("");
                                    edtConfirmPass.setText("");
                                    edtNewPass.requestFocus();
                                }
                            }
                            else {
                                notification.setVisibility(View.VISIBLE);
                                notification.setText("Your password is incorrect");
                                edtPass.setText("");
                                edtNewPass.setText("");
                                edtConfirmPass.setText("");
                                edtPass.requestFocus();
                            }
                        } else {

                            Toast.makeText(ChangePassWordActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Firebase", "Error " + databaseError.getMessage());
                    }
                });

            }
        });

    }
    public void hideBtnSave()
    {
        if (edtNewPass.getText().toString().trim().length() == 0 || edtConfirmPass.getText().toString().trim().length() == 0)
        {
            btnSave.setVisibility(View.GONE);
        }
        else btnSave.setVisibility(View.VISIBLE);
    }

}