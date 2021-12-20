package com.example.eyebrowsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import stream.customalert.CustomAlertDialogue;

public class MenuScreen extends AppCompatActivity {

    ImageView bg,clover,gallery,camera;
    LinearLayout textsplash,texthome,menus;
    Animation frombottom;

    int SELECT_IMAGE_CODE = 1;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_screen);

        frombottom = AnimationUtils.loadAnimation(this,R.anim.frombottom);

        gallery = (ImageView)findViewById(R.id.gallery);
        camera = (ImageView)findViewById(R.id.camera);

        bg = (ImageView)findViewById(R.id.bg);
        clover = (ImageView)findViewById(R.id.clover);

        textsplash = (LinearLayout)findViewById(R.id.textsplash);
        texthome = (LinearLayout)findViewById(R.id.texthome);
        menus = (LinearLayout)findViewById(R.id.menus);

        bg.animate().translationY(-1700).setDuration(1500).setStartDelay(300);
        clover.animate().alpha(0).setDuration(800).setStartDelay(600);
        textsplash.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(600);

        texthome.startAnimation(frombottom);
        menus.startAnimation(frombottom);

        /*YoYo.with(Techniques.Shake)
                .duration(2000)
                .repeat(10)
                .playOn(findViewById(R.id.menus));*/


        // Request for camera
        /*if(ContextCompat.checkSelfPermission(MenuScreen.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MenuScreen.this,new String[]{
                    Manifest.permission.CAMERA
            },100);
        }*/

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, SELECT_IMAGE_CODE);//one can be replaced with any action code
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_alert dialog = new dialog_alert(MenuScreen.this);
                //dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                dialog.show();
                Button Okay = dialog.findViewById(R.id.btn_okay);
                Button Cancel = dialog.findViewById(R.id.btn_cancel);

                Okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MenuScreen.this, cameraKit.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

                Cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    Intent intent = new Intent(MenuScreen.this,processScreen.class);
                    intent.putExtra("Img",uri.toString());
                    intent.putExtra("Check_g_OR_p","0");
                    startActivity(intent);
                }
                break;
        }

    }
}