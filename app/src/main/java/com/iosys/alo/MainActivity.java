package com.iosys.alo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static int SPlash_Screen = 2000;

    Animation topAnim,bottomAnim;
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bootom_anim);


        imageView = findViewById(R.id.useLogo);
//        textView = findViewById(R.id.text_logo);

        imageView.setAnimation(topAnim);
//        textView.setAnimation(bottomAnim);
        Dexter.withContext(this)

        .withPermissions(Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        //                        splash screen


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent= new Intent(getApplicationContext(), index_activity.class);

                                startActivity(intent);
                                finish();

                            }
                        },SPlash_Screen);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError dexterError) {
                        Log.e("Dexter", "There was an error: " + dexterError.toString());
                    }
                })
                .check();
    }
}
