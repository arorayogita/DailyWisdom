package com.techindustan.dailywisdom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by android on 15/1/18.
 */

public class SettingsFragment extends Fragment {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.ivSignOut)
    ImageView ivSignOut;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.rlChangePass)
    RelativeLayout rlChangePass;
    @BindView(R.id.rlNotificationTimeChange)
    RelativeLayout rlNotificationTimeChange;
    @BindView(R.id.fragment)
    FrameLayout fragment;
    Unbinder unbinder;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_settings, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();

    }



    @OnClick({R.id.ivSignOut, R.id.rlChangePass, R.id.rlNotificationTimeChange, R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivSignOut:
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.rlChangePass:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new ChangePasswordFragment()).addToBackStack(null).commit();

                break;
            case R.id.rlNotificationTimeChange:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new DayTimeFragment()).addToBackStack(null).commit();

                break;
            case R.id.ivBack:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
