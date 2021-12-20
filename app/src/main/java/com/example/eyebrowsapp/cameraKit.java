package com.example.eyebrowsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;

public class cameraKit extends AppCompatActivity {
    private CameraView cameraView;
    ImageView imageView,mask;

    ImageButton takepicture ,filp_btn ,ok_btn ,back_btn;

    int chcekflip = 0; // เช็คกล้องหน้ากล้องหลัง

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_kit);

        cameraView = findViewById(R.id.camera);
        filp_btn=(ImageButton)findViewById(R.id.filp_btn);
        takepicture=(ImageButton)findViewById(R.id.takepicture);

        imageView = (ImageView)findViewById(R.id.img);
        mask = (ImageView)findViewById(R.id.mask);

        ok_btn = (ImageButton)findViewById(R.id.ok_btn);
        back_btn = (ImageButton)findViewById(R.id.back_btn);

        takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.takePicture();
            }
        });

        filp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.toggleFacing();
                if(chcekflip == 0){
                    chcekflip = 1;
                }else if(chcekflip == 1) {
                    chcekflip = 0;
                }
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cameraKit.this, processScreen.class);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                BitmapDrawable drawable=(BitmapDrawable)imageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("Check_g_OR_p","1");
                intent.putExtra("sendCam",bytes);
                finish();
                startActivity(intent);
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takepicture.setVisibility(View.VISIBLE);
                filp_btn.setVisibility(View.VISIBLE);
                cameraView.setVisibility(View.VISIBLE);
                mask.setVisibility(View.VISIBLE);
                /////////////////
                imageView.setVisibility(View.INVISIBLE);
                back_btn.setVisibility(View.INVISIBLE);
                ok_btn.setVisibility(View.INVISIBLE);

                cameraView.open();
            }
        });

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(PictureResult result) {
                super.onPictureTaken(result);
                result.toBitmap(cameraView.getWidth(),cameraView.getHeight(),(BitmapCallback)(new BitmapCallback(){
                    public final void onBitmapReady(@Nullable Bitmap bitmap) {
                        if(chcekflip == 0){
                            imageView.setImageBitmap(flipImg(bitmap));
                        }else if (chcekflip == 1){
                            imageView.setImageBitmap(bitmap);
                        }
                        //imageView.setImageBitmap(flipImg(bitmap));
                        cameraView.close();
                        takepicture.setVisibility(View.INVISIBLE);
                        filp_btn.setVisibility(View.INVISIBLE);
                        cameraView.setVisibility(View.INVISIBLE);
                        mask.setVisibility(View.INVISIBLE);
                        /////
                        back_btn.setVisibility(View.VISIBLE);
                        ok_btn.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }
                }));

            }
        });

    }

    public Bitmap flipImg(Bitmap bitmap){
        Bitmap bitmapInput= bitmap, bitmapOutput;
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        bitmapOutput = Bitmap.createBitmap(bitmapInput, 0, 0, bitmapInput.getWidth(), bitmapInput.getHeight(), matrix, true);
        return bitmapOutput;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.close();
    }

}