package com.example.memomate.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.memomate.Models.Intro;
import com.example.memomate.R;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class IntroAdapter extends RecyclerView.Adapter<IntroAdapter.IntroViewHolder> {

    ArrayList<Intro> list;
    Context context;

    public IntroAdapter(ArrayList<Intro> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public IntroAdapter.IntroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.intro_slide_item_container,parent,false);
        return new IntroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IntroAdapter.IntroViewHolder holder, int position) {
        Intro intro =list.get(position);
        holder.setImage(intro);
        holder.mTxtDesc.setText(intro.getDescription());
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        return 0;
    }

    public class IntroViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageBack;
        ImageView mImageSlide;
        TextView mTxtDesc;

        public IntroViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageBack = itemView.findViewById(R.id.imgBack);
            mImageSlide = itemView.findViewById(R.id.imgSlider);
            mTxtDesc = itemView.findViewById(R.id.txtDesc);

        }

        void setImage(Intro intro)
        {
            int cornerRadius = 60;
            int cropMargin = 10;
            Glide.with(context).load(intro.getImageBack()).into(mImageBack);
            Glide.with(context).load(intro.getImageSlider()).transform(new RoundedCornersTransformation(cornerRadius,cropMargin)).into(mImageSlide);
        }
    }
}
