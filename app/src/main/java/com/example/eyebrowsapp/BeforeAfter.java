package com.example.eyebrowsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class BeforeAfter extends AppCompatActivity {

    ImageView img_save;
    Button btn_save,btn_share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_after);

        img_save =(ImageView)findViewById(R.id.img_save) ;
        btn_save =(Button)findViewById(R.id.btn_save);
        btn_share =(Button)findViewById(R.id.btn_share);

        byte[] bytes = getIntent().getByteArrayExtra("sendImgBefore_After");
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img_save.setImageBitmap(bmp);

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
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startActivity(Intent.createChooser(intent,"Share image Via"));
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) img_save.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                saveImageToGallery(bitmap);
            }
        });
    }
    private void saveImageToGallery(Bitmap bitmap) {
        FileOutputStream fos;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                ContentResolver resolver = getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,"BeforeAfter_"+".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+File.separator+"TestFolder");
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