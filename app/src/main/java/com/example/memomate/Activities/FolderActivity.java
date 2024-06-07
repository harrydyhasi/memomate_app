package com.example.memomate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.memomate.Adapters.StudySetAdapter;
import com.example.memomate.Adapters.StudySetRecyclerViewAdapter;
import com.example.memomate.FBModel.StudySetModel;
import com.example.memomate.Fragments.LibraryFragment;
import com.example.memomate.Fragments.TabFoldersFragment;
import com.example.memomate.Models.FlashCard;
import com.example.memomate.Models.Folder;
import com.example.memomate.Models.StudySet;
import com.example.memomate.Models.User;
import com.example.memomate.R;
import com.example.memomate.viewmodel.FolderViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FolderActivity extends AppCompatActivity {
    ImageButton btnReturn, btnAdd, btnMenu;
    RecyclerView rcvFolder;
    ArrayList<StudySet> studySets = new ArrayList<>();
    CardView cardAddSet;
    TextView txtTitleFolder, txtQuantity, txtUserName;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String id;
    ProgressDialog progressDialog;
    ImageView avatar;
    FolderViewModel folderViewModel;

    StudySetRecyclerViewAdapter studySetRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        initView();
        createRecyclerView();
        getIntentFromFolderAdapter();
        loadDetailFolder();
        loadAvatar();
        loadUserName();
        //loadData();

    }

    private void initView()
    {
        btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title_folder_activity", txtTitleFolder.getText().toString());
                setResult(45, intent);
                finish();
            }
        });
        btnAdd = findViewById(R.id.btnAdd);
        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMenu();
            }
        });
        cardAddSet = findViewById(R.id.cardAddSet);
        cardAddSet.setVisibility(View.GONE);
        txtTitleFolder = findViewById(R.id.txtTitleFolder);
        txtQuantity = findViewById(R.id.txtQuantity);
        txtQuantity.setText(String.valueOf(studySets.size()) + " sets");

        rcvFolder = findViewById(R.id.rcvFolder);

        progressDialog = new ProgressDialog(FolderActivity.this);
        progressDialog.setMessage("Please wait a minute");

        avatar = findViewById(R.id.avatar);
        txtUserName = findViewById(R.id.txtUserName);
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
                Intent i = new Intent(FolderActivity.this, EditFolderActivity.class);
                String title = txtTitleFolder.getText().toString();
                i.putExtra("idFolder", id);
                startActivityForResult(i, 70);
                dialog.dismiss();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FolderActivity.this, AddSetActivity.class);
                i.putExtra("FolderID", id);
                startActivity(i);
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

    private void createRecyclerView()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvFolder.setLayoutManager(linearLayoutManager);
        StudySetRecyclerViewAdapter studySetAdapter = new StudySetRecyclerViewAdapter(studySets);
        rcvFolder.setAdapter(studySetAdapter);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==70) && (resultCode==71))
        {
            loadDetailFolder();
        }

