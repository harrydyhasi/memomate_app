package com.example.memomate.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memomate.Models.User;
import com.example.memomate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
    ImageView btnBack;
    EditText edtBob, edtEmailSignUp, edtPassSignUp;
    DatePickerDialog datePickerDialog;
    TextView txtEmailHelper, txtBob;
    MaterialButton btnSignUp;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        initView();

    }

    private void initView()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait a minute");
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtBob = findViewById(R.id.edtBob);
        edtBob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        edtBob.setText(getToDayDate());

        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        edtEmailSignUp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validEmail())
                {
                    txtEmailHelper.setText("EMAIL ADDRESS");
                    txtEmailHelper.setTextColor(Color.parseColor("#616F91"));
                }
                else
                {
                    txtEmailHelper.setText("ENTER A VALID EMAIL ADDRESS.");
                    txtEmailHelper.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });
        txtEmailHelper = findViewById(R.id.txtEmailHelper);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpWithEmail();
            }
        });
        edtPassSignUp = findViewById(R.id.edtPassSignUp);
        initDatePicker();
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth,month,year);
                edtBob.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String getToDayDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + ", " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "Jan";
        if(month == 2)
            return "Feb";
        if(month == 3)
            return "Mar";
        if(month == 4)
            return "Apr";
        if(month == 5)
            return "May";
        if(month == 6)
            return "Jun";
        if(month == 7)
            return "Jul";
        if(month == 8)
            return "Aug";
        if(month == 9)
            return "Sep";
        if(month == 10)
            return "Oct";
        if(month == 11)
            return "Nov";
        if(month == 12)
            return "Dec";
        return "Jan";
    }

    private boolean validEmail()
    {
        String emailText = edtEmailSignUp.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
            return false;
        return true;
    }
    public void SignUpWithEmail()
    {
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        String emailSignUp = edtEmailSignUp.getText().toString();
        //Log.d("Create successful", emailSignUp);
        String passSignUp = edtPassSignUp.getText().toString();
        //Log.d("Create successful", passSignUp);
        mAuth.createUserWithEmailAndPassword(emailSignUp,passSignUp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    //String userName = firebaseUser.getDisplayName();
                    if (emailSignUp.contains("@")) {
                        String userName = emailSignUp.substring(0, emailSignUp.indexOf('@'));
                        User user = new User(userName, emailSignUp, passSignUp);
//                        User user = new User();
//                        user.setUserName(userName);
//                        user.setEmail(emailSignUp);
//                        user.setPassWord(passSignUp);
                        Log.d("PROFILE", "username " + userName + " email " + emailSignUp + "password " + passSignUp);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                        databaseReference.child(firebaseUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    showDialogSuccess();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, "Sign up fail", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Create account fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAuth.createUserWithEmailAndPassword(emailSignUp,passSignUp).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SignUp Fail", e.getMessage());
            }
        });

    }
    private void showDialogSuccess()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success);
        Window window = dialog.getWindow();
        if (window == null)
        {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.BottomDialogAnimation);

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        dialog.show();
    }
}