package com.techindustan.dailywisdom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.techindustan.dailywisdom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.tvForgotPass)
    TextView tvForgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        etEmail.setText("yogita@gmail.com");
        etPassword.setText("12345");

    }

    @OnClick(R.id.btnLogin)
    public void Login() {
        if (isValid()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @OnClick(R.id.tvRegister)
    public void Register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);


    }

    @OnClick(R.id.tvForgotPass)
    public void ForgotPassword() {
        Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(intent);

    }

    boolean isValid() {
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.requestFocus();
            etEmail.setError(getResources().getString(R.string.email_can_not_be_empty));
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
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

}
