package com.example.memomate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memomate.Adapters.FolderRecyclerViewAdapter;
import com.example.memomate.Adapters.MemberAdapter;
import com.example.memomate.Models.Class;
import com.example.memomate.Models.Folder;
import com.example.memomate.Models.User;
import com.example.memomate.R;
import com.example.memomate.Utils.DataFireBase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ClassDetailActivity extends AppCompatActivity {
    RecyclerView rcvMember, rcvFolder;
    MemberAdapter memberAdapter;
    FolderRecyclerViewAdapter folderAdapter;

    ArrayList<User> listMember = new ArrayList<>();
    ArrayList<Folder> listFolder = new ArrayList<>();
    ImageButton btnReturn, btnShare, btnMenu;
    CardView layoutAddSet;
    TextView txtName, txtQuantity;
    Dialog dialog;
    String id_class;

    Class c;
    DataFireBase dataFireBase = new DataFireBase();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = firebaseUser.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        getDataFromIntent();

        loadTab();
        getFormWidgets();
        addEvents();
        getListFolderFromFireBase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle();
    }
    public void setTitle(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("classes");
        ref.child(c.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { txtName.setText(snapshot.child("title").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getFormWidgets()
    {
        txtName = findViewById(R.id.txtName);
        setTitle();

        txtQuantity = findViewById(R.id.txtQuantity);
        txtQuantity.setText(String.valueOf(c.getStudySetList().size()) + " sets");
        btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnShare = findViewById(R.id.btnShare);
        btnMenu = findViewById(R.id.btnMenu);
        layoutAddSet = findViewById(R.id.layoutAddSet);

        dataFireBase = new DataFireBase();

        rcvMember = findViewById(R.id.rcvMember);


        listMember.clear();
        listMember = dataFireBase.getMemberListByMemberId(c.getMemberList());
        memberAdapter = new MemberAdapter(listMember);
        dataFireBase.setMemberAdapter(memberAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvMember.setLayoutManager(linearLayoutManager);
        rcvMember.setAdapter(memberAdapter);

        rcvFolder = findViewById(R.id.rcvClassFolder);
        folderAdapter = new FolderRecyclerViewAdapter(listFolder);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvFolder.setLayoutManager(linearLayoutManager1);
        rcvFolder.setAdapter(folderAdapter);

    }
    public void loadTab()
    {
        final TabHost tab = (TabHost) findViewById(R.id.tabHost);
        tab.setup();
        TabHost.TabSpec spec;
        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.class_sets);
        spec.setIndicator("Folders");

        tab.addTab(spec);
        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.class_members);
        spec.setIndicator("Members");
        tab.addTab(spec);
        tab.setCurrentTab(0);
    }
    public void addEvents()
    {
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMenu();
            }
        });
        layoutAddSet.setVisibility(View.GONE);
    }
    private void showDialogMenu()
    {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_class_menu);
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
//        TextView btnAddSet = dialog.findViewById(R.id.btnAddSet);
        TextView btnAddFolder = dialog.findViewById(R.id.btnAddFolder);
        //TextView btnDrop = dialog.findViewById(R.id.btnDrop);
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
                Intent i = new Intent(ClassDetailActivity.this, EditClassActivity.class);
                i.putExtra("Class", c);
                dialog.dismiss();
                startActivity(i);
            }
        });
//        btnAddSet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(ClassDetailActivity.this, AddSetActivity.class);
//                i.putExtra("Class", c);
//                dialog.dismiss();
//                startActivity(i);
//            }
//        });
        btnAddFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(ClassDetailActivity.this, AddFolderActivity.class);
                i.putExtra("Class", c);
                startActivity(i);
                //startActivityForResult(i, 32);
            }
        });
//        btnDrop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dataFireBase.removeMemberFromClass(c.getId(),uid);
//                Toast.makeText(ClassDetailActivity.this,"Drop class is successful", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWarining();
            }
        });
        dialog.show();
    }


    private void getDataFromIntent()
    {
        Bundle b = getIntent().getExtras();
        c = (Class) b.getSerializable("Class");
        id_class = c.getId();
        Log.d("Id class", " " + id_class);

    }
    private void showDialogWarining()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_delete_class);
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
                if(dataFireBase.deleteClass(c.getId())){
                    Toast.makeText(ClassDetailActivity.this,"Delete class is successful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }
            }
        });
        dialog.show();
    }
    public void getListFolderFromFireBase()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("classes");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(c.getId()).child("list_folder").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Folder folder = snapshot.getValue(Folder.class);
                if( folder!= null)
                {
                    listFolder.add(folder);
                    folderAdapter.notifyDataSetChanged();
//                    Toast.makeText(getContext(), "bnhjhj", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Folder folder = snapshot.getValue(Folder.class);
                if (folder == null || listFolder == null || listFolder.isEmpty()) return;
                for (int i=0; i<listFolder.size();i++)
                {
                    if (Objects.equals(folder.getId(), listFolder.get(i).getId()))
                        listFolder.set(i, folder);
                }

                folderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Folder folder = snapshot.getValue(Folder.class);

                if (folder == null || listFolder == null || listFolder.isEmpty()) return;
                for (int i=0; i<listFolder.size();i++)
                {
                    if (Objects.equals(folder.getId(), listFolder.get(i).getId())) {
                        listFolder.remove(listFolder.get(i));
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




}