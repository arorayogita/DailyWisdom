package com.techindustan.dailywisdom.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DayTimeActivity extends AppCompatActivity {

    @BindView(R.id.tvHeader)
    TextView tvHeader;
    public static TextView tvChooseDay;
    public static TextView tvChooseTime;
    @BindView(R.id.btnSave)
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_time);
        tvChooseDay = (TextView) findViewById(R.id.tvChooseDay);
        tvChooseTime = (TextView) findViewById(R.id.tvChooseTime);
        ButterKnife.bind(this);
        tvChooseDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        tvChooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker();
            }
        });

    }


    public void openDatePicker() {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle b = new Bundle();
        b.putString("date", tvChooseDay.getText().toString());
        newFragment.setArguments(b);
        newFragment.show(getFragmentManager(), "datePicker");

    }

    public void openTimePicker() {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle b = new Bundle();
        b.putString("time", tvChooseTime.getText().toString());
        newFragment.setArguments(b);
        newFragment.show(getFragmentManager(), "timePicker");

    }

    @OnClick(R.id.btnSave)
    public void saveDetails() {

        Toast.makeText(this, "Details Saved Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            Calendar currentInstance = Calendar.getInstance();
            int year, month, day;
            boolean isFromPlanning = false;
            Bundle b = getArguments();
            if (b != null) {
                String time = b.getString("date");
                if (!time.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date d = sdf.parse(time);
                        c.setTime(d);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMinDate(currentInstance.getTimeInMillis());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            tvChooseDay.setText(year + "-" + (month + 1) + "-" + day);

        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int hour, minute;
            Bundle b = getArguments();
            if (b != null) {
                String time = b.getString("time");
                if (!time.isEmpty()) {

                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    try {
                        sdf.parse(time);
                        c = sdf.getCalendar();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = hourOfDay + ":" + minute;
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                final Date dateObj = sdf.parse(time);
                String formatedTime = new SimpleDateFormat("HH:mm").format(dateObj);
                tvChooseTime.setText(formatedTime);
            } catch (final ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
