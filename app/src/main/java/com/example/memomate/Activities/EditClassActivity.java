package com.example.memomate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.memomate.Models.Class;
import com.example.memomate.R;
import com.example.memomate.Utils.DataFireBase;

public class EditClassActivity extends AppCompatActivity {
    TextView btnDone, btnCancel;
    EditText edtTitle, edtDesc;
    Switch switchRight;
    private Class c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class);

        getIntentData();
        initView();
        addEvents();

    }
    public void initView()
    {
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtDesc = (EditText) findViewById(R.id.edtDesc);
        btnCancel = (TextView) findViewById(R.id.btnCancel);
        btnDone = (TextView) findViewById(R.id.btnDone);
        switchRight = (Switch) findViewById(R.id.switchRight);

        edtTitle.setText(c.getTitle());
        edtDesc.setText(c.getDesc());
        switchRight.setChecked(c.getRight());
    }
    public void  addEvents()
    {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.setTitle(edtTitle.getText().toString());
                c.setDesc(edtDesc.getText().toString());

                if(switchRight.isChecked()) {
                    c.setRight(true);
                } else {
                    c.setRight(false);
                }
                DataFireBase dataFireBase = new DataFireBase();
                dataFireBase.updateClass(c);

                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getIntentData(){
        Intent intent = getIntent();
        c = (Class) intent.getSerializableExtra("Class");
    }
}