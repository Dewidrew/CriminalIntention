package com.augmentis.ayp.crimin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.augmentis.ayp.crimin.model.Crime;
import com.augmentis.ayp.crimin.model.CrimeLab;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends SingleFragmentActivity implements CrimeFragment.Callbacks{
    private UUID _crimeId;
    protected static final String CRIME_ID = "crimeActivity.crimeId";

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_single_fragment;
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//
//    }

    @Override
    protected Fragment onCreateFragment() {
        _crimeId = (UUID) getIntent().getSerializableExtra(CRIME_ID);
        return CrimeFragment.newInstance(_crimeId);
    }


    public static Intent newIntent(Context activity, UUID id) {
        Intent intent = new Intent(activity, CrimePagerActivity.class);
        intent.putExtra(CRIME_ID, id);
        return intent;
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        //TODO I will see what I can do here.
    }

    @Override
    public void onCrimeDeleted() {
        finish();
    }
}
