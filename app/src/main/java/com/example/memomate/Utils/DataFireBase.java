package com.example.memomate.Utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.memomate.Adapters.ClassAdapter;
import com.example.memomate.Adapters.ClassRecyclerViewAdapter;
import com.example.memomate.Adapters.FolderAdapter;
import com.example.memomate.Adapters.FolderRecyclerViewAdapter;
import com.example.memomate.Adapters.MemberAdapter;
import com.example.memomate.Adapters.SearchResultAdapter;
import com.example.memomate.Adapters.StudySetAdapter;
import com.example.memomate.Adapters.StudySetRecyclerViewAdapter;
import com.example.memomate.Adapters.StudySetSelectAdapter;
import com.example.memomate.Models.Class;
import com.example.memomate.Models.FlashCard;
import com.example.memomate.Models.Member;
import com.example.memomate.Models.StudySet;
import com.example.memomate.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DataFireBase {
    private StudySetAdapter studySetAdapter;
    private FolderAdapter folderAdapter;
    private ClassAdapter classAdapter;
    private StudySetRecyclerViewAdapter studySetRcvAdapter;
    private FolderRecyclerViewAdapter folderRcvAdapter;
    private ClassRecyclerViewAdapter classRcvAdapter;
    private SearchResultAdapter setStudySetLvAdapter;
    private StudySetSelectAdapter setStudySetSelectAdapter;
    private MemberAdapter memberAdapter;

    public void addClass(Class c){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("classes/");

        DatabaseReference newPostRef = ref.push();
        String Id = newPostRef.getKey();
        c.setId(Id);
        newPostRef.setValue(c);
    }
public void updateClass(Class c){
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("classes/");
    DatabaseReference classRef = ref.child(c.getId());

    Map<String, Object> updates = new HashMap<>();
    updates.put("desc", c.getDesc());
    updates.put("right", c.getRight());
    updates.put("creator", c.getCreator());
    updates.put("title", c.getTitle());
    updates.put("studySetList", c.getStudySetList());
    updates.put("memberList", c.getMemberList());

    classRef.updateChildren(updates)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("UPDATE", "Class updated successfully");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("UPDATE", "Failed to update class: " + e.getMessage());
                }
            });
}

    public ArrayList<Class> getClassesByUid(String uid){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("classes");
        ArrayList<Class> classes = new ArrayList<>();
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSnapshot list = snapshot.child("memberList");
                for (DataSnapshot ns : list.getChildren()){
                    String id = ns.getValue(String.class);
                    if (id.equals(uid)){
                        Class c = new Class();
                        c.setId(snapshot.child("id").getValue(String.class));
                        c.setDesc(snapshot.child("desc").getValue(String.class));
                        c.setRight(snapshot.child("right").getValue(Boolean.class));
                        c.setCreator(snapshot.child("creator").getValue(String.class));
                        c.setTitle(snapshot.child("title").getValue(String.class));
                        c.setStudySetList(snapshot.child("studySetList").getValue(ArrayList.class));
                        ArrayList<String> studySets = new ArrayList<>();
                        DataSnapshot studySetList = snapshot.child("studySetList");
                        for (DataSnapshot data : studySetList.getChildren()){
                            String studySetId;
                            studySetId = data.getValue(String.class);
                            studySets.add(studySetId);
                        }
                        c.setStudySetList(studySets);
                        ArrayList<String> members = new ArrayList<>();
                        DataSnapshot memberList = snapshot.child("memberList");
                        for (DataSnapshot data : memberList.getChildren()){
                            String memberId;
                            memberId = data.getValue(String.class);
                            members.add(memberId);
                        }
                        c.setMemberList(members);
                        classes.add(c);
                        if(classAdapter == null){
                            classRcvAdapter.notifyDataSetChanged();
                        } else {
                            classAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Class c = new Class();
                c.setId(snapshot.child("id").getValue(String.class));
                c.setDesc(snapshot.child("desc").getValue(String.class));
                c.setRight(snapshot.child("right").getValue(Boolean.class));
                c.setCreator(snapshot.child("creator").getValue(String.class));
                c.setTitle(snapshot.child("title").getValue(String.class));
                c.setStudySetList(snapshot.child("studySetList").getValue(ArrayList.class));
                ArrayList<String> studySets = new ArrayList<>();
                DataSnapshot studySetList = snapshot.child("studySetList");
                for (DataSnapshot data : studySetList.getChildren()){
                    String studySetId;
                    studySetId = data.getValue(String.class);
                    studySets.add(studySetId);
                }
                c.setStudySetList(studySets);
                ArrayList<String> members = new ArrayList<>();
                DataSnapshot memberList = snapshot.child("memberList");
                for (DataSnapshot data : memberList.getChildren()){
                    String memberId;
                    memberId = data.getValue(String.class);
                    members.add(memberId);
                }
                c.setMemberList(members);
                for (int i=0; i<classes.size();i++)
                {
                    if (c.getId() == classes.get(i).getId())
                        classes.set(i, c);
                }
                if (classAdapter == null) {
                    classRcvAdapter.notifyDataSetChanged();
                } else {
                    classAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for (Class c : classes) {
                    if (c.getId().equals(snapshot.child("id").getValue(String.class))) {
                        classes.remove(c);
                        break;
                    }
                }
                if (classAdapter == null) {
                    classRcvAdapter.notifyDataSetChanged();
                } else {
                    classAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Query query = ref.orderByChild("memberList").equalTo(uid);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot classSnapshot : snapshot.getChildren()) {
//                    Class c = classSnapshot.getValue(Class.class);
//                    classes.add(c);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled event
//            }
//        });
        return classes;
    }
    public boolean deleteClass(String id){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("classes");
        ref.child(id).removeValue();

        return true;
    }
    public void removeMemberFromClass(String classId, String memberId) {
        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("classes").child(classId);
        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<String> memberList = new ArrayList<>();
                    DataSnapshot memberListSnapshot = snapshot.child("memberList");
                    for (DataSnapshot memberIdSnapshot : memberListSnapshot.getChildren()) {
                        String currentMemberId = memberIdSnapshot.getValue(String.class);
                        if (!currentMemberId.equals(memberId)) {
                            memberList.add(currentMemberId);
                        }
                    }
                    classRef.child("memberList").setValue(memberList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "Member removed from the class member list");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("TAG", "Failed to remove member from the class member list: " + e.getMessage());
                                }
                            });
                } else {
                    Log.d("TAG", "Class node not found");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Database error: " + error.getMessage());
            }
        });
    }

    public boolean deleteMemberOfClass(String classId, String uid){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("classes").child(classId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> memberList = new ArrayList<>();
                DataSnapshot memberListSnapshot = snapshot.child("memberList");
                for (DataSnapshot memberIdSnapshot : memberListSnapshot.getChildren()) {
                    String memberId = memberIdSnapshot.getValue(String.class);
                    if (!memberId.equals(uid)) {
                        memberList.add(memberId);
                    }
                    memberAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return true;
    }

    public ArrayList<Class> getAllClass(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("classes");
        ArrayList<Class> classes = new ArrayList<>();

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Class c = new Class();
                c.setId(snapshot.child("id").getValue(String.class));
                c.setDesc(snapshot.child("desc").getValue(String.class));
                c.setRight(snapshot.child("right").getValue(Boolean.class));
                c.setCreator(snapshot.child("creator").getValue(String.class));
                c.setTitle(snapshot.child("title").getValue(String.class));
                c.setStudySetList(snapshot.child("studySetList").getValue(ArrayList.class));
                ArrayList<String> studySets = new ArrayList<>();
                DataSnapshot studySetList = snapshot.child("studySetList");
                for (DataSnapshot data : studySetList.getChildren()){
                    String studySetId;
                    studySetId = data.getValue(String.class);
                    studySets.add(studySetId);
                }
                c.setStudySetList(studySets);
                ArrayList<String> members = new ArrayList<>();
                DataSnapshot memberList = snapshot.child("memberList");
                for (DataSnapshot data : memberList.getChildren()){
                    String memberId;
                    memberId = data.getValue(String.class);
                    members.add(memberId);
                }
                c.setMemberList(members);
                classes.add(c);
                if(classAdapter == null){
                    classRcvAdapter.notifyDataSetChanged();
                } else {
                    classAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return classes;
    }
    public ArrayList<User> getMemberListByMemberId(ArrayList<String> ids){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ArrayList<User> members = new ArrayList<>();

            DatabaseReference myRef = database.getReference("User");
            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    for (String id : ids) {
                        if (id.equals(snapshot.getKey())){
                            User user = new User();
                            user.setUserName(snapshot.child("userName").getValue(String.class));
                            user.setEmail(snapshot.child("email").getValue(String.class));
                            user.setAvatar(snapshot.child("avatar").getValue(String.class));
                            members.add(user);
                            memberAdapter.notifyDataSetChanged();
                        }
                    }

                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    for (User member : members) {
                        if (member.getUserName().equals(snapshot.child("id").child("userName").getValue(String.class))) {
                            members.remove(member);
                            break;
                        }
                    }
                    memberAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        return members;
    }
    public ArrayList<StudySet> getAllStudySetsByUserName(String username){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users/" + username + "/Folders/");
        ArrayList<StudySet> studySets = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DataSnapshot list = dataSnapshot.child("studySetList");
                    for (DataSnapshot data : list.getChildren()){
                        StudySet studySet = new StudySet();
                        studySet.setName(data.child("name").getValue(String.class));
                        studySet.setAvatar(data.child("avatar").getValue(String.class));
                        studySet.setUserName(data.child("userName").getValue(String.class));
                        ArrayList<FlashCard> flashCards = new ArrayList<>();
                        DataSnapshot flashCardList = data.child("flashCardList");
                        for (DataSnapshot d : flashCardList.getChildren()){
                            flashCards.add(d.getValue(FlashCard.class));
                        }
                        studySet.setFlashCardList(flashCards);
                        studySets.add(studySet);
                    }
                }
                if(studySetAdapter == null){
                    if(studySetRcvAdapter == null)
                        setStudySetSelectAdapter.notifyDataSetChanged();
                    else
                        studySetRcvAdapter.notifyDataSetChanged();
                } else {
                    studySetAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return studySets;

    }
    public ArrayList<Class> getAllClassesByUserName(String username){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users/" + username + "/Classes/");
        ArrayList<Class> classes = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Class c = new Class();
                    c.setTitle(dataSnapshot.child("title").getValue(String.class));
                    ArrayList<StudySet> studySets = new ArrayList<>();
                    DataSnapshot studySetList = dataSnapshot.child("studySetList");
                    for (DataSnapshot data : studySetList.getChildren()){
                        StudySet studySet = new StudySet();
                        studySet.setName(data.child("name").getValue(String.class));
                        studySet.setAvatar(data.child("avatar").getValue(String.class));
                        studySet.setUserName(data.child("userName").getValue(String.class));
                        ArrayList<FlashCard> flashCards = new ArrayList<>();
                        DataSnapshot flashCardList = data.child("flashCardList");
                        for (DataSnapshot d : flashCardList.getChildren()){
                            flashCards.add(d.getValue(FlashCard.class));
                        }
                        studySet.setFlashCardList(flashCards);
                        studySets.add(studySet);
                    }
//                    c.setStudySetList(studySets);
                    ArrayList<Member> members = new ArrayList<>();
                    DataSnapshot memberList = dataSnapshot.child("memberList");
                    for (DataSnapshot data : memberList.getChildren()){
                        Member member = new Member();
                        member.setAvatar(data.child("avatar").getValue(String.class));
                        member.setUserName(data.child("userName").getValue(String.class));
                        members.add(member);
                    }
//                    c.setMemberList(members);
                    classes.add(c);
                }
                if(classAdapter == null){
                    classRcvAdapter.notifyDataSetChanged();
                } else {
                    classAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return classes;
    }
    public ArrayList<String> getStudySetNames(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("StudySets");
        ArrayList<String> studySetNames = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String studySetName = dataSnapshot.child("name").getValue(String.class);
                    studySetNames.add(studySetName);
                }
                Log.d("SO LUONG", String.valueOf(studySetNames.size()));
                setStudySetLvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return studySetNames;

    }
    public void setClassRcvAdapter(ClassRecyclerViewAdapter classAdapter){
        this.classRcvAdapter = classAdapter;
    }
    public void setStudySetLvAdapter(SearchResultAdapter adapter){
        this.setStudySetLvAdapter = adapter;
    }
    public void setMemberAdapter(MemberAdapter adapter){
        this.memberAdapter = adapter;
    }

    public void setClassAdapter(ClassAdapter classAdapter) {
        this.classAdapter = classAdapter;
    }
}
