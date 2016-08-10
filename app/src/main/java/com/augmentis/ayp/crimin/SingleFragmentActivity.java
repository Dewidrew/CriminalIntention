package com.augmentis.ayp.crimin;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        Log.d(CrimeListFragment.TAG,"On create activity");


        //Create Fragment Container
        FragmentManager fm = getSupportFragmentManager();
        Fragment f =fm.findFragmentById(R.id.fragment_container);


        if( f == null){
            f = onCreateFragment(); // Create Fragment

            // Set Fragment to Fragment Container
            fm.beginTransaction()
                    .add(R.id.fragment_container,f)
                    .commit();
            Log.d(CrimeListFragment.TAG," Fragment is created");
        }else{
            Log.d(CrimeListFragment.TAG," Fragment have already been created");
        }
    }



    protected abstract Fragment onCreateFragment();
}
