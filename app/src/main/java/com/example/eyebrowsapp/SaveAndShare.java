package com.example.eyebrowsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class SaveAndShare extends AppCompatActivity {

    ImageView img_save;
    ImageView Imgb_a;

    Button btn_save,btn_share;

    LinearLayout btn_b_a;

    BitmapDrawable drawable;
    Bitmap bitmap;
    String imageString="";

    BitmapDrawable drawable1;
    Bitmap bitmap1;
    String imageString1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_and_share);

        img_save =(ImageView)findViewById(R.id.img_save) ;
        Imgb_a =(ImageView)findViewById(R.id.Imgb_a) ;

        btn_save =(Button)findViewById(R.id.btn_save);
        btn_share =(Button)findViewById(R.id.btn_share);

        btn_b_a =(LinearLayout)findViewById(R.id.btn_before_after);

        ActivityCompat.requestPermissions(SaveAndShare.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(SaveAndShare.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        byte[] bytes = getIntent().getByteArrayExtra("sendImg");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img_save.setImageBitmap(bmp);

        byte[] bytes_origi = getIntent().getByteArrayExtra("sendOrigi");
        Bitmap bmp1 = BitmapFactory.decodeByteArray(bytes_origi, 0, bytes_origi.length);
        Imgb_a.setImageBitmap(bmp1);

        BeforeAfter(Imgb_a,img_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) img_save.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                saveImageToGallery(bitmap);
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                BitmapDrawable drawable = (BitmapDrawable)img_save.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                File file = new File(getExternalCacheDir()+"/"+"Demo1"+".jpg");
                Intent intent = new Intent(Intent.ACTION_SEND);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(Intent.createChooser(intent,"Share image Via"));
            }
        });

        btn_b_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveAndShare.this,BeforeAfter.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                drawable=(BitmapDrawable)Imgb_a.getDrawable();
                bitmap=drawable.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("sendImgBefore_After",bytes);
                startActivity(intent);
                //Toast.makeText(SaveAndShare.this,"Image Saved",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void BeforeAfter(ImageView img1,ImageView img2){
        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));
        final Python py = Python.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                drawable=(BitmapDrawable)img1.getDrawable();
                bitmap=drawable.getBitmap();
                imageString=getStringImage(bitmap);

                drawable1=(BitmapDrawable)img2.getDrawable();
                bitmap1=drawable1.getBitmap();
                imageString1=getStringImage(bitmap1);

                PyObject pyo=py.getModule("before_after");
                //call module in .py file
                PyObject obj=pyo.callAttr("main",imageString,imageString1);
                //return value
                String str=obj.toString();


                //convert bytearray
                byte[]data=android.util.Base64.decode(str, Base64.DEFAULT);
                //conver to bitmap
                Bitmap bmp= BitmapFactory.decodeByteArray(data,0,data.length);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Imgb_a.setImageBitmap(bmp);
                    }
                });
            }
        }).start();
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        //store in bytearray
        byte[] imageBytes=baos.toByteArray();
        String encodedImage=android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void saveImageToGallery(Bitmap bitmap) {
        FileOutputStream fos;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"Image_"+".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,Environment.DIRECTORY_PICTURES+File.separator+"TestFolder");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

                fos = (FileOutputStream)resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                Objects.requireNonNull(fos);

                Toast.makeText(this,"Image Saved",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this,"Image Not Saved \n"+e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }
}