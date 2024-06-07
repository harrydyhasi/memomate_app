package com.example.memomate.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.memomate.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LoginActivity extends AppCompatActivity {
    ImageView btnBack;
    TextView txtForgotUserName, txtForgotPassword, txtMessageEmail, txtMessagePass;
    MaterialButton btnLogin;
    DatePickerDialog datePickerDialog;
    EditText txtEmail, txtPassWord;
    FirebaseAuth mAuth;
    Button btnGoogle;
    ProgressDialog progressDialog;
    private SignInClient oneTapClient;
    private BeginSignInRequest signUpRequest;
    int requestCode;
    //    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
//    private boolean showOneTapUI = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        LoginWithGoogle();
        LoginWithEmail();
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

        txtForgotUserName = findViewById(R.id.txtForgotUserName);
        txtForgotUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForgot(R.layout.dialog_forgot_username);
            }
        });

        txtForgotPassword = findViewById(R.id.txtForgotPassword);
        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForgot(R.layout.dialog_forgot_password);
            }
        });
        txtEmail = findViewById(R.id.txtEmail);
        txtPassWord = findViewById(R.id.txtPassWord);
        txtMessageEmail = findViewById(R.id.txtMesssageEmail);
        txtMessageEmail.setVisibility(View.GONE);
        txtMessagePass = findViewById(R.id.txtMessagePass);
        txtMessagePass.setVisibility(View.GONE);


    }
    public void LoginWithGoogle()
    {
        btnGoogle = findViewById(R.id.btnGoogle);
        oneTapClient = Identity.getSignInClient(this);
        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.web_client_id))// trong strings.xml
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();
        //Đăng nhập bằng gg
        ActivityResultLauncher<IntentSenderRequest> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken !=  null) {
                        String email = credential.getId();
                        String username = credential.getDisplayName();
                        Log.d("Email login", email);
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                } catch (ApiException e) {
                    Log.d("TAG Login failed", e.getLocalizedMessage());
                }
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                oneTapClient.beginSignIn(signUpRequest)
                        .addOnSuccessListener(LoginActivity.this, new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult result) {
                                progressDialog.dismiss();
                                IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                                activityResultLauncher.launch(intentSenderRequest);
                            }
                        })
                        .addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // No Google Accounts found. Just continue presenting the signed-out UI.
                                Log.d("TAG Login failed", e.getLocalizedMessage());
                            }
                        });
            }
        });
    }

    public void LoginWithEmail()
    {
        mAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = txtEmail.getText().toString().trim();
                //Log.d("Login successful", email);
                String pass = txtPassWord.getText().toString();
                //Log.d("Login", pass);
                if (txtEmail.getText().toString().isEmpty())
                {
                    txtMessageEmail.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
                if (txtPassWord.getText().toString().isEmpty())
                {
                    txtMessagePass.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
                else
                {
                    mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful())
                            {
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                show_dialog_login_failed();
                                txtEmail.setText("");
                                txtPassWord.setText("");
                                txtEmail.requestFocus();
                            }
                        }
                    });
                }
            }
        });
    }


    private void showDialogForgot(int layout)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        if (window == null)
        {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        EditText edtEmail = dialog.findViewById(R.id.edtConfirmEmail);
        TextView txtCancel = dialog.findViewById(R.id.txtCancel);
        TextView txtOk = dialog.findViewById(R.id.txtOk);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senderEmail = "memomate915@gmail.com";
                String receiverEmail = edtEmail.getText().toString();
//                String passWordSenderEmail = "wvypxyjadnlkjgyw";
                String passWordSenderEmail = "nimmavbxqzbnadxr";

                Random random = new Random();
                requestCode = random.nextInt(8999) + 1000;

                String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
                Properties properties = System.getProperties();
                properties.setProperty("mail.smtp.host", "smtp.gmail.com");
                properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
                properties.setProperty("mail.smtp.socketFactory.fallback", "false");
                properties.setProperty("mail.smtp.port", "465");
                properties.setProperty("mail.smtp.socketFactory.port", "465");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.debug", "true");
                properties.put("mail.store.protocol", "pop3");
                properties.put("mail.transport.protocol", "smtp");

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, passWordSenderEmail);

                    }

                });
                MimeMessage mimeMessage = new MimeMessage(session);
                try {

                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
                    mimeMessage.setSubject("Subject: SendOTP");
                    mimeMessage.setText(String.valueOf(requestCode));
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Transport.send(mimeMessage);
                            } catch (MessagingException e) {
                                e.printStackTrace();
                                Log.e("error send mail", "Error sending email", e);
                            }
                        }
                    });
                    thread.start();
                    dialog.dismiss();
                    showDialogEnterOtp();

                } catch (MessagingException e) {
                    e.printStackTrace();
                    Log.e("error send mail", "Error sending email", e);
                }
            }
        });

        dialog.show();
    }


    private void show_dialog_login_failed()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login_failed);
        Window window = dialog.getWindow();
        if (window == null)
        {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        TextView txtOk = dialog.findViewById(R.id.txtOk);

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    private void showDialogEnterOtp()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_enter_otp);
        Window window = dialog.getWindow();
        if (window == null)
        {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        PinView enter_otp = dialog.findViewById(R.id.enter_otp);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputCode = enter_otp.getText().toString();
                if (inputCode.equals(String.valueOf(requestCode)))
                {
                    dialog.dismiss();
                    showDialogCreateNewPass();
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
    private void showDialogCreateNewPass()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_new_pass);
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

        EditText edtNewPass = dialog.findViewById(R.id.edtNewPass);
        EditText edtConfirmPass = dialog.findViewById(R.id.edtConfirmPass);
        TextView notification = dialog.findViewById(R.id.notification);
        Button btnSaveDialog = dialog.findViewById(R.id.btnSaveDialog);
        Button btnCancelDialog = dialog.findViewById(R.id.btnCancelDialog);

        notification.setVisibility(View.GONE);
        btnCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSaveDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtNewPass.getText().toString().equals(edtConfirmPass.getText().toString()))
                {
                    String newPassword = edtConfirmPass.getText().toString();
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser != null) {
                        firebaseUser.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                                            databaseReference.child(firebaseUser.getUid()).child("passWord").setValue(newPassword, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                    dialog.dismiss();
                                                    Toast.makeText(LoginActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    notification.setVisibility(View.VISIBLE);
                    edtNewPass.setText("");
                    edtConfirmPass.setText("");
                    edtNewPass.requestFocus();
                }
            }
        });
        dialog.show();
    }

}