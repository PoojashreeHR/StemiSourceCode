package com.stemi.stemiapp.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.utils.CommonUtils;

/**
 * Created by praburaam on 17/07/17.
 */

public class AnswerTemplateView extends LinearLayout implements View.OnClickListener {
    private TextView tv_yes, tv_no, tv_dont_know;
    private String response;
    private boolean hideDontKnow;
    private int color;
    private int Backgroundcolor;
    private int clickedColor;
    private Drawable setBackground;
    private int layoutVal;

    public AnswerTemplateView() {
        super(null);

    }

    public void setResponseChangedListener(ResponseChangedListener responseChangedListener) {
        this.responseChangedListener = responseChangedListener;
    }

    public interface ResponseChangedListener{
        void onResponse(String response);
    }
    private ResponseChangedListener responseChangedListener;

    public AnswerTemplateView(Context context){
        super(context);
    }
    public AnswerTemplateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AnswerTemplateView,
                0, 0);
        try{
            hideDontKnow = a.getBoolean(R.styleable.AnswerTemplateView_hideDontKnow, false);
            color = a.getColor(R.styleable.AnswerTemplateView_colorView, 0 );
            setBackground = a.getDrawable(R.styleable.AnswerTemplateView_textBackground);
            Backgroundcolor = a.getColor(R.styleable.AnswerTemplateView_textColor,0);
            clickedColor = a.getColor(R.styleable.AnswerTemplateView_clickedColor,0);
            layoutVal = a.getInteger(R.styleable.AnswerTemplateView_layoutVal,0);
        }
        finally {
            a.recycle();
        }

        View v = LayoutInflater.from(context).inflate(R.layout.answer_layout, null);

        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.layout_smoke);

        tv_yes = (TextView) v.findViewById(R.id.tv_yes);
        CommonUtils.setRobotoRegularFonts(getContext(),tv_yes);
        tv_yes.setBackgroundDrawable(setBackground);
        tv_yes.setTextColor(clickedColor);

        tv_no = (TextView) v.findViewById(R.id.tv_no);
        CommonUtils.setRobotoRegularFonts(getContext(),tv_no);
        tv_no.setBackgroundDrawable(setBackground);
        tv_no.setTextColor(clickedColor);

        tv_dont_know = (TextView) v.findViewById(R.id.tv_dont_know);
        CommonUtils.setRobotoRegularFonts(getContext(),tv_dont_know);
        tv_dont_know.setBackgroundDrawable(setBackground);
        tv_dont_know.setTextColor(clickedColor);

        tv_yes.setOnClickListener(this);
        tv_no.setOnClickListener(this);
        tv_dont_know.setOnClickListener(this);

        if(hideDontKnow){
            if(layoutVal == 1){
                tv_dont_know.setVisibility(GONE);
                linearLayout.setGravity(Gravity.CENTER);
            }else {
                tv_dont_know.setVisibility(GONE);
            }
//            linearLayout.setGravity(Gravity.CENTER);
        }
        addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
    }


    public String getResponse() {return response;}

    public void setResponse(String response) {
        this.response = response;
        switch (response) {
            case "YES":
                setYes();
                break;
            case "NO":
                setNo();
                break;
            case "DON'T KNOW":
                setDontKnow();
                break;
            case "NULL":
                setNull();

        }
    }
    public void setNull(){
        response = null;

        tv_no.setBackground(setBackground);
        tv_no.setTextColor(Backgroundcolor);

        tv_dont_know.setBackground(setBackground);
        tv_dont_know.setTextColor(Backgroundcolor);

        tv_yes.setBackground(setBackground);
        tv_yes.setTextColor(Backgroundcolor);
    }
    public void setYes(){
        response = "YES";
        tv_no.setBackground(setBackground);
        tv_no.setTextColor(Backgroundcolor);

        tv_dont_know.setBackground(setBackground);
        tv_dont_know.setTextColor(Backgroundcolor);

        tv_yes.setBackgroundColor(Backgroundcolor);
        tv_yes.setTextColor(color);

        if(responseChangedListener != null){
            responseChangedListener.onResponse(response);
        }
    }

    public void setNo()
    {
        response = "NO";
        tv_yes.setBackground(setBackground);
        tv_yes.setTextColor(Backgroundcolor);

        tv_dont_know.setBackground(setBackground);
        tv_dont_know.setTextColor(Backgroundcolor);

        tv_no.setBackgroundColor(Backgroundcolor);
        tv_no.setTextColor(color);

        if(responseChangedListener != null){
            responseChangedListener.onResponse(response);
        }
    }

    public void setDontKnow(){
        response = "DON'T KNOW";
        tv_no.setBackground(setBackground);
        tv_no.setTextColor(Backgroundcolor);

        tv_yes.setBackground(setBackground);
        tv_yes.setTextColor(Backgroundcolor);

        tv_dont_know.setBackgroundColor(Backgroundcolor);
        tv_dont_know.setTextColor(color);

        if(responseChangedListener != null){
            responseChangedListener.onResponse(response);
        }
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_yes:
                setYes();
                break;
            case R.id.tv_no:
                setNo();
                break;
            case R.id.tv_dont_know:
                setDontKnow();
                break;
        }

    }
}
