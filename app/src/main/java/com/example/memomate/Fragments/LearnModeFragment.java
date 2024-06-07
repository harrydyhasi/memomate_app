package com.example.memomate.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memomate.Activities.LearnModeActivity;
import com.example.memomate.Adapters.ChoiceAdapter;
import com.example.memomate.Models.Question;
import com.example.memomate.R;
import com.example.memomate.Utils.Utils;

public class LearnModeFragment extends Fragment {

    private View mView;
    private TextView txtQuestion;
    private RecyclerView rcvChoices;
    private LearnModeActivity mLearnModeActivity;

    public LearnModeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mLearnModeActivity = (LearnModeActivity) getActivity();
        mView =  inflater.inflate(R.layout.fragment_question, container, false);
        initView();
        Bundle bundleReceive = getArguments();
        if (bundleReceive != null)
        {
            Question question = (Question) bundleReceive.get("Question");
            if (question != null)
            {
                txtQuestion.setText(question.getQuestion());
                rcvChoices.setAdapter(new ChoiceAdapter(question.getChoices(), new ChoiceAdapter.IOnClickListener() {
                    @Override
                    public void onClick(String cont) {
                        if (question.isCorrect(cont))
                        {
                            showCorrectDialog();
                        }
                        else
                            showIncorrectDialog(question,cont);
                        mLearnModeActivity.increaseProgressBar();
                    }
                }));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext());
                rcvChoices.setLayoutManager(linearLayoutManager);
            }
        }
        return mView;
    }

    private void initView()
    {
        txtQuestion = mView.findViewById(R.id.txtQuestion);
        rcvChoices = mView.findViewById(R.id.rcvChoices);
    }

    private void showIncorrectDialog(Question A, String yourAnswer)
    {
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_incorrect);
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
        dialog.setCancelable(false);

        TextView txtQuestion, txtCorrect, txtYourAnswer;
        Button btnContinue;

        txtQuestion = dialog.findViewById(R.id.txtQuestion);
        txtQuestion.setText(A.getQuestion());
        txtCorrect = dialog.findViewById(R.id.txtCorrect);
        txtCorrect.setText(A.getAnswer());
        txtYourAnswer = dialog.findViewById(R.id.txtYourAnswer);
        txtYourAnswer.setText(yourAnswer);

        btnContinue = dialog.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mLearnModeActivity.nextQuestion();
            }
        });

        dialog.show();
    }

    private void showCorrectDialog()
    {
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_correct);
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
        dialog.setCancelable(false);

        dialog.show();

        Utils.delay(500, new Utils.DelayCallBack() {
            @Override
            public void afterDelay() {
                dialog.dismiss();
                mLearnModeActivity.nextQuestion();
            }
        });
    }

}