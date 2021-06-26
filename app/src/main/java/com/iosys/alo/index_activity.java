package com.iosys.alo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class index_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnPaired;
    ListView devicelist;


//    side_header nav

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_index_activity);

        btnPaired = (Button) findViewById(R.id.button);
        devicelist = (ListView) findViewById(R.id.listView);

        //        navigation drawer

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.design_navigation_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        if ( myBluetooth==null ) {
            Toast.makeText(getApplicationContext(), "Bluetooth device not available", Toast.LENGTH_LONG).show();
            finish();
        } else if ( !myBluetooth.isEnabled() ) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevicesList();
            }
        });


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
                    String shareMessage = "https://www.facebook.com/iosysxyz"
                            + BuildConfig.APPLICATION_ID + "\n\n";
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

//    bluetooh

    private void pairedDevicesList () {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if ( pairedDevices.size() > 0 ) {
            for ( BluetoothDevice bt : pairedDevices ) {
                list.add(bt.getName().toString() + "\n" + bt.getAddress().toString());
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener);
    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length()-17);

            Intent i = new Intent(getApplicationContext(), home.class);
            i.putExtra(EXTRA_ADDRESS, address);
            startActivity(i);
        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


@Override
public void onBackPressed() {

    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Are you sure you want to close the app?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    index_activity.super.onBackPressed();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

    AlertDialog alertDialog = builder.create();
    alertDialog.show();
}
}
