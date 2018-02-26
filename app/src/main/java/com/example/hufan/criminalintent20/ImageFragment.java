package com.example.hufan.criminalintent20;


import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v4.app.DialogFragment;//倒入此包相当重要

/**
 * Created by hufan on 2016/8/20.
 * 放大imageview中的图片
 */
public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH=
            "com.example.hufan.criminalintent2.0.image_path";

    public static ImageFragment newInstance(String imagePath){
        Bundle args=new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH,imagePath);

        ImageFragment fragment=new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);

        return fragment;
    }

    private ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mImageView=new ImageView(getActivity());
        String path=(String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image=PictureUtils.getScaledDrawable(getActivity(),path);
        mImageView.setImageDrawable(image);
        return mImageView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtils.cleanImageView(mImageView);
    }



}
