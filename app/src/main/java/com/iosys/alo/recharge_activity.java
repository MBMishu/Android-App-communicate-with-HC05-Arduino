package com.iosys.alo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class recharge_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextInputLayout number, trxid;
    String str_number, str_trxid;

//    side_header nav

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_recharge_activity);

        //        navigation drawer

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.design_navigation_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_recharge);

        trxid = findViewById(R.id.trxid);
        number = findViewById(R.id.number);

    }
    //        navigation drawer

    public void openNav(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), index_activity.class));
                finish();
                break;
            case R.id.nav_recharge:

                break;


            case R.id.nav_contact:

                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/iosysxyz"));
                    startActivity(myIntent);
                } catch (Exception e) {
                    Toast.makeText(this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                break;
            case R.id.nav_share:

                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                    String shareMessage = "https://github.com/MBMishu/Android-App-communicate-with-HC05-Arduino/blob/main/app-debug.apk";
                    intent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(intent, "Share Via"));
                } catch (Exception e) {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    private Boolean ValidateuserName() {
        if (number.getEditText().getText().toString().isEmpty()) {
            number.setError("can not be empty!");
            return false;

        } else {
            number.setError(null);
            number.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean ValidateTrxid() {
        if (trxid.getEditText().getText().toString().isEmpty()) {
            trxid.setError("can not be empty!");
            return false;

        }  else {
            trxid.setError(null);
            trxid.setErrorEnabled(false);
            return true;
        }
    }

    public void recharge(View view) {
        if (!ValidateuserName() | !ValidateTrxid()) {
            return;
        }
        str_number = number.getEditText().getText().toString().trim();
        str_trxid = trxid.getEditText().getText().toString().trim();

        String serial_number = "alo100";
        String serial_trxid_number = "12345678";

        if (str_number.equalsIgnoreCase(serial_number) && str_trxid.equalsIgnoreCase(serial_trxid_number)) {
            new SweetAlertDialog(recharge_activity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("রিচার্জ সফল")
                    .show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent= new Intent(getApplicationContext(), index_activity.class);

                    startActivity(intent);
                    finish();

                }
            },2000);

        }else {
            new SweetAlertDialog(recharge_activity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("কিছু একটা সমস্যা!")
                    .show();
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        Intent intent = new Intent(getApplicationContext(), index_activity.class);
        startActivity(intent);
        finish();

    }


}
