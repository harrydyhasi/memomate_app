package com.example.memomate.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.memomate.FBModel.StudySetModel;
import com.example.memomate.repository.FolderRepository;
import com.example.memomate.repository.StudySetRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class FolderViewModel extends ViewModel {
    FolderRepository folderRepository;
    StudySetRepository studySetRepository;

    public FolderViewModel(String uid)
    {
        folderRepository = new FolderRepository(uid);
        studySetRepository = new StudySetRepository(uid);
    }

    public void addListStudySet(String idFolder, List<String> studySetModelIds)
    {
        for (int i=0; i<studySetModelIds.size();i++)
            folderRepository.addStudySetToFolder(idFolder, studySetModelIds.get(i));
    }

    public List<StudySetModel> getStudySet(String idFolder)
    {
        List<String> idStudySets = folderRepository.getStudySet(idFolder);
        return studySetRepository.getStudySetFromFolder(idStudySets);
    }
}
