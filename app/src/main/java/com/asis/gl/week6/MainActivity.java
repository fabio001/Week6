package com.asis.gl.week6;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String html="";
    Thread thread;
    RadioGroup radio;
    ImageView imgView;
    MediaPlayer mp;
    SharedPreferences shrdFile;
    EditText txt;
    SharedPreferences.Editor editor;
    private  int clickCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("ninjas", "oncreate is called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radio =(RadioGroup)findViewById(R.id.radioGroup);
        imgView= (ImageView)findViewById(R.id.imgView);
        shrdFile = this.getPreferences(MODE_PRIVATE);
        //mp = MediaPlayer.create(this,R.raw.h1);
        //mp.setLooping(true);
        //mp.start();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("http://www.ybu.edu.tr/muhendislik/bilgisayar/").get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements newsHeadlines = doc.select("div.caContent");
                html = newsHeadlines.text();
            }
        });
        thread.start();
        clickCount =0;
        radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.leo:
                        imgView.setImageResource(R.drawable.leo);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("ninjas", "Onstart called");
        txt = (EditText)findViewById(R.id.edtxt);
        clickCount = shrdFile.getInt("click",0);
        //mp = MediaPlayer.create(this,R.raw.h1);
        //mp.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("ninjas","Onresume is called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("ninjas", "Onpause is called");
        //mp.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("ninjas","Ondestroy called");
        editor = shrdFile.edit();
        editor.putInt("click",clickCount);
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("ninjas", "onstop called");
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("ninjas", "onsaveInstance was called");
        outState.putInt("clicks", clickCount);
        outState.putString("edit", txt.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v("ninjas", "onRestoreInstance was called");
        clickCount = savedInstanceState.getInt("clicks");
        txt.setText(savedInstanceState.getString("edit"));
    }

    public void btnClicked(View v){
        clickCount ++;
        Toast.makeText(this,"Clicked:" + clickCount,Toast.LENGTH_SHORT).show();
        txt.setText(html);
    }
}
