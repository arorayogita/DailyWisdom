package com.techindustan.dailywisdom.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.activity.LoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by android on 16/1/18.
 */

public class DayTimeFragment extends Fragment {
    Unbinder unbinder;
    View v;
    public static TextView tvChooseDay;
    public static TextView tvChooseTime;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivSignOut)

    ImageView ivSignOut;
    @BindView(R.id.btnSave)
    Button btnSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_day_time, container, false);
        unbinder = ButterKnife.bind(this, v);
        tvChooseDay = (TextView) v.findViewById(R.id.tvChooseDay);
        tvChooseTime = (TextView) v.findViewById(R.id.tvChooseTime);
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
        return v;
    }

    public void openDatePicker() {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle b = new Bundle();
        b.putString("date", tvChooseDay.getText().toString());
        newFragment.setArguments(b);
        newFragment.show(getActivity().getFragmentManager(), "datePicker");

    }

    public void openTimePicker() {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle b = new Bundle();
        b.putString("time", tvChooseTime.getText().toString());
        newFragment.setArguments(b);
        newFragment.show(getActivity().getFragmentManager(), "timePicker");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick({R.id.ivBack, R.id.ivSignOut, R.id.btnSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                getFragmentManager().popBackStack();
                break;
            case R.id.ivSignOut:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btnSave:
                Toast.makeText(getActivity(), "Details Saved Successfully", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
                break;
        }
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
