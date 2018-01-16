package com.techindustan.dailywisdom.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.tvBackToLogin)
    TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnRegister)
    public void Register() {
        if (isValid()) {
            Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @OnClick(R.id.tvBackToLogin)
    public void openLoginScreen() {
        finish();
    }


    boolean isValid() {
        if (etUserName.getText().toString().trim().isEmpty()) {
            etUserName.requestFocus();
            etUserName.setError(getResources().getString(R.string.name_can_not_be_empty));
            return false;
        } else if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.requestFocus();
            etEmail.setError(getResources().getString(R.string.email_can_not_be_empty));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.requestFocus();
            etEmail.setError(getResources().getString(R.string.invalid_email));
            return false;
        } else if (etPassword.getText().toString().trim().equalsIgnoreCase("")) {
            etPassword.requestFocus();
            etPassword.setError(getResources().getString(R.string.password_can_not_empty));
            return false;
        } else if (etPassword.getText().toString().length() < 4) {
            etPassword.requestFocus();
            etPassword.setError(getResources().getString(R.string.password_length));
            return false;
        }


        return true;

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
