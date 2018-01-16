package com.techindustan.dailywisdom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;
import com.techindustan.dailywisdom.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by android on 15/1/18.
 */

public class ChangePasswordFragment extends Fragment {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.fragment)
    FrameLayout fragment;
    Unbinder unbinder;
    View v;
    @BindView(R.id.ivSignOut)
    ImageView ivSignOut;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_change_password, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
       /* ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.fragment);
        mContainer.removeAllViews();*/
    }

    @OnClick(R.id.ivBack)
    public void performBackOperation() {
        getFragmentManager().popBackStack();
    }

    @OnClick({R.id.ivSignOut, R.id.etPassword, R.id.etNewPassword, R.id.etConfirmPassword, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivSignOut:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.etPassword:
                break;
            case R.id.etNewPassword:
                break;
            case R.id.etConfirmPassword:
                break;
            case R.id.btnSubmit:

                Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                getFragmentManager().popBackStack();
                break;
        }
    }
}
