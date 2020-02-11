package com.harilee.bsafeadmin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;

public class Login extends AppCompatActivity implements Presenter.ViewInterface {


    @BindView(R.id.number_et)
    EditText numberEt;
    @BindView(R.id.login_bt)
    Button loginBt;
    private Presenter presenter;
    private Dialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ButterKnife.bind(this);
        dialog = new Dialog(this);
        presenter = new Presenter(this);
    }

    @OnClick(R.id.login_bt)
    public void onViewClicked() {

        String login = numberEt.getText().toString().trim();
        Utility.showGifPopup(this, true, dialog);
        presenter.loginUser(login);

    }

    @Override
    public void showMessage(String message) {
        Utility.showGifPopup(this, false, dialog);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getResponse(LoginModel loginModel) {
        Utility.showGifPopup(this, false, dialog);
        Utility.getUtilityInstance().setPreference(this, Config.LOGIN, "yes");
        Utility.getUtilityInstance().setPreference(this, Config.PASSENGER, loginModel.getPassenger());
        Utility.getUtilityInstance().setPreference(this, Config.ADMIN_ID, numberEt.getText().toString().trim());
        startActivity(new Intent(Login.this, Home.class));
    }
}
