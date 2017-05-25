package com.example.wzl.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> textList;
    private VerticalHorizontalTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setData();
        textView = (VerticalHorizontalTextView) findViewById(R.id.vertical_text_view);
        textView.setTextStillTime(4000);
        textView.setAnimTime(400,100);
        textView.setTextList(textList);
        textView.startScroll();
        textView.setListener(new VerticalHorizontalTextView.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                Toast.makeText(MainActivity.this,"item"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        textList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            textList.add("这是TextView中的第 "+(i+1)+" 个item");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (textView != null) {
            textView.startScroll();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textView != null) {
            textView.stopScroll();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textView != null) {
            textView.onDestroy();
        }
    }
}
