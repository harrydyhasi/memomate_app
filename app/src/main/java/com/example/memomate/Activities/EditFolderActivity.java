package com.example.memomate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memomate.Models.Folder;
import com.example.memomate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditFolderActivity extends AppCompatActivity {
    TextView btnSave;
    ImageButton btnClose;
    EditText edtTitle, edtDescription;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String id_folder;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_folder);
        getIntentFromFolderActivity();
        getFormWidgets();
        addEvents();
        setFolder();
    }

    public void getFormWidgets()
    {
        btnClose = findViewById(R.id.btnClose);
        btnSave = findViewById(R.id.btnSave);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        progressDialog = new ProgressDialog(EditFolderActivity.this);
        progressDialog.setMessage("Please wait a minute");
    }
    public void addEvents()
    {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference.child(firebaseUser.getUid()).child("list_folder").child(id_folder).child("title").setValue(edtTitle.getText().toString(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        progressDialog.dismiss();
                    }
                });
                databaseReference.child(firebaseUser.getUid()).child("list_folder").child(id_folder).child("description").setValue(edtDescription.getText().toString(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        progressDialog.dismiss();
                        Intent i = new Intent();
                        i.putExtra("title_folder", edtTitle.getText().toString());
                        setResult(71, i);
                        finish();
                    }
                });
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    public void getIntentFromFolderActivity()
    {
        Intent i = getIntent();
        id_folder = i.getStringExtra("idFolder");
    }
    public void getIntentFromEmptyFolderActivity()
    {
        Intent i = getIntent();
        id_folder = i.getStringExtra("idFolderEmpty");
    }
    //Lay chi tiet folder tu firebase
    public void setFolder()
    {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        if (id_folder != null)
        {
            databaseReference.child(firebaseUser.getUid()).child("list_folder").child(id_folder).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Folder folder = snapshot.getValue(Folder.class);
                    if (folder != null) {
                        edtTitle.setText(folder.getTitle());
                        edtDescription.setText(folder.getDescription());
                        Log.d("title folder", folder.getTitle());

                    }
                    else Toast.makeText(EditFolderActivity.this, "Error to load title", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditFolderActivity.this, "Error to load title", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else Toast.makeText(this, "error to get title", Toast.LENGTH_SHORT).show();
    }

}