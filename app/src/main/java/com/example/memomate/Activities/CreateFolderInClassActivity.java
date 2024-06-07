package com.example.memomate.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.memomate.Models.Folder;
import com.example.memomate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateFolderInClassActivity extends AppCompatActivity {
    TextView btnSave, txtNewFolder;
    ImageButton btnClose;
    EditText edtTitle, edtDescription;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder_in_class);
        getFormWidgets();
        addEvents();
    }

    public void getFormWidgets() {
        btnClose = findViewById(R.id.btnClose);
        btnSave = findViewById(R.id.btnSave);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        txtNewFolder = findViewById(R.id.txtNewFolder);

        progressDialog = new ProgressDialog(CreateFolderInClassActivity.this);
        progressDialog.setMessage("Please wait a minute");
    }

    public void addEvents() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnSave.setVisibility(View.INVISIBLE);
        edtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    btnSave.setVisibility(View.INVISIBLE);
                } else btnSave.setVisibility(View.VISIBLE);

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String title = edtTitle.getText().toString();
                String description = edtDescription.getText().toString();

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference = FirebaseDatabase.getInstance().getReference("User");
                Folder folder = new Folder(title, description);
                DatabaseReference pushedPostRef = FirebaseDatabase.getInstance().getReference("list_folder").push();
                String id = pushedPostRef.getKey();
                folder.setId(id);

                databaseReference.child(firebaseUser.getUid()).child("list_folder").child(folder.getId()).setValue(folder).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        finish();
                    }
                });
            }
        });
    }
}