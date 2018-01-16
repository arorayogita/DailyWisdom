package com.techindustan.dailywisdom.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.techindustan.dailywisdom.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ForgotActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.btnResetPassword)
    Button btnResetPassword;
    @BindView(R.id.tvBackToLogin)
    TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tvBackToLogin)
    public void backToLogin() {
        finish();

    }

    @OnClick(R.id.btnResetPassword)
    public void ResetPassword() {
        if (isValid()) {
            Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }

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
        }
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
