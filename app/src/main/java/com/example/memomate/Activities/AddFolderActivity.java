package com.example.memomate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memomate.Adapters.FolderSelectAdapter;
import com.example.memomate.Models.Class;
import com.example.memomate.Models.Folder;
import com.example.memomate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class AddFolderActivity extends AppCompatActivity {
    private RecyclerView folderRcv;
    private FolderSelectAdapter folderAdapter;
    private LinearLayoutManager layoutManager;
    private ImageButton btnBack;
    private TextView btnDone, btnCreateNewFolder;
    ArrayList<Folder> folders = new ArrayList<>();
    Class c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_folder);

        folderRcv = findViewById(R.id.recyclerview_folder);
        folderAdapter = new FolderSelectAdapter(folders);

        initView();
        addEvent();
        getListFolderFromFireBase();
        getDataFromIntent();
    }
    private void initView(){
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ArrayList<String> studySetArrayList = new ArrayList<>();
        folderRcv.setLayoutManager(layoutManager);
        folderRcv.setAdapter(folderAdapter);
    }

public void addEvent(){
    btnBack = findViewById(R.id.btnBack);
    btnBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });
    btnDone = findViewById(R.id.btnDone);
//    btnDone.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent i = new Intent(AddFolderActivity.this, ClassDetailActivity.class);
//            i.putExtra("getFolders", folderAdapter.getSelectedFolders());
//            Log.d("SELECTED", String.valueOf(folderAdapter.getSelectedFolders().size()));
//            //Toast.makeText(AddFolderActivity.this, "id" + folderAdapter.getIdFolder(), Toast.LENGTH_SHORT).show();
//            //Log.d("SELECTED", folderAdapter.getIdFolder());
//
//            //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//            ArrayList<String> listFolderId = new ArrayList<>();
//            for (Folder folder : folderAdapter.getSelectedFolders()){
////                    listFolderId.add(folder.getId());
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classes");
//                databaseReference.child(c.getId()).child("list_folder").child(folder.getId()).setValue(folder.getId()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(AddFolderActivity.this, "Add folder successfully", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//
//            //setResult(51, i);
//            finish();
//        }
//    });
    btnDone.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(AddFolderActivity.this, ClassDetailActivity.class);
            i.putExtra("getFolders", folderAdapter.getSelectedFolders());
            Log.d("SELECTED", String.valueOf(folderAdapter.getSelectedFolders().size()));
            //Toast.makeText(AddFolderActivity.this, "id" + folderAdapter.getIdFolder(), Toast.LENGTH_SHORT).show();
            //Log.d("SELECTED", folderAdapter.getIdFolder());

            //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            ArrayList<String> listFolderId = new ArrayList<>();
            for (Folder folder : folderAdapter.getSelectedFolders()){
//                    listFolderId.add(folder.getId());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classes");
                databaseReference.child(c.getId()).child("list_folder").setValue(folderAdapter.getSelectedFolders()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AddFolderActivity.this, "Add folder successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            //setResult(51, i);
            finish();
        }
    });
    btnCreateNewFolder = findViewById(R.id.btnCreateANewFolder);
    btnCreateNewFolder.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(AddFolderActivity.this, CreateFolderInClassActivity.class);
            //i.putExtra("getSelectedStudySets", studySetAdapter.getSelectedStudySets());
//                i.putExtra("getAvatar", folder.getAvatar());
//                i.putExtra("getUserName", folder.getAuthor());
//                i.putExtra("position", holder.getAdapterPosition());
            startActivityForResult(i, 12);
        }
    });
}
    public void getListFolderFromFireBase()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(firebaseUser.getUid()).child("list_folder").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Folder folder = snapshot.getValue(Folder.class);
                if( folder!= null)
                {
                    folders.add(folder);
                    folderAdapter.notifyDataSetChanged();
//                    Toast.makeText(getContext(), "bnhjhj", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Folder folder = snapshot.getValue(Folder.class);
                if (folder == null || folders == null || folders.isEmpty()) return;
                for (int i=0; i<folders.size();i++)
                {
                    if (Objects.equals(folder.getId(), folders.get(i).getId()))
                        folders.set(i, folder);
                }

                folderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Folder folder = snapshot.getValue(Folder.class);

                if (folder == null || folders == null || folders.isEmpty()) return;
                for (int i=0; i<folders.size();i++)
                {
                    if (Objects.equals(folder.getId(), folders.get(i).getId())) {
                        folders.remove(folders.get(i));
                        break;
                    }
                }
                folderAdapter.notifyDataSetChanged();
//                Toast.makeText(getContext(), "dfdfsdfsd", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getDataFromIntent()
    {
        Bundle b = getIntent().getExtras();
        c = (Class) b.getSerializable("Class");
    }
}