package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hattapong on 7/28/2016.
 */
public class TimePickerFragment extends DialogFragment implements DialogInterface.OnClickListener {
    protected static final String EXTRA_TIME = "extra_time";
    private Date tempDate;

    public static TimePickerFragment newInstance(Date time) {
        TimePickerFragment df = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARG_TIME", time);
        df.setArguments(args);
        return df;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        // DatePicker -----> Model
        int hour = _timePicker.getHour();
        int min = _timePicker.getMinute();

        Calendar c = Calendar.getInstance();
        c.setTime(tempDate);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, min);
        Date date = c.getTime();
        sendResult(Activity.RESULT_OK, date);
    }

    private void sendResult(int resultCode, Date date) {
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }


    TimePicker _timePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        tempDate = (Date) getArguments().getSerializable("ARG_TIME");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tempDate);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);
        _timePicker = (TimePicker) v.findViewById(R.id.time_picker_in_dialog);
        _timePicker.setMinute(min);
        _timePicker.setHour(hour);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(v);
        builder.setTitle(R.string.time_picker_title);
        builder.setPositiveButton(android.R.string.ok, this);
        builder.setNegativeButton(android.R.string.cancel, null);


        return builder.create();
    }

}
