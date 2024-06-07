package com.example.memomate.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memomate.R;

public class Setting_Password extends AppCompatActivity {
    TextView btnSave;
    EditText edtPass;
    ImageButton btnReturn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_password);
        getFormWidgets();
        addEvents();

    }
    public void getFormWidgets()
    {
        btnSave = findViewById(R.id.btnSave);
        btnReturn = findViewById(R.id.btnReturn);
        edtPass = findViewById(R.id.edtPass);

    }
    public void addEvents()
    {
        btnSave.setVisibility(View.GONE);
        edtPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s))
                {
                    btnSave.setVisibility(View.GONE);
                }
                else
                {
                    btnSave.setVisibility(View.VISIBLE);
                }

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edtPass.getText().toString();
                Intent intent = getIntent();
                intent.putExtra("password", password);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}