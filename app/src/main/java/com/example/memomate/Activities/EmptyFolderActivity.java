package com.example.memomate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.memomate.Fragments.TabFoldersFragment;
import com.example.memomate.Models.Folder;
import com.example.memomate.Models.StudySet;
import com.example.memomate.Models.User;
import com.example.memomate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class EmptyFolderActivity extends AppCompatActivity {
    String id;
    ProgressDialog progressDialog;
    TextView txtTitleFolder, txtQuantity, txtUserName;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<StudySet> studySets = new ArrayList<>();
    ImageButton btnReturn, btnAdd, btnMenu;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_folder);

        getFormWidget();
        addEvents();
        getIntentFromCreateFolder();
        loadDetailFolder();
        loadAvatar();
        loadUserName();
    }

    private void addEvents() {
        progressDialog = new ProgressDialog(EmptyFolderActivity.this);
        progressDialog.setMessage("Please wait a minute");

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMenu();
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void getFormWidget() {
        txtTitleFolder = findViewById(R.id.txtTitleFolder);  // Cai nay lay du lieu
        txtQuantity = findViewById(R.id.txtQuantity);
        txtQuantity.setText(String.valueOf(studySets.size()) + " sets");
        btnMenu = findViewById(R.id.btnMenu);
        btnReturn = findViewById(R.id.btnReturn);

        btnAdd = findViewById(R.id.btnAdd);
        txtUserName = findViewById(R.id.txtUserName);
        avatar = findViewById(R.id.avatar);


    }

    public void getIntentFromCreateFolder()
    {
        Intent i = getIntent();
        id = i.getStringExtra("id_folder");
    }
    public void loadDetailFolder()
    {
        progressDialog.show();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        if (id!= null)
        {
            databaseReference.child(firebaseUser.getUid()).child("list_folder").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Folder folder = snapshot.getValue(Folder.class);
                    if (folder != null) {
                        progressDialog.dismiss();
                        txtTitleFolder.setText(folder.getTitle());
                        Log.d("title folder", folder.getTitle());

                    }
                    else Toast.makeText(EmptyFolderActivity.this, "Error to load title", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EmptyFolderActivity.this, "Error to load title", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else Toast.makeText(this, "error to get title", Toast.LENGTH_SHORT).show();
    }
    private void showDialogMenu() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_folder_menu);
        Window window = dialog.getWindow();
        if (window == null)
        {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setWindowAnimations(R.style.BottomDialogAnimation);

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        TextView btnEdit = dialog.findViewById(R.id.btnEdit);
        TextView btnAdd = dialog.findViewById(R.id.btnAdd);
        TextView btnDelete = dialog.findViewById(R.id.btnDelete);
        TextView btnCancel = dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmptyFolderActivity.this, EditFolderActivity2.class);
                //String title = txtTitleFolder.getText().toString();
                i.putExtra("idFolderEmpty", id);
                startActivityForResult(i, 704);
                dialog.dismiss();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dô của th hòa
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWarining();
            }
        });
        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==704) && (resultCode==715))
        {
            loadDetailFolder();
        }
    }

    private void showDialogWarining()
    {
        final Dialog dialog = new Dialog(EmptyFolderActivity.this);
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

        TextView txtDelete = dialog.findViewById(R.id.txtDelete);
        TextView txtDelete1 = dialog.findViewById(R.id.txtDelete1);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnYes = dialog.findViewById(R.id.btnYes);

        txtDelete.setText("Delete Folder");
        txtDelete1.setText("Are you sure you want to delete this folder? ");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFolder();
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }
    public void deleteFolder()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(firebaseUser.getUid()).child("list_folder").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EmptyFolderActivity.this, "Folder deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loadAvatar()
    {
        progressDialog.show();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseStorage.getInstance().getReference().child("profile_pic").child(firebaseUser.getUid()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Uri uri = task.getResult();
                    setProfilePic(EmptyFolderActivity.this, uri, avatar);
                }
            }
        });
    }
    public void loadUserName()
    {
        progressDialog.show();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    progressDialog.dismiss();
                    txtUserName.setText(user.getUserName());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmptyFolderActivity.this, "ERROR TO GET PROFILE", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setProfilePic(Context context, Uri imageUri, ImageView avatar)
    {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(avatar);
    }

}