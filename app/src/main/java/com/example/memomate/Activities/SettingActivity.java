package com.example.memomate.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.memomate.R;

public class SettingActivity extends AppCompatActivity {
    TextView btnVisible;
    TextView btnEditable;
    ImageButton btnReturn;
    LinearLayout language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getFormWidgets();
        addEvents();

    }
    public void getFormWidgets()
    {
        btnVisible = findViewById(R.id.btnVisible);
        btnEditable = findViewById(R.id.btnEditable);
        btnReturn = findViewById(R.id.btnReturn);
        language = findViewById(R.id.language);
    }
    public  void addEvents()
    {
        language.setVisibility(View.GONE);
        btnVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, VisibleTo.class);
                String visible = btnVisible.getText().toString();
                i.putExtra("setVisible", visible);
                startActivityForResult(i, 99);
            }
        });
        btnEditable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, Editable.class);
                String editable = btnEditable.getText().toString();
                i.putExtra("setEditable", editable);
                startActivityForResult(i, 98);
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 99) && resultCode == 1)
        {
            String getVisible = data.getStringExtra("set");
            btnVisible.setText(getVisible);
        }
        if ((requestCode == 98) && resultCode == 11)
        {
            String getEditable = data.getStringExtra("set");
            btnEditable.setText(getEditable);
        }
    }
}