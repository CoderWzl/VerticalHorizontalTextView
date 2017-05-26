package com.example.wzl.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.RestrictTo;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Des:可以实现文本的横向和竖向滚动
 * Created by changfeng on 2017/5/26 0:04.
 * Email: 17010058651@163.com
 */

public class VerticalHorizontalTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {

    /** @hide */
    @RestrictTo(LIBRARY_GROUP)      //?????
    @IntDef(flag=true, value={
            SCROLL_VERTICAL,
            SCROLL_HORIZONTAL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScrollOrientation {}

    public static final int SCROLL_VERTICAL = 0;
    public static final int SCROLL_HORIZONTAL = 1;

    public static final int FLAG_START_SCROLL = 0;
    public static final int FLAG_STOP_SCROLL = 1;

    /**
     * 滚动的方向
     */
    private int orientation = SCROLL_VERTICAL;
    private int mTextColor = Color.BLACK;
    private float mTextSize = 16f;
    private int currentId = -1;
    private boolean isScrolling = false;
    /**
     *动画时长
     */
    private long mAnimTime;
    /**
     * 动画起止位置
     */
    private int mAnimTranslate;
    private Context mContext;
    /**
     * 控制文案轮播的handler
     */
    private Handler handler;
    /**
     * 数据源
     */
    private List<String> textList;
    /**
     * 最大行数
     */
    private int mMaxLines = 1;

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOrientation(@ScrollOrientation int orientation) {
        this.orientation = orientation;
        //重新
        setAnimTime(mAnimTime,mAnimTranslate);
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public void setmMaxLines(int mMaxLines) {
        this.mMaxLines = mMaxLines;
    }

    public VerticalHorizontalTextView(Context context) {
        this(context,null);
    }

    public VerticalHorizontalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        textList = new ArrayList<>();
        //初始化属性值
        TypedArray array = mContext.obtainStyledAttributes(attrs,R.styleable.VerticalHorizontalTextView);
        mTextColor = array.getColor(R.styleable.VerticalHorizontalTextView_textColor,Color.BLACK);
        mTextSize = array.getDimension(R.styleable.VerticalHorizontalTextView_textSize,16);
        mMaxLines = array.getInteger(R.styleable.VerticalHorizontalTextView_maxLines,1);
        orientation = array.getInt(R.styleable.VerticalHorizontalTextView_scrollOrientation, 0);
        array.recycle();
    }

    /**
     * 设置动画时长＆起止位置
     * @param time
     * @param translate
     */
    public void setAnimTime(long time,int translate){
        mAnimTranslate = translate;
        mAnimTime = time;
        try {
            removeAllViewsInLayout();
            setFactory(this);
            Animation inAnim;
            Animation outAnim;
            if (orientation == SCROLL_VERTICAL){
                inAnim = new TranslateAnimation(0,0,translate,0);
                outAnim = new TranslateAnimation(0,0,0,(-translate));
            }else {
                inAnim = new TranslateAnimation(translate,0,0,0);
                outAnim = new TranslateAnimation(0,-translate,0,0);
            }
            inAnim.setDuration(time);
            outAnim.setDuration(time);
            //设置插值器
            inAnim.setInterpolator(new LinearInterpolator());
            outAnim.setInterpolator(new LinearInterpolator());
            setInAnimation(inAnim);
            setOutAnimation(outAnim);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设置文案停留时间
     * @param time
     */
    public void setTextStillTime(final long time){
        if (handler == null) {
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what){
                        case FLAG_START_SCROLL:
                            if (textList.size() > 0){
                                currentId++;
                                setText(textList.get(currentId % textList.size()));
                            }
                            handler.sendEmptyMessageDelayed(FLAG_START_SCROLL,time);
                            break;
                        case FLAG_STOP_SCROLL:
                            handler.removeMessages(FLAG_START_SCROLL);
                            break;
                    }
                }
            };
        }
    }

    /**
     * 设置数据源
     * @param titles
     */
    public void setTextList(List<String> titles){
        textList.clear();
        textList.addAll(titles);
        currentId = -1;
    }

    /**
     * 开始滚动
     */
    public void startScroll(){
        if (!isScrolling) {
            isScrolling = true;
            handler.sendEmptyMessage(FLAG_START_SCROLL);
        }
    }

    /**
     * 停止滚动
     */
    public void stopScroll(){
        if (isScrolling) {
            isScrolling = false;
            handler.sendEmptyMessage(FLAG_STOP_SCROLL);
        }
    }

    @Override
    public View makeView() {
        final TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setTextColor(mTextColor);
        textView.setTextSize(mTextSize);
        textView.setMaxLines(mMaxLines);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null && textList.size()>0 && currentId != -1){
                    listener.itemClick(currentId % textList.size());
                }
            }
        });
        return textView;
    }

    /**
     * 防止内存泄漏
     */
    public void onDestroy(){
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    /**
     * 文案被点击之后的回调
     */
    public interface OnItemClickListener{
        void itemClick(int position);
    }
}
