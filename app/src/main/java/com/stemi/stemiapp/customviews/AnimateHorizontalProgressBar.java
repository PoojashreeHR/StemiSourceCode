package com.stemi.stemiapp.customviews;


import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stemi.stemiapp.R;

/**
 * Created by Pooja on 18-09-2017.
 */

public class AnimateHorizontalProgressBar extends SeekBar{

    int maxCount, textColor;
    Context mContext;
    LinearLayout mSeekLin;
    SeekBar mSeekBar;

    public AnimateHorizontalProgressBar(Context context, int maxCount, int textColor) {
        super(context);
        this.mContext = context;
        this.maxCount = maxCount;
        this.textColor = textColor;
    }

    public void addSeekBar(LinearLayout parent) {

        if (parent instanceof LinearLayout) {

            parent.setOrientation(LinearLayout.VERTICAL);
            mSeekBar = new SeekBar(mContext);
            mSeekBar.setMax(maxCount - 1);

            // Add LinearLayout for labels below SeekBar
            mSeekLin = new LinearLayout(mContext);
            mSeekLin.setOrientation(LinearLayout.HORIZONTAL);
            mSeekLin.setPadding(10, 0, 10, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(35, 10, 35, 0);
            mSeekLin.setLayoutParams(params);

            addLabelsBelowSeekBar();
            parent.addView(mSeekBar);
            parent.addView(mSeekLin);

        } else {

            Log.e("CustomSeekBar", " Parent is not a LinearLayout");

        }

    }

    private void addLabelsBelowSeekBar() {
        for (int count = 0; count < maxCount; count++) {
            TextView textView = new TextView(mContext);
            textView.setText(String.valueOf(count + 1));
            textView.setTextColor(textColor);
            textView.setGravity(Gravity.LEFT);
            mSeekLin.addView(textView);
            textView.setLayoutParams((count == maxCount - 1) ? getLayoutParams(0.0f) : getLayoutParams(1.0f));
        }
    }

    LinearLayout.LayoutParams getLayoutParams(float weight) {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, weight);
    }
}
