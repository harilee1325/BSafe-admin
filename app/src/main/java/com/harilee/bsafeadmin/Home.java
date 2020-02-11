package com.harilee.bsafeadmin;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity implements Presenter.HomeInterface {

    @BindView(R.id.pic_user)
    CircularImageView picUser;
    @BindView(R.id.admin_name)
    TextView adminName;
    @BindView(R.id.accept_rides)
    Switch acceptRides;
    @BindView(R.id.vehicle_num)
    TextView vehicleNum;
    @BindView(R.id.navigate_bt)
    Button navigateBt;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.contact_num)
    TextView contactNum;
    @BindView(R.id.admin_name_tv)
    TextView adminNameTv;
    @BindView(R.id.nearby_card)
    CardView nearbyCard;
    @BindView(R.id.card_passenger)
    CardView cardPassenger;
    private Presenter presenter;
    private Dialog dialog;
    private String TAG = "Home";
    private String lat, lng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        dialog = new Dialog(this);
        presenter = new Presenter(this);
        Log.e(TAG, "onCreate: " + Utility.getUtilityInstance().getPreference(this, Config.FCM));
        getAdminDetails();
        getPassengerData();
    }

    private void getPassengerData() {
        String passenger = Utility.getUtilityInstance().getPreference(this, Config.PASSENGER);
        if (!passenger.equalsIgnoreCase("0")) {
            Utility.showGifPopup(this, true, dialog);
            presenter.getPassenger(Utility.getUtilityInstance().getPreference(this, Config.PASSENGER)
            ,Utility.getUtilityInstance().getPreference(this, Config.ADMIN_ID));
        } else {
            cardPassenger.setVisibility(View.GONE);
        }
    }

    private void getAdminDetails() {

        Utility.showGifPopup(this, true, dialog);
        presenter.getAdminDetails(Utility.getUtilityInstance().getPreference(this, Config.ADMIN_ID));
    }

    @OnClick(R.id.navigate_bt)
    public void onViewClicked() {
        Utility.showGifPopup(this, true, dialog);
        presenter.sendNotification(Utility.getUtilityInstance().getPreference(this, Config.FCM_USER));
    }

    @Override
    public void showMessages(String message) {
        Utility.showGifPopup(this, false, dialog);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getResponse(AdminModel adminModel) {

        Utility.showGifPopup(this, false, dialog);
        adminName.setText(adminModel.getResult().getName());
        vehicleNum.setText(adminModel.getResult().getVehicleNumber());
        Log.e(TAG, "getResponse: "+adminModel.getResult().getStatus() );
        if (adminModel.getResult().getStatus().equalsIgnoreCase("1")) {
            acceptRides.setChecked(true);
            cardPassenger.setVisibility(View.VISIBLE);

        } else {
            acceptRides.setChecked(false);
            cardPassenger.setVisibility(View.GONE);
        }
    }

    @Override
    public void getResponse(PassModel passModel) {

        Utility.showGifPopup(this, false, dialog);
        nameTv.setText(passModel.getName());
        contactNum.setText(passModel.getNumber());
        adminNameTv.setText(adminName.getText().toString().trim());
        Log.e(TAG, "getResponse: "+passModel.getFcm() );
        Utility.getUtilityInstance().setPreference(this, Config.FCM_USER, passModel.getFcm());
        lat = passModel.getLat();
        lng = passModel.getLng();
        Toast.makeText(getApplicationContext(), passModel.getLat(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getResponse() {
        Utility.showGifPopup(this, false, dialog);
        Uri gmmIntentUri = Uri.parse("geo:"+lat+","+lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}
