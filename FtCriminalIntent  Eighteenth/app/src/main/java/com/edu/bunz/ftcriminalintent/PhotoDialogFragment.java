package com.edu.bunz.ftcriminalintent;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by asuss on 2017/11/1.
 */

public class PhotoDialogFragment extends DialogFragment{

    private static final String ARG_PHOTO = "photo";

    private ImageView mPhotoView;

    public static PhotoDialogFragment newInstance(File photoFile) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO, photoFile);
        PhotoDialogFragment fragment = new PhotoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        File photoFile = (File) getArguments().getSerializable(ARG_PHOTO);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_photo, null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        mPhotoView = (ImageView) v.findViewById(R.id.big_photo_view);
        final Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
        mPhotoView.setImageBitmap(bitmap);
        ViewTreeObserver observer = mPhotoView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

//                Point outSize = new Point();
//                display.getSize(outSize);
//                screenWidth = outSize.x;
//                screenHeight = outSize.y;
                Point size = new Point();
                getActivity().getWindowManager().getDefaultDisplay().getSize(size);
                int shift;
                //检测屏幕方向
                if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                   //竖屏
                    shift = 5;
                } else {
                    //横屏
                    shift = 100;
                }

                //设置对话框的宽和高，sixe.x 屏幕宽 size.y 屏幕的高
//                getDialog().getWindow().setLayout(size.x - shift, (size.x - shift) * bitmap.getHeight() / bitmap.getWidth() + 22);
                  getDialog().getWindow().setLayout(size.x - shift, (size.x - shift) * bitmap.getHeight() / bitmap.getWidth());

                mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }
}
