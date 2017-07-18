package com.stemi.stemiapp.customviews;

import android.content.Context;
import android.content.res.TypedArray;
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

    public AnswerTemplateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AnswerTemplateView,
                0, 0);
        try{
            hideDontKnow = a.getBoolean(R.styleable.AnswerTemplateView_hideDontKnow, false);
        }
        finally {
            a.recycle();
        }


        View v = LayoutInflater.from(context).inflate(R.layout.answer_layout, null);
        tv_yes = (TextView) v.findViewById(R.id.tv_yes);
        tv_no = (TextView) v.findViewById(R.id.tv_no);
        tv_dont_know = (TextView) v.findViewById(R.id.tv_dont_know);

        tv_yes.setOnClickListener(this);
        tv_no.setOnClickListener(this);
        tv_dont_know.setOnClickListener(this);

        if(hideDontKnow){
            tv_dont_know.setVisibility(GONE);
        }

        addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

    }


    public String getResponse() {
        return response;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.tv_yes:
                response = "YES";
                tv_no.setBackground(getResources().getDrawable(R.drawable.text_border_with_color));
                //tv_no.setBackgroundColor(getResources().getColor(R.color.appBackground));
                tv_no.setTextColor(getResources().getColor(R.color.white));

                tv_dont_know.setBackground(getResources().getDrawable(R.drawable.text_border_with_color));
                tv_dont_know.setTextColor(getResources().getColor(R.color.white));

                tv_yes.setBackgroundColor(getResources().getColor(R.color.white));
                tv_yes.setTextColor(getResources().getColor(R.color.appBackground));
                break;
            case R.id.tv_no:
                response = "NO";
                tv_yes.setBackground(getResources().getDrawable(R.drawable.text_border_with_color));
                tv_yes.setTextColor(getResources().getColor(R.color.white));

                tv_dont_know.setBackground(getResources().getDrawable(R.drawable.text_border_with_color));
                tv_dont_know.setTextColor(getResources().getColor(R.color.white));

                tv_no.setBackgroundColor(getResources().getColor(R.color.white));
                tv_no.setTextColor(getResources().getColor(R.color.appBackground));
                break;
            case R.id.tv_dont_know:
                response = "DON'T KNOW";
                tv_no.setBackground(getResources().getDrawable(R.drawable.text_border_with_color));
                tv_no.setTextColor(getResources().getColor(R.color.white));

                tv_yes.setBackground(getResources().getDrawable(R.drawable.text_border_with_color));
                tv_yes.setTextColor(getResources().getColor(R.color.white));

                tv_dont_know.setBackgroundColor(getResources().getColor(R.color.white));
                tv_dont_know.setTextColor(getResources().getColor(R.color.appBackground));
                break;
        }

    }
}
