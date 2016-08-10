package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.augmentis.ayp.crimin.model.Crime;
import com.augmentis.ayp.crimin.model.CrimeLab;
import com.augmentis.ayp.crimin.model.PictureUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

/**
 * Created by Nutdanai on 7/18/2016.
 */
public class CrimeListFragment extends Fragment {
    private static final java.lang.String SUBTITLE_VISIBLE_STATE = "SUBTITLE";
    private TextView showNull;
    private RecyclerView _crimeRecyclerView;

    private CrimeAdapter _adapter;
    private boolean _subTitleVisible;

    protected static final String TAG = "CRIME_LIST";
    private int crimePos;
    private Callbacks callbacks;

    public interface Callbacks{
        void onCrimeSelected(Crime crime);
        void onOpenSelectFirst();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks)context;
        callbacks.onOpenSelectFirst();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        _crimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view); // Bind Recycle Layout
        _crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // set LinearLayout in Recycle Layout && get activity(CrimeListActivity) and send with layout

//        if(CrimeLab.getInstance(getActivity()).getCrime() != null && v.findViewById(R.id.detail_fragment_container) != null) {
//            Fragment newDetailFragment = CrimeFragment.newInstance(CrimeLab.getInstance(getActivity()).getCrime().get(0).getId());
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container, newDetailFragment).commit();
//        }
        showNull = (TextView) v.findViewById(R.id.crime_list_null);
        showNull.setVisibility(View.GONE);
        if (savedInstanceState != null) {
            _subTitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE_STATE);
        }

        updateUI();
        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_list_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_iten_show_subtitle);

        if (_subTitleVisible) {
            menuItem.setIcon(R.drawable.ic_visibility_white_24dp);
            menuItem.setTitle(R.string.hide_subtitle);
        } else {
            menuItem.setIcon(R.drawable.ic_visibility_off_white_24dp);
            menuItem.setTitle(R.string.show_subtitle);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime); // TODO: Add addCrime() to Crime
//                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
//                startActivity(intent);
                //support tablet
                updateUI();
                callbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_iten_show_subtitle:
                _subTitleVisible = !_subTitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SUBTITLE_VISIBLE_STATE, _subTitleVisible);
    }

    /**
     * Update UI
     */
    public void updateUI() {
        final CrimeLab crimeLab = CrimeLab.getInstance(getActivity());

        List<Crime> crimes = crimeLab.getCrime(); // create list

        if (_adapter == null) {
            _adapter = new CrimeAdapter(crimes); // set list to Adapter
            _crimeRecyclerView.setAdapter(_adapter); // set Adapter to recycleview

        } else {
            _adapter.setCrimes(crimeLab.getCrime());
            _adapter.notifyDataSetChanged();

            //           _adapter.notifyItemChanged(crimePos);
        }

        updateSubtitle();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Resume list");
        //notifyArrayPosition();
        updateUI();
    }


    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView _titleTextView;
        public TextView _dateTextView;
        public CheckBox _solvedCheckBox;
        int _position;
        Crime _crime;
        public ImageView _crimeImage;
        public File photoFile;

        public CrimeHolder(View itemView) {
            super(itemView);
            _titleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_title_text_view);
            _solvedCheckBox = (CheckBox)
                    itemView.findViewById(R.id.list_item_crime_solved_check_box);

            _dateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_date_text_view);
            _crimeImage = (ImageView)itemView.findViewById(R.id.image_crime_list);

            itemView.setOnClickListener(this);

        }
        private void updatePhotoView() {
            if (photoFile == null || !photoFile.exists()) {
                _crimeImage.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
                _crimeImage.setImageBitmap(bitmap);
            }
        }
        public void bind(Crime crime, int position) {
            _crime = crime;
            _position = position;
            _titleTextView.setText(_crime.getTitle());
            _dateTextView.setText(_crime.getCrimeDate().toString());
            _solvedCheckBox.setChecked(_crime.isSolved());
            photoFile =  CrimeLab.getInstance(getActivity()).getPhotoFile(_crime);
            _solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(_solvedCheckBox.isPressed()) {
                        _crime.setSolved(b);
                        Log.d("Augmentis","Show : "+b);
                        CrimeLab.getInstance(getActivity()).updateCrime(_crime);
                        callbacks.onCrimeSelected(_crime);
                    }
                }
            });
            updatePhotoView();
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "Send position ; " + _position);
            callbacks.onCrimeSelected(_crime);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> _crimes;
        private int viewCreatingCount;

        public CrimeAdapter(List<Crime> crimes) {
            this._crimes = crimes;
        }

        protected void setCrimes(List<Crime> crimes) {
            _crimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            viewCreatingCount++;
            Log.d(TAG, "Create view holder for CrimeList : creating view time = " + viewCreatingCount);
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(v);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Log.d(TAG, "Bind view holder for CrimeList : position = " + position);
            Crime crime = _crimes.get(position);
            holder.bind(crime, position);
        }

        @Override
        public int getItemCount() {
            return _crimes.size();
        }
    }

    public void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        int crimeCount = crimeLab.getCrime().size();
        if(crimeCount == 0){
            showNull.setVisibility(View.VISIBLE);
        }else{
            showNull.setVisibility(View.GONE);
        }
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_format, crimeCount,crimeCount);
        if(crimeCount == 0){
            subtitle = "0 Crime";
        }
//        String.format("%s %d","Hello",222);
        if (!_subTitleVisible) {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

}
