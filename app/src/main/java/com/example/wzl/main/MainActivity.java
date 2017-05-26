package com.example.wzl.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private List<String> textList;
    private VerticalHorizontalTextView textView,clickTextView;
    private VerticalHorizontalTextView horizontalTextView;
    private Button btn;
    private boolean isVertical = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setData();
        initView();
        setListener();
    }

    private void initView() {
        textView = (VerticalHorizontalTextView) findViewById(R.id.vertical_text_view);
        horizontalTextView = (VerticalHorizontalTextView) findViewById(R.id.horizontal_text_view);
        clickTextView = (VerticalHorizontalTextView) findViewById(R.id.click_text_view);
        btn = (Button) findViewById(R.id.btn);

        clickTextView.setTextStillTime(5000);
        clickTextView.setAnimTime(400,100);
        clickTextView.setTextList(textList);
        clickTextView.startScroll();

        textView.setTextStillTime(4000);
        textView.setAnimTime(400,100);
        textView.setTextList(textList);
        textView.startScroll();

        horizontalTextView.setTextStillTime(4000);
        horizontalTextView.setAnimTime(4000,1000);
        horizontalTextView.setTextList(textList);
        horizontalTextView.startScroll();
    }

    private void setListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVertical){
                    isVertical = false;
                    clickTextView.setTextStillTime(5000);
                    clickTextView.setAnimTime(5000,1000);
                    clickTextView.setOrientation(VerticalHorizontalTextView.SCROLL_HORIZONTAL);
                }else {
                    isVertical = true;
                    clickTextView.setTextStillTime(5000);
                    clickTextView.setAnimTime(400,100);
                    clickTextView.setOrientation(VerticalHorizontalTextView.SCROLL_VERTICAL);
                }
            }
        });

        clickTextView.setListener(new VerticalHorizontalTextView.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                Toast.makeText(MainActivity.this,"item"+position,Toast.LENGTH_SHORT).show();
            }
        });

        textView.setListener(new VerticalHorizontalTextView.OnItemClickListener() {
            @Override
            public void itemClick(int position) {
                Toast.makeText(MainActivity.this,"item"+position,Toast.LENGTH_SHORT).show();
            }
        });

        horizontalTextView.setListener(new VerticalHorizontalTextView.OnItemClickListener() {
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
            horizontalTextView.startScroll();
            clickTextView.startScroll();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textView != null) {
            textView.stopScroll();
            horizontalTextView.stopScroll();
            clickTextView.stopScroll();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textView != null) {
            textView.onDestroy();
            horizontalTextView.onDestroy();
            clickTextView.onDestroy();
        }
    }

}
