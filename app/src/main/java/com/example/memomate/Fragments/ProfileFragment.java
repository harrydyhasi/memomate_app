package com.example.memomate.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.memomate.Activities.ChangePassWordActivity;
import com.example.memomate.Activities.DashboardActivity;
import com.example.memomate.Activities.IntroActivity;
import com.example.memomate.Models.User;
import com.example.memomate.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {
    private Context context;
    Button btnChangePassword;
    EditText edtEmail;
    CardView cardUserName;
    TextView txtUserName1, txtUserName2;
    Button btnLogout, btnDeleteAccount;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    ImageView imgAvatar;
    ProgressDialog progressDialog;

    public ProfileFragment(Context context) {
        this.context = context;
    }
    public ProfileFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        if(data!= null && data.getData()!= null)
                        {
                            selectedImageUri = data.getData();
                            setProfilePic(getContext(), selectedImageUri, imgAvatar);
                            Log.d("ProfileFragment", "Selected Image URI: " + selectedImageUri);
                            getCurrentProfilePic();
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFormWidgets(view);
        addEvents(view);
        setProfileByEmail();
        setProfileByGoogle();
    }

    public void getFormWidgets(View v)
    {
        btnChangePassword = v.findViewById(R.id.btnChangePassword);
        edtEmail = v.findViewById(R.id.edtEmail);
        cardUserName = v.findViewById(R.id.cardUserName);
        txtUserName1 = v.findViewById(R.id.txtUserName1);
        txtUserName2 = v.findViewById(R.id.txtUserName2);
        btnLogout = v.findViewById(R.id.btnLogout);
        btnDeleteAccount = v.findViewById(R.id.btnDeleteAccount);
        imgAvatar = v.findViewById(R.id.imgAvatar);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait a minute");
    }

    public void addEvents(View v)
    {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangePassWordActivity.class);
                startActivityForResult(i, 34);
            }
        });
        cardUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangeUserName(v);
            }
        });
        //txtUserName2.setText(txtUserName1.getText().toString());
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), IntroActivity.class);
                startActivity(i);
            }
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWarining(v);
            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(getActivity()).cropSquare().compress(512).maxResultSize(512, 512)
                        .createIntent(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent intent) {
                                imagePickLauncher.launch(intent);
                                return null;

                            }
                        });
            }
        });

    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 34 && resultCode == 65) {
            String newPassword = data.getStringExtra("newPass");
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null)
            {
                firebaseUser.updatePassword(data.getStringExtra("newPass"))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                                    databaseReference.child(firebaseUser.getUid()).child("passWord").setValue(data.getStringExtra("newPass"), new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else
                                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();

                            }
                        });
            }

            firebaseUser.updatePassword(newPassword).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("error", e.getLocalizedMessage());
                }
            });
        }
    }
    private void setProfileByEmail() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null)
        {
            Toast.makeText(getActivity(), "ERROR TO GET PROFILE", Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
            databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = new User();
                    user.setUserName(snapshot.child("userName").getValue(String.class));
                    user.setEmail(snapshot.child("email").getValue(String.class));
                    if (user != null) {
                        txtUserName1.setText(user.getUserName());
                        txtUserName2.setText(user.getUserName());
                        edtEmail.setText(user.getEmail());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "ERROR TO GET PROFILE", Toast.LENGTH_SHORT).show();
                }
            });
            getProfileImgFromStorage();
        }

    }

    private void setProfileByGoogle()
    {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            for (UserInfo profile : user.getProviderData()) {
//                txtUserName1.setText(profile.getDisplayName());
//                //txtUserName2.setText(profile.getDisplayName());
//                edtEmail.setText(profile.getEmail());
//                //Uri avatar = profile.getPhotoUrl();
//            }
//        }
    }
    private void showDialogChangeUserName(View v)
    {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_username);
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

        Button btnCancelDialog = dialog.findViewById(R.id.btnCancelDialog);
        Button btnSaveDialog = dialog.findViewById(R.id.btnSaveDialog);
        EditText edtUserName = dialog.findViewById(R.id.edtUserName);
        edtUserName.setText(txtUserName2.getText().toString());
        btnCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSaveDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUserName = edtUserName.getText().toString();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference.child(firebaseUser.getUid()).child("userName").setValue(edtUserName.getText().toString(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        txtUserName1.setText(newUserName);
                        txtUserName2.setText(newUserName);
                    }
                });

                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void showDialogWarining(View v)
    {
        final Dialog dialog = new Dialog(v.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_delete_account);
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

        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnYes = dialog.findViewById(R.id.btnYes);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                databaseReference.child(firebaseUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "User account deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                firebaseUser.delete();
                dialog.dismiss();
                Intent i = new Intent(getActivity(), IntroActivity.class);
                startActivity(i);
            }
        });
        dialog.show();
    }
    public void setProfilePic(Context context, Uri imageUri, ImageView imageView)
    {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
    public void getCurrentProfilePic()
    {
        progressDialog.show();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (selectedImageUri != null)
        {
            //FirebaseStorage firebaseStorage =  FirebaseStorage.getInstance().getReference("profile_pic");
            FirebaseStorage.getInstance().getReference().child("profile_pic").child(firebaseUser.getUid()).putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        progressDialog.dismiss();
                    }
                    else
                        Toast.makeText(getActivity(), "Can't upload profile", Toast.LENGTH_SHORT).show();

                }
            });
//            FirebaseStorage.getInstance().getReference().child("profile_pic").child(firebaseUser.getUid()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful())
//                    {
//                        Uri uri = task.getResult();
//                        setProfilePic(getContext(), uri, imgAvatar);
//                        String imgUri = uri.toString();
//                        FirebaseDatabase.getInstance().getReference("list_user/"+firebaseUser.getUid()).child("avatar").setValue(imgUri);
//
//                    }
//                }
//            });
        }
    }
    public void getProfileImgFromStorage()
    {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseStorage.getInstance().getReference().child("profile_pic").child(firebaseUser.getUid()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                {
                    Uri uri = task.getResult();
                    setProfilePic(getContext(), uri, imgAvatar);
                    String imgUri = uri.toString();
//                    Log.d("ProfileFragment", "Download URL: " + imgUri);
                    FirebaseDatabase.getInstance().getReference("User/"+firebaseUser.getUid()).child("avatar").setValue(imgUri);

                }
            }
        });
    }
}