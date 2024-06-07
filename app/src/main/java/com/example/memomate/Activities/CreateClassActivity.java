package com.example.memomate.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.memomate.Models.Class;
import com.example.memomate.R;
import com.example.memomate.Utils.DataFireBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateClassActivity extends AppCompatActivity {
    TextView btnSave;
    ImageButton btnClose;
    EditText edtTitle, edtDesc;
    Switch switchRight;
    DataFireBase dataFireBase;
    private String username;
    FirebaseUser firebaseUser;
    String uid;
    Class c = new Class();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        dataFireBase = new DataFireBase();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = firebaseUser.getUid();
        retrieveUserName();
        getFormWidgets();
        addEvents();
    }
    public void getFormWidgets()
    {
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtDesc = (EditText) findViewById(R.id.edtDesc);
        btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnSave = (TextView) findViewById(R.id.btnSave);
        switchRight = (Switch) findViewById(R.id.switchRight);
    }
    public void  addEvents()
    {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClassData();
                dataFireBase.addClass(c);
                finish();
            }
        });
    }
    private void setClassData(){
        c.setTitle(edtTitle.getText().toString());
        c.setDesc(edtDesc.getText().toString());

        if(switchRight.isChecked()) {
            c.setRight(true);
        } else {
            c.setRight(false);
        }

        c.setCreator(username);
        c.setStudySetList(new ArrayList<>());
        ArrayList<String> members = new ArrayList<>();
        members.add(uid);
        c.setMemberList(members);
    }
    private void retrieveUserName() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("list_user").child(uid).child("userName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        String userName = snapshot.getValue(String.class);
                        Log.d("MainActivity", "userName: " + userName);
                        username = userName;
                    } else {
                        Log.d("MainActivity", "No such user exists");
                    }
                } else {
                    Log.d("MainActivity", "Error getting user data", task.getException());
                }
            }
        });
    }




}