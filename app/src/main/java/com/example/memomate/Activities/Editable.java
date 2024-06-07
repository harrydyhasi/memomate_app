package com.example.memomate.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memomate.R;

public class Editable extends AppCompatActivity {
    RadioGroup rdgroup;
    RadioButton rdbPeopleWithPass, rdbJustMe;
    String setEditable;
    TextView test, btnDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable);
        getFormWidgets();
        addEvents();
        getEditable();
    }
    public void getFormWidgets()
    {
        rdgroup = findViewById(R.id.rdgroup);
        rdbPeopleWithPass = findViewById(R.id.rdbPeopleWithPass);
        rdbJustMe = findViewById(R.id.rdbJustMe);
        test = findViewById(R.id.test);
        btnDone = findViewById(R.id.btnDone);
    }
    public void addEvents() {
        rdgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdbPeopleWithPass)
                {
                    Intent i = new Intent(Editable.this,Setting_Password.class);
                    startActivityForResult(i, 20);
                    setEditable="With Password";
                }
                else if (checkedId == R.id.rdbJustMe)
                {
                    setEditable=rdbJustMe.getText().toString();
                }
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                i.putExtra("set", setEditable);
                setResult(11, i);
                finish();
            }
        });
    }
    public void getEditable()
    {
        Intent i = getIntent();
        String visible = i.getStringExtra("setEditable");
        if (visible.equals("People with a password"))
        {
            rdbPeopleWithPass.setChecked(true);
        }
        else if (visible.equals("Just me"))
        {
            rdbJustMe.setChecked(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==20 && resultCode==RESULT_OK)
        {
            String password = data.getStringExtra("password");
            test.setText(password);
            rdbPeopleWithPass.setChecked(true);
        }
        else if (resultCode != RESULT_OK)
        {
            getEditable();
        }

    }
}