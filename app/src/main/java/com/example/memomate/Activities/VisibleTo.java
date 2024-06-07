package com.example.memomate.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memomate.R;

public class VisibleTo extends AppCompatActivity {
    TextView btnDone, test;
    ImageButton btnClose;
    RadioGroup rdgroup;
    //    RadioGroupPlus rdgroup;
    RadioButton rdbEveryone, rdbPeopleWithPass, rdbJustMe;
    String setVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visible_to);
        getFormWidgets();
        getVisible();
        addEvents();
    }
    public void getFormWidgets()
    {
        btnDone = findViewById(R.id.btnDone);
        btnClose = findViewById(R.id.btnClose);
        rdgroup = findViewById(R.id.rdgroup);
        rdbEveryone = findViewById(R.id.rdbEveryone);
        rdbPeopleWithPass = findViewById(R.id.rdbPeopleWithPass);
        rdbJustMe = findViewById(R.id.rdbJustMe);
        test = findViewById(R.id.test);
    }
    public void addEvents()
    {
//        rdgroup.setOnCheckedChangeListener(new RadioGroupPlus.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroupPlus radioGroupPlus, int checkedId) {
//                if (checkedId == R.id.rdbEveryone)
//                {
//                    setVisible="Everyone";
//                }
//                else if (checkedId == R.id.rdbPeopleWithPass)
//                {
//                    Intent i = new Intent(VisibleTo.this,Setting_Password.class);
//                    startActivityForResult(i, 18);
//                    setVisible="With Password";
//                }
//                else if (checkedId == R.id.rdbJustMe)
//                {
//                    setVisible="Just me";
//                }
//            }
//        });
        rdgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdbEveryone)
                {
                    setVisible=rdbEveryone.getText().toString();
                }
                else if (checkedId == R.id.rdbPeopleWithPass)
                {
                    Intent i = new Intent(VisibleTo.this,Setting_Password.class);
                    startActivityForResult(i, 18);
                    setVisible="With Password";
                }
                else if (checkedId == R.id.rdbJustMe)
                {
                    setVisible=rdbJustMe.getText().toString();
                }
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                i.putExtra("set", setVisible);
                setResult(1, i);
                finish();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    public void getVisible()
    {
        Intent i = getIntent();
        String visible = i.getStringExtra("setVisible");
        if (visible.equals("Everyone"))
        {
            rdbEveryone.setChecked(true);
        }
        else if (visible.equals("People with a password"))
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
        if (requestCode==18 && resultCode==RESULT_OK)
        {
            String password = data.getStringExtra("password");
            test.setText(password);
            rdbPeopleWithPass.setChecked(true);
        }
        else if (resultCode != RESULT_OK)
        {
            getVisible();
        }

    }
}