package com.stemi.stemiapp.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stemi.stemiapp.R;

/**
 * Created by praburaam on 17/07/17.
 */

public class AnswerTemplateView extends LinearLayout implements View.OnClickListener {
    private TextView tv_yes, tv_no, tv_dont_know;
    private String response;
    private boolean hideDontKnow;
    private int color;
    private Drawable setBackground;

    public void setResponseChangedListener(ResponseChangedListener responseChangedListener) {
        this.responseChangedListener = responseChangedListener;
    }


    public interface ResponseChangedListener{
        void onResponse(String response);
    }
    private ResponseChangedListener responseChangedListener;

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
        }
        finally {
            a.recycle();
        }


        View v = LayoutInflater.from(context).inflate(R.layout.answer_layout, null);
        tv_yes = (TextView) v.findViewById(R.id.tv_yes);
        tv_yes.setBackgroundDrawable(setBackground);
        tv_no = (TextView) v.findViewById(R.id.tv_no);
        tv_no.setBackgroundDrawable(setBackground);
        tv_dont_know = (TextView) v.findViewById(R.id.tv_dont_know);
        tv_dont_know.setBackgroundDrawable(setBackground);

        tv_yes.setOnClickListener(this);
        tv_no.setOnClickListener(this);
        tv_dont_know.setOnClickListener(this);

        if(hideDontKnow){
            tv_dont_know.setVisibility(GONE);
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
        }
    }
    public void setYes(){
        response = "YES";
        tv_no.setBackground(setBackground);
        tv_no.setTextColor(getResources().getColor(R.color.white));

        tv_dont_know.setBackground(setBackground);
        tv_dont_know.setTextColor(getResources().getColor(R.color.white));

        tv_yes.setBackgroundColor(getResources().getColor(R.color.white));
        tv_yes.setTextColor(color);

        if(responseChangedListener != null){
            responseChangedListener.onResponse(response);
        }
    }

    public void setNo()
    {
        response = "NO";
        tv_yes.setBackground(setBackground);
        tv_yes.setTextColor(getResources().getColor(R.color.white));

        tv_dont_know.setBackground(setBackground);
        tv_dont_know.setTextColor(getResources().getColor(R.color.white));

        tv_no.setBackgroundColor(getResources().getColor(R.color.white));
        tv_no.setTextColor(color);

        if(responseChangedListener != null){
            responseChangedListener.onResponse(response);
        }
    }

    public void setDontKnow(){
        response = "DON'T KNOW";
        tv_no.setBackground(setBackground);
        tv_no.setTextColor(getResources().getColor(R.color.white));

        tv_yes.setBackground(setBackground);
        tv_yes.setTextColor(getResources().getColor(R.color.white));

        tv_dont_know.setBackgroundColor(getResources().getColor(R.color.white));
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
