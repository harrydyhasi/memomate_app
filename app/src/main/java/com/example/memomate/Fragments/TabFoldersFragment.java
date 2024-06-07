package com.example.memomate.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Activities.FolderActivity;
import com.example.memomate.Adapters.FolderAdapter;
import com.example.memomate.Adapters.FolderRecyclerViewAdapter;
import com.example.memomate.Models.Folder;
import com.example.memomate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class TabFoldersFragment extends Fragment {
    private RecyclerView folderRecyclerView;
    private FolderRecyclerViewAdapter folderAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Folder> listFolder = new ArrayList<>();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);

        folderRecyclerView = view.findViewById(R.id.recyclerview_folder);
        folderAdapter = new FolderRecyclerViewAdapter(view.getContext());
        folderAdapter.setData(getListFolder());
        getListFolderFromFireBase();
        folderRecyclerView.setLayoutManager(layoutManager);
        folderRecyclerView.setAdapter(folderAdapter);
        folderAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_tab_folders, container, false);
    }

    private ArrayList<Folder> getListFolder()
    {
//        listFolder.add(new Folder("Hello", 2, R.drawable.images, "thanhhoa"));
//        listFolder.add(new Folder("Xin chao", 2, R.drawable.images, "thanhhoa11"));
//        listFolder.add(new Folder("Hello", 2, R.drawable.images, "thanhhoa"));
//        listFolder.add(new Folder("Hello", 2, R.drawable.images, "thanhhoa"));
//        listFolder.add(new Folder("Hello", 2, R.drawable.images, "thanhhoa"));
        return listFolder;
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