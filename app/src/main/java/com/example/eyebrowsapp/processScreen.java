package com.example.eyebrowsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.github.chrisbanes.photoview.PhotoView;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import stream.customalert.CustomAlertDialogue;

public class processScreen extends AppCompatActivity {
    PhotoView Img;
    ImageView Img_stay;
    String gORp;

    ImageView save,B_A;
    Drawable Original_img;
    Drawable remove_img;
    Drawable full_img;
    Drawable img_now;

    //take bitmap and bitmap drawable to get image form image view
    BitmapDrawable drawable;
    Bitmap bitmap;
    String imageString="";

    BitmapDrawable drawable1;
    Bitmap bitmap1;
    String imageString1="";

    ImageView btn1,btn2,btn3,btn4,btn5;

    RecyclerView recyclerView;

    IndicatorSeekBar slider;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_screen);

        Img=(PhotoView)findViewById(R.id.image);
        Img_stay = new ImageView(this);

        save =(ImageView)findViewById(R.id.save);
        B_A =(ImageView)findViewById(R.id.B_A);

        slider = findViewById(R.id.SeekBar);

        btn1 = (ImageView)findViewById(R.id.btn1);
        btn2 = (ImageView)findViewById(R.id.btn2);
        btn3 = (ImageView)findViewById(R.id.btn3);
        btn4 = (ImageView)findViewById(R.id.btn4);
        btn5 = (ImageView)findViewById(R.id.btn5);

        gORp = getIntent().getStringExtra("Check_g_OR_p");

        /////////////////recyclerView
        recyclerView = findViewById(R.id.recycler_view);

        // สร้าง Layout Manager Object
        LinearLayoutManager lm = new LinearLayoutManager(processScreen.this,RecyclerView.HORIZONTAL,false);
        // สร้าง Adapter Object
        MainModel[]items={
                new  MainModel(R.drawable.e1normal,"Normal",R.drawable.e1mask), //S-Shape
                new  MainModel(R.drawable.e1blonde,"Blonde",R.drawable.e1mask),
                new  MainModel(R.drawable.e1chocolate,"Chocolate",R.drawable.e1mask),
                new  MainModel(R.drawable.e1darkbrown,"DarkBrown",R.drawable.e1mask),
                new  MainModel(R.drawable.e1mediumbrown,"MediumBrown",R.drawable.e1mask),
                new  MainModel(R.drawable.e1softbrown,"SoftBrown",R.drawable.e1mask),
                new  MainModel(R.drawable.e1auburn,"Auburn",R.drawable.e1mask),
        };
        MyAdapter adapter = new MyAdapter(items);
        //เจ้าถึง RecyclerView  กับ Layout
        recyclerView.setLayoutManager(lm);//กำหนด layout manager ให้กับ RecyclerView
        recyclerView.setAdapter(adapter);//กำหนด  Adapter ให้กับRecyclerView
        btn1.setBackgroundResource(R.drawable.custom_btn_select);
        ////////////////////////////////////
        if(gORp.equals("0")){
            String imageUrl = getIntent().getStringExtra("Img");
            Uri mImageUri = Uri.parse(imageUrl);
            Img.setImageURI(mImageUri);
            drawable=(BitmapDrawable)Img.getDrawable();
            bitmap=drawable.getBitmap();
            Bitmap bitmap1  = reduceBitmapSize(bitmap,307200);
            Img.setImageBitmap(bitmap1);
            Original_img=Img.getDrawable();
            img_now = Img.getDrawable();
            full_img = Img.getDrawable();
        }else if(gORp.equals("1")){
            byte[] bytes = getIntent().getByteArrayExtra("sendCam");
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Bitmap bitmap1  = reduceBitmapSize(bmp,307200);
            Img.setImageBitmap(bitmap1);
            Original_img=Img.getDrawable();
            img_now = Img.getDrawable();
            full_img = Img.getDrawable();
            /*String imageUrl1 = getIntent().getStringExtra("Img1");
            Bitmap bitmap = BitmapFactory.decodeFile(imageUrl1);
            Bitmap bitmap1  = reduceBitmapSize(bitmap,307200);
            Img.setImageBitmap(bitmap1);
            Original_img=Img.getDrawable();
            img_now = Img.getDrawable();
            full_img = Img.getDrawable();*/
        }

        slider.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                value = seekParams.progress;
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                setAlpha(remove_img,full_img,value);
                //Toast.makeText(processScreen.this,String.valueOf(value),Toast.LENGTH_SHORT).show();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBtn();
                btn1.setBackgroundResource(R.drawable.custom_btn_select); //S-Shape
                MainModel[]items={
                        new  MainModel(R.drawable.e1normal,"Normal",R.drawable.e1mask), //S-Shape
                        new  MainModel(R.drawable.e1blonde,"Blonde",R.drawable.e1mask),
                        new  MainModel(R.drawable.e1chocolate,"Chocolate",R.drawable.e1mask),
                        new  MainModel(R.drawable.e1darkbrown,"DarkBrown",R.drawable.e1mask),
                        new  MainModel(R.drawable.e1mediumbrown,"MediumBrown",R.drawable.e1mask),
                        new  MainModel(R.drawable.e1softbrown,"SoftBrown",R.drawable.e1mask),
                        new  MainModel(R.drawable.e1auburn,"Auburn",R.drawable.e1mask),
                };
                MyAdapter adapter = new MyAdapter(items);
                recyclerView.setAdapter(adapter);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBtn();
                btn2.setBackgroundResource(R.drawable.custom_btn_select); //Rounded
                MainModel[]items={
                        new  MainModel(R.drawable.e2normal,"Normal",R.drawable.e2mask),
                        new  MainModel(R.drawable.e2blonde,"Blonde",R.drawable.e2mask),
                        new  MainModel(R.drawable.e2chocolate,"Chocolate",R.drawable.e2mask),
                        new  MainModel(R.drawable.e2darkbrown,"DarkBrown",R.drawable.e2mask),
                        new  MainModel(R.drawable.e2mediumbrown,"MediumBrown",R.drawable.e2mask),
                        new  MainModel(R.drawable.e2softbrown,"SoftBrown",R.drawable.e2mask),
                        new  MainModel(R.drawable.e2auburn,"Auburn",R.drawable.e2mask),
                };
                MyAdapter adapter = new MyAdapter(items);
                recyclerView.setAdapter(adapter);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBtn();
                btn3.setBackgroundResource(R.drawable.custom_btn_select); //Straight
                MainModel[]items={
                        new  MainModel(R.drawable.e3normal,"Normal",R.drawable.e3mask),
                        new  MainModel(R.drawable.e3blonde,"Blonde",R.drawable.e3mask),
                        new  MainModel(R.drawable.e3chocolate,"Chocolate",R.drawable.e3mask),
                        new  MainModel(R.drawable.e3darkbrown,"DarkBrown",R.drawable.e3mask),
                        new  MainModel(R.drawable.e3mediumbrown,"MediumBrown",R.drawable.e3mask),
                        new  MainModel(R.drawable.e3softbrown,"SoftBrown",R.drawable.e3mask),
                        new  MainModel(R.drawable.e3auburn,"Auburn",R.drawable.e3mask),
                };
                MyAdapter adapter = new MyAdapter(items);
                recyclerView.setAdapter(adapter);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBtn();
                btn4.setBackgroundResource(R.drawable.custom_btn_select); //Soft-Angled
                MainModel[]items={
                        new  MainModel(R.drawable.e4normal,"Normal",R.drawable.e4mask),
                        new  MainModel(R.drawable.e4blonde,"Blonde",R.drawable.e4mask),
                        new  MainModel(R.drawable.e4chocolate,"Chocolate",R.drawable.e4mask),
                        new  MainModel(R.drawable.e4darkbrown,"DarkBrown",R.drawable.e4mask),
                        new  MainModel(R.drawable.e4mediumbrown,"MediumBrown",R.drawable.e4mask),
                        new  MainModel(R.drawable.e4softbrown,"SoftBrown",R.drawable.e4mask),
                        new  MainModel(R.drawable.e4auburn,"Auburn",R.drawable.e4mask),
                };
                MyAdapter adapter = new MyAdapter(items);
                recyclerView.setAdapter(adapter);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearBtn();
                btn5.setBackgroundResource(R.drawable.custom_btn_select); //Hard-Angled
                MainModel[]items={
                        new  MainModel(R.drawable.e5normal,"Normal",R.drawable.e5mask),
                        new  MainModel(R.drawable.e5blonde,"Blonde",R.drawable.e5mask),
                        new  MainModel(R.drawable.e5chocolate,"Chocolate",R.drawable.e5mask),
                        new  MainModel(R.drawable.e5darkbrown,"DarkBrown",R.drawable.e5mask),
                        new  MainModel(R.drawable.e5mediumbrown,"MediumBrown",R.drawable.e5mask),
                        new  MainModel(R.drawable.e5softbrown,"SoftBrown",R.drawable.e5mask),
                        new  MainModel(R.drawable.e5auburn,"Auburn",R.drawable.e5mask),
                };
                MyAdapter adapter = new MyAdapter(items);
                recyclerView.setAdapter(adapter);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImg(Img);
            }
        });
        B_A.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Img.setImageDrawable(Original_img);
                        break;
                    case MotionEvent.ACTION_UP:
                        Img.setImageDrawable(img_now);
                        break;
                }
                return true;
            }
        });

    }

    public void clearBtn(){
        btn1.setBackgroundResource(R.drawable.custom_btn);
        btn2.setBackgroundResource(R.drawable.custom_btn);
        btn3.setBackgroundResource(R.drawable.custom_btn);
        btn4.setBackgroundResource(R.drawable.custom_btn);
        btn5.setBackgroundResource(R.drawable.custom_btn);
    }

    public void sendImg(PhotoView img){
        Intent intent = new Intent(processScreen.this,SaveAndShare.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        drawable=(BitmapDrawable)img.getDrawable();
        bitmap=drawable.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        BitmapDrawable d =(BitmapDrawable)Original_img;
        Bitmap bit = d.getBitmap();
        bit.compress(Bitmap.CompressFormat.JPEG,100,stream1);
        byte[] bytes_origi = stream1.toByteArray();

        intent.putExtra("sendImg",bytes);
        intent.putExtra("sendOrigi",bytes_origi);
        startActivity(intent);
    }
    public void setAlpha(Drawable origi, Drawable img,int val){
        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));
        final Python py = Python.getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                drawable=(BitmapDrawable)origi;
                bitmap=drawable.getBitmap();
                imageString=getStringImage(bitmap);

                drawable1=(BitmapDrawable)img;
                bitmap1=drawable1.getBitmap();
                imageString1=getStringImage(bitmap1);

                PyObject pyo=py.getModule("set_alpha");
                //call module in .py file
                PyObject obj=pyo.callAttr("main",imageString,imageString1,val);
                //return value
                String str=obj.toString();


                //convert bytearray
                byte[]data=android.util.Base64.decode(str, Base64.DEFAULT);
                //conver to bitmap
                Bitmap bmp= BitmapFactory.decodeByteArray(data,0,data.length);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Img.setImageBitmap(bmp);
                        img_now = Img.getDrawable();
                    }
                });
            }
        }).start();
    }
    public static Bitmap reduceBitmapSize(Bitmap bitmap,int MAX_SIZE) {
        double ratioSquare;
        int bitmapHeight, bitmapWidth;
        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();
        ratioSquare = (bitmapHeight * bitmapWidth) / MAX_SIZE;
        if (ratioSquare <= 1)
            return bitmap;
        double ratio = Math.sqrt(ratioSquare);
        Log.d("mylog", "Ratio: " + ratio);
        int requiredHeight = (int) Math.round(bitmapHeight / ratio);
        int requiredWidth = (int) Math.round(bitmapWidth / ratio);
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true);
    }
    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        //store in bytearray
        byte[] imageBytes=baos.toByteArray();
        String encodedImage=android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void processImage(ImageView imageView ,ImageView imageMask){
        CustomProressDialog dialog = new CustomProressDialog(processScreen.this);
        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));
        final Python py = Python.getInstance();

        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                drawable=(BitmapDrawable)Img.getDrawable();
                bitmap=drawable.getBitmap();
                imageString=getStringImage(bitmap);

                drawable1=(BitmapDrawable)imageMask.getDrawable();
                bitmap1=drawable1.getBitmap();
                imageString1=getStringImage(bitmap1);

                PyObject pyo0 =py.getModule("remove_eyebrows");
                //call module in .py file
                PyObject obj0 =pyo0.callAttr("main",imageString,imageString1);
                //return value
                String str0 = obj0.toString();
                //convert bytearray
                byte[]data0 = android.util.Base64.decode(str0, Base64.DEFAULT);
                //conver to bitmap
                Bitmap bmp0 = BitmapFactory.decodeByteArray(data0,0,data0.length);

                ImageView img_wait = new ImageView(processScreen.this); //

                //get image from image view
                drawable=(BitmapDrawable)Img.getDrawable();
                bitmap=drawable.getBitmap();
                imageString=getStringImage(bitmap);

                drawable1=(BitmapDrawable)imageView.getDrawable();
                bitmap1=drawable1.getBitmap();
                imageString1=getStringImage(bitmap1);

                //imageString we get encoded iamge string
                //pass this string in python script

                //call .py file
                PyObject pyo=py.getModule("myscript2");
                //call module in .py file
                PyObject obj=pyo.callAttr("main",imageString,imageString1);
                //return value
                String str=obj.toString();
                if(str.equals("null")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            CustomAlertDialogue.Builder alert = new CustomAlertDialogue.Builder(processScreen.this)
                                    .setStyle(CustomAlertDialogue.Style.DIALOGUE)
                                    .setTitle("Alert")
                                    //.setMessage("Cannot detect faces Please try again.")
                                    .setMessage("ไม่สามารถตรวจจับใบหน้าได้ กรุณาลองใหม่อีกครั้ง")
                                    .setNegativeText("OK")
                                    .setNegativeColor(R.color.negative)
                                    .setNegativeTypeface(Typeface.DEFAULT_BOLD)
                                    .setOnNegativeClicked(new CustomAlertDialogue.OnNegativeClicked() {
                                        @Override
                                        public void OnClick(View view, Dialog dialog) {
                                            finish();
                                            dialog.dismiss();
                                        }
                                    })
                                    .setDecorView(getWindow().getDecorView())
                                    .build();
                            alert.show();
                            //Toast.makeText(processScreen.this,"not fond face",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    //convert bytearray
                    byte[]data=android.util.Base64.decode(str, Base64.DEFAULT);
                    //conver to bitmap
                    Bitmap bmp= BitmapFactory.decodeByteArray(data,0,data.length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //set this bitmap to imageView2
                            img_wait.setImageBitmap(bmp0);
                            remove_img = img_wait.getDrawable();
                            Img_stay.setImageBitmap(bmp);
                            full_img = Img_stay.getDrawable();
                            img_now = Img_stay.getDrawable();
                            setAlpha(remove_img,full_img,50);
                            dialog.dismiss();
                        }
                    });
                }
            }
        }).start();
    }

    class MyAdapter extends RecyclerView.Adapter <MyAdapter.MyViewHolder> {
        int selectedPos = RecyclerView.NO_POSITION;
        MainModel[]items;
        public MyAdapter(MainModel[] mainModels) {items = mainModels;}
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.imageView.setImageResource(items[position].langEyebrows);
            holder.image_mask.setImageResource(items[position].langEyebrows_mask);
            holder.textView.setText(items[position].langName);
            holder.imageView.setSelected(selectedPos == position);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Img.setImageDrawable(Original_img);
                    slider.setProgress((float) 50);
                    processImage(holder.imageView,holder.image_mask);
                }
            });
        }
        @Override
        public int getItemCount() {
            return items.length;
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageView image_mask;
            TextView textView;
            LinearLayout linearLayout;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.image_view);
                image_mask = itemView.findViewById(R.id.image_mask);
                textView = itemView.findViewById(R.id.text_view);
                linearLayout = itemView.findViewById(R.id.linear);
            }

        }
    }
}

