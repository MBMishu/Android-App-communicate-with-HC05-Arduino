package com.iosys.alo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.UUID;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    //    side_header nav

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    Button btn1, btn2,btn3;


    String address = null;
    TextView lumn;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent newint = getIntent();
        address = newint.getStringExtra(index_activity.EXTRA_ADDRESS);

        setContentView(R.layout.activity_home);

        btn1 = (Button) findViewById(R.id.button2);
        btn2 = (Button) findViewById(R.id.button3);
        btn3 = (Button) findViewById(R.id.button4);

        new ConnectBT().execute();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                sendSignal("1");
//                Toast.makeText(home.this, "btn1", Toast.LENGTH_SHORT).show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                sendSignal("2");
//                Toast.makeText(home.this, "btn2", Toast.LENGTH_SHORT).show();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Disconnect();
//                Toast.makeText(home.this, address, Toast.LENGTH_SHORT).show();
            }
        });

        //        navigation drawer

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.design_navigation_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    private void sendSignal ( String number ) {
        if ( btSocket != null ) {
            try {

                btSocket.getOutputStream().write(number.toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }
    private void Disconnect () {
        if ( btSocket!=null ) {
            try {
                btSocket.close();
            } catch(IOException e) {
                msg("Error");
            }
        }

        finish();
    }
    private void msg (String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    //        navigation drawer

    public void openNav(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:

                break;
            case R.id.nav_recharge:
                startActivity(new Intent(getApplicationContext(), recharge_activity.class));
                finish();
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

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected  void onPreExecute () {
            progress = ProgressDialog.show(home.this, "Connecting...", "Please Wait!!!");
        }

        @Override
        protected Void doInBackground (Void... devices) {
            try {
                if ( btSocket==null || !isBtConnected ) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute (Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a  Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected");
                isBtConnected = true;
            }

            progress.dismiss();
        }
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        if ( btSocket!=null ) {
            try {
                btSocket.close();
            } catch(IOException e) {
                msg("Error");
            }
        }
        Intent intent = new Intent(getApplicationContext(), index_activity.class);
        startActivity(intent);
        finish();


    }
}
