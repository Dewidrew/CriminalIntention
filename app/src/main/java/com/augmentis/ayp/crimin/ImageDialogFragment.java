package com.augmentis.ayp.crimin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.augmentis.ayp.crimin.model.PictureUtils;

import java.io.File;

/**
 * Created by Hattapong on 8/4/2016.
 */
public class ImageDialogFragment extends DialogFragment {
    File photoFile;
    ImageView photoView;
    public static ImageDialogFragment newInstance(File photoFile) {
        ImageDialogFragment df = new ImageDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARG_PHOTOFILE", photoFile);
        df.setArguments(args);
        return df;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.photoFile = (File) getArguments().getSerializable("ARG_PHOTOFILE");

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.image_dialog, null);
        photoView = (ImageView) v.findViewById(R.id.image_dialog);
        updatePhotoView();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
//        builder.setTitle(R.string.image_dialog_title);
        builder.setPositiveButton(android.R.string.ok, null);

        return builder.create();
    }

    private void updatePhotoView() {
        if (photoFile == null || !photoFile.exists()) {
            photoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
            photoView.setImageBitmap(bitmap);
        }
    }
}
