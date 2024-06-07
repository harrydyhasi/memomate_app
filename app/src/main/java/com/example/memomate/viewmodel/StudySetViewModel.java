package com.example.memomate.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.memomate.FBModel.StudySetModel;
import com.example.memomate.repository.StudySetRepository;

import java.util.List;

public class StudySetViewModel extends ViewModel {
    private StudySetRepository studySetRepository;
    private LiveData<List<StudySetModel>> mListStudySetLiveData;

    public StudySetViewModel(String uid)
    {
        studySetRepository = new StudySetRepository(uid);
        //mListStudySetLiveData = studySetRepository.getAll();
        mListStudySetLiveData = studySetRepository.getTest();
    }


    public void addStudySet(StudySetModel studySet)
    {
        studySetRepository.addStudySet(studySet);
    }

    public LiveData<List<StudySetModel>> getListStudySetLiveData() {
        return mListStudySetLiveData;
    }

    public LiveData<List<StudySetModel>> getListStudySetLiveDataAll() {
        return studySetRepository.getAllUser();
    }

    public void update(StudySetModel A)
    {
        studySetRepository.update(A);
    }

    public void delete(String id)
    {
        studySetRepository.delete(id);
    }
}