//        if (requestCode == 50 && resultCode == 51) {
//
//            Log.d("BACK", "Successfull");
//
//            ArrayList<StudySet> newStudySets = new ArrayList<>();
//            Intent i = getIntent();
//
//            if(i.getSerializableExtra("getStudySets") != null){
//                newStudySets = (ArrayList<StudySet>) i.getSerializableExtra("getStudySets");
//                for (int j = 0; j < newStudySets.size(); j++) {
//                    studySets.add(newStudySets.get(j));
//                    Log.d("GET INTENT", "Successfull");
//                }
//            }
//            createRecyclerView();
//        }
    }


    public void getIntentFromFolderAdapter() {
        Intent i = getIntent();
        id = i.getStringExtra("id_title_adapter");
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
                    else Toast.makeText(FolderActivity.this, "Error to load title", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(FolderActivity.this, "Error to load title", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else Toast.makeText(this, "error to get title", Toast.LENGTH_SHORT).show();
    }

    private void showDialogWarining()
    {
        final Dialog dialog = new Dialog(FolderActivity.this);
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
                Toast.makeText(FolderActivity.this, "Folder deleted", Toast.LENGTH_SHORT).show();
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
                    setProfilePic(FolderActivity.this, uri, avatar);
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
                Toast.makeText(FolderActivity.this, "ERROR TO GET PROFILE", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setProfilePic(Context context, Uri imageUri, ImageView avatar)
    {
        Glide.with(context).load(imageUri).apply(RequestOptions.circleCropTransform()).into(avatar);
    }

    private void loadData()
    {
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        folderViewModel = new FolderViewModel(currUser.getUid());
//
//
//        List<StudySetModel> A = folderViewModel.getStudySet(id);
//        ArrayList<StudySet> result = new ArrayList<>();
//        for (int i=0; i<A.size(); i++)
//        {
//            StudySet tmp = new StudySet();
//            tmp.setId(A.get(i).getId());
//            tmp.setName(A.get(i).getName());
//            tmp.setDesc(A.get(i).getDesc());
//            result.add(tmp);
//        }
//        studySetRecyclerViewAdapter = new StudySetRecyclerViewAdapter(result);
//        rcvFolder.setAdapter(studySetRecyclerViewAdapter);
//        rcvFolder.setLayoutManager(layoutManager);


    }

}

//package com.example.memomate.Activities;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.example.memomate.Adapters.StudySetRecyclerViewAdapter;
//import com.example.memomate.Models.Folder;
//import com.example.memomate.Models.StudySet;
//import com.example.memomate.R;
//
//import java.util.ArrayList;
//
//public class FolderActivity extends AppCompatActivity {
//    ImageButton btnReturn, btnAdd, btnMenu;
//    RecyclerView rcvFolder;
//    ArrayList<StudySet> studySets = new ArrayList<>();
//    CardView cardAddSet;
//    TextView txtTitleFolder, txtQuantity, txtAuthor;
//    ImageView avatar;
//    StudySetRecyclerViewAdapter studySetAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_folder);
//        initView();
//        populateDummyFlashcards();
//        createRecyclerView();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 50 && resultCode == 51) {
//
//            Log.d("BACK", "Successfull");
//
//            ArrayList<StudySet> newStudySets = new ArrayList<>();
//            Intent i = getIntent();
//
//            if(i.getSerializableExtra("getStudySets") != null){
//                newStudySets = (ArrayList<StudySet>) i.getSerializableExtra("getStudySets");
//                for (int j = 0; j < newStudySets.size(); j++) {
//                    studySets.add(newStudySets.get(j));
//                    Log.d("GET INTENT", "Successfull");
//                }
//            }
//            createRecyclerView();
//        }
//    }
//
//    private void initView()
//    {
//        btnReturn = findViewById(R.id.btnReturn);
//        btnReturn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        btnAdd = findViewById(R.id.btnAdd);
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(FolderActivity.this, AddSetActivity.class);
////                i.putExtra("getTitle", folder.getTitle());
////                i.putExtra("getAvatar", folder.getAvatar());
////                i.putExtra("getUserName", folder.getAuthor());
////                i.putExtra("position", holder.getAdapterPosition());
//                ((Activity)FolderActivity.this).startActivityForResult(i, 50);
//            }
//        });
//        btnMenu = findViewById(R.id.btnMenu);
//        btnMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialogMenu();
//            }
//        });
//        rcvFolder = findViewById(R.id.rcvFolder);
//        cardAddSet = findViewById(R.id.cardAddSet);
//        cardAddSet.setVisibility(View.GONE);
//        txtTitleFolder = findViewById(R.id.txtTitleFolder);  // Cai nay lay du lieu
//        txtQuantity = findViewById(R.id.txtQuantity);
//        //txtAuthor = findViewById(R.id.txtAuthor);
//        avatar = findViewById(R.id.avatar);
//        txtQuantity.setText(String.valueOf(studySets.size()) + " sets");
//
//        rcvFolder = findViewById(R.id.rcvFolder);
//    }
//
//    private void showDialogMenu() {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_folder_menu);
//        Window window = dialog.getWindow();
//        if (window == null)
//        {
//            return;
//        }
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.setWindowAnimations(R.style.BottomDialogAnimation);
//
//        WindowManager.LayoutParams windowAttributes = window.getAttributes();
//        windowAttributes.gravity = Gravity.BOTTOM;
//        window.setAttributes(windowAttributes);
//        dialog.setCancelable(true);
//
//        TextView btnEdit = dialog.findViewById(R.id.btnEdit);
//        TextView btnAdd = dialog.findViewById(R.id.btnAdd);
//        TextView btnDelete = dialog.findViewById(R.id.btnDelete);
//        TextView btnCancel = dialog.findViewById(R.id.btnCancel);
//
//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//
//        btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(FolderActivity.this, CreateFolderActivity.class);
//                startActivity(i);
//            }
//        });
//
//
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // dô của th hòa
//            }
//        });
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // dô của th hòa
//            }
//        });
//        dialog.show();
//    }
//
//
//    private void createRecyclerView()
//    {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
//        studySetAdapter = new StudySetRecyclerViewAdapter(studySets);
//        rcvFolder.setLayoutManager(linearLayoutManager);
//        rcvFolder.setAdapter(studySetAdapter);
//    }
//
//    private void populateDummyFlashcards(){
//
//        Bundle b = getIntent().getExtras();
//        Folder folder = (Folder) b.getSerializable("Folder");
//        txtTitleFolder.setText(folder.getTitle());
//        Glide.with(this).load(folder.getAvatar()).into(avatar);
//        txtAuthor.setText(folder.getAuthor());
//        studySets = folder.getStudySetList();
//        txtQuantity.setText(String.valueOf(studySets.size()) + " sets");
//    }
//}