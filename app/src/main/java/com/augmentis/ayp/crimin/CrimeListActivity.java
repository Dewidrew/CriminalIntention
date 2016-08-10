package com.augmentis.ayp.crimin;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.augmentis.ayp.crimin.model.Crime;
import com.augmentis.ayp.crimin.model.CrimeLab;

import java.util.List;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {
    @Override
    protected Fragment onCreateFragment() {


        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            //single pane
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {

            CrimeFragment currentDetailFragment = (CrimeFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
            if (currentDetailFragment == null || !currentDetailFragment.getCrimeId().equals(crime.getId())) {
                //two pane
                Fragment newDetailFragment = CrimeFragment.newInstance(crime.getId());
                //replace old fragment with new one
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetailFragment).commit();
            }else{
                currentDetailFragment.getUpdateUI();
                System.out.println();
            }
        }
    }

    @Override
    public void onOpenSelectFirst() {
        if (findViewById(R.id.detail_fragment_container) != null) {
            //single pane
            List<Crime> crimeList = CrimeLab.getInstance(this).getCrime();
            if (crimeList != null && crimeList.size() > 0) {
                Crime crime = crimeList.get(0);

                Fragment newDetailFragment = CrimeFragment.newInstance(crime.getId());
                getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetailFragment).commit();

            }
        }

    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        Log.d("Augmentis", "Call back in CrimeListActivity");
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeDeleted() {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        CrimeFragment crimeFragment = (CrimeFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);


        listFragment.updateUI();

        //clear
        getSupportFragmentManager().beginTransaction().detach(crimeFragment).commit();
    }
}
