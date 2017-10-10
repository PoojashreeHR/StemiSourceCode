package com.stemi.stemiapp.customviews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.stemi.stemiapp.R;

import java.util.List;

/**
 * Created by Pooja on 04-10-2017.
 */

@SuppressLint("AppCompatCustomView")
public class CustomSeekbar extends LinearLayout {
    private RelativeLayout RelativeLayout = null;
    private SeekBar Seekbar = null;
    private RelativeLayout Divider = null;
    private View verticalLine = null;

    private int WidthMeasureSpec = 0;
    private int HeightMeasureSpec = 0;

    public CustomSeekbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        getActivity().getLayoutInflater()
                .inflate(R.layout.seekbar_with_intervals, this);
    }

    private Activity getActivity() {
        return (Activity) getContext();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        verticalLine = new View(getActivity());
        verticalLine.setLayoutParams(new LayoutParams(2, LayoutParams.MATCH_PARENT));
        verticalLine.setBackgroundColor(Color.BLACK);

        if (changed) {
            alignIntervals();

            // We've changed the intervals layout, we need to refresh.
            RelativeLayout.measure(WidthMeasureSpec, HeightMeasureSpec);
            RelativeLayout.layout(RelativeLayout.getLeft(), RelativeLayout.getTop(), RelativeLayout.getRight(), RelativeLayout.getBottom());
        }
    }

    private void alignIntervals() {
        int widthOfSeekbarThumb = getSeekbarThumbWidth();
        int thumbOffset = widthOfSeekbarThumb / 2;

        int widthOfSeekbar = getSeekbar().getWidth();
        int firstIntervalWidth = getRelativeLayout().getChildAt(0).getWidth();
        int remainingPaddableWidth = widthOfSeekbar - firstIntervalWidth - widthOfSeekbarThumb;

        int numberOfIntervals = getSeekbar().getMax();
        int maximumWidthOfEachInterval = remainingPaddableWidth / numberOfIntervals;

        alignFirstInterval(thumbOffset);
        alignIntervalsInBetween(maximumWidthOfEachInterval);
        alignLastInterval(thumbOffset, maximumWidthOfEachInterval);
    }

    private int getSeekbarThumbWidth() {
        return 20;
    }

    private void alignFirstInterval(int offset) {
        TextView firstInterval = (TextView) getRelativeLayout().getChildAt(0);
        firstInterval.setPadding(offset - 10, 0, 0, 0);

        TextView firstLine = (TextView) getDivider().getChildAt(0);
        firstLine.setPadding(offset + 10, 0, 0, 0);
    }

    private void alignIntervalsInBetween(int maximumWidthOfEachInterval) {
        int widthOfPreviousIntervalsText = 0;
        int widthOfPreviousLine = 0;

        // Don't align the first or last interval.
        for (int index = 1; index < (getRelativeLayout().getChildCount() - 1); index++) {
            TextView textViewInterval = (TextView) getRelativeLayout().getChildAt(index);
            int widthOfText = textViewInterval.getWidth();

            // This works out how much left padding is needed to center the current interval.
            //int leftPadding = Math.round(maximumWidthOfEachInterval - (widthOfText / 2) - (widthOfPreviousIntervalsText / 2) - (widthOfText / 2));
            int leftPadding = Math.round(maximumWidthOfEachInterval - (widthOfText / 2) - (widthOfPreviousIntervalsText / 2) - (widthOfText / index ) + index + 5 * 5);

            textViewInterval.setPadding(leftPadding, 0, 0, 0);

            widthOfPreviousIntervalsText = widthOfText;

            TextView textViewLine = (TextView) getDivider().getChildAt(index);
            int widthOfLine = textViewLine.getWidth();

            // This works out how much left padding is needed to center the current interval.
            leftPadding = (maximumWidthOfEachInterval + (index + (maximumWidthOfEachInterval / 10)) - (index * 4)); //Math.round(maximumWidthOfEachInterval + (widthOfLine ) + (widthOfPreviousLine ));
            //leftPadding = Math.round((maximumWidthOfEachInterval - (widthOfPreviousLine / index) - (widthOfPreviousLine / index) - (widthOfPreviousLine / index)) + 10);
            textViewLine.setPadding(leftPadding , 0, 0, 0);

            widthOfPreviousLine = widthOfLine;
        }
    }

    private void alignLastInterval(int offset, int maximumWidthOfEachInterval) {
        int lastIndex = getRelativeLayout().getChildCount() - 1;

        TextView lastInterval = (TextView) getRelativeLayout().getChildAt(lastIndex);
        int widthOfText = lastInterval.getWidth();

        int leftPadding = Math.round(maximumWidthOfEachInterval - widthOfText - offset);
        lastInterval.setPadding(leftPadding + 20, 0, 0, 0);

        TextView lastLine = (TextView) getDivider().getChildAt(lastIndex);
        leftPadding = Math.round(maximumWidthOfEachInterval - (widthOfText / 5) - (widthOfText / 5) - (widthOfText / 5 ) + 3 * 10);
        lastLine.setPadding(leftPadding , 0, 0, 0);

    }

    protected synchronized void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        WidthMeasureSpec = widthMeasureSpec;
        HeightMeasureSpec = heightMeasureSpec;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getProgress() {
        return getSeekbar().getProgress();
    }

    public void setProgress(int progress) {
        getSeekbar().setProgress(progress);
    }

    public void setIntervals(List<String> intervals) {
        displayIntervals(intervals);
        getSeekbar().setMax(intervals.size() - 1);
    }

    private void displayIntervals(List<String> intervals) {
        int idOfPreviousInterval = 0;
        int idOfPreviousLine = 0;

        if (getRelativeLayout().getChildCount() == 0) {
            for (String interval : intervals) {
                TextView textViewInterval = createInterval(interval);
                alignTextViewToRightOfPreviousInterval(textViewInterval, idOfPreviousInterval);

                TextView textViewVerticaLine = createVerticalLine();
                alignTextViewToRightOfPreviousInterval(textViewVerticaLine, idOfPreviousLine);
                idOfPreviousLine = textViewVerticaLine.getId();

                idOfPreviousInterval = textViewInterval.getId();

                getRelativeLayout().addView(textViewInterval);
                getDivider().addView(textViewVerticaLine);

            }
        }
    }

    private TextView createInterval(String interval) {
        View textBoxView = (View) LayoutInflater.from(getContext())
                .inflate(R.layout.seekbar_with_intervals_labels, null);

        TextView textView = (TextView) textBoxView
                .findViewById(R.id.textViewInterval);

        textView.setId(View.generateViewId());
        textView.setText(interval);

        return textView;
    }

    private TextView createVerticalLine() {
        View textBoxView = (View) LayoutInflater.from(getContext())
                .inflate(R.layout.seekbar_vertical_lines, null);

        TextView textView = (TextView) textBoxView
                .findViewById(R.id.textViewVerticalLine);

        textView.setId(View.generateViewId());

        return textView;
    }


    private void alignTextViewToRightOfPreviousInterval(TextView textView, int idOfPreviousInterval) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        if (idOfPreviousInterval > 0) {
            params.addRule(RelativeLayout.RIGHT_OF, idOfPreviousInterval);
        }

        textView.setLayoutParams(params);
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener onSeekBarChangeListener) {
        getSeekbar().setOnSeekBarChangeListener(onSeekBarChangeListener);
    }

    private RelativeLayout getRelativeLayout() {
        if (RelativeLayout == null) {
            RelativeLayout = (RelativeLayout) findViewById(R.id.intervals);
        }

        return RelativeLayout;
    }

    private SeekBar getSeekbar() {
        if (Seekbar == null) {
            Seekbar = (SeekBar) findViewById(R.id.seekbar);
        }

        return Seekbar;
    }

    private RelativeLayout getDivider() {

        if (Divider == null) {
            Divider = (RelativeLayout) findViewById(R.id.fl_divider);
        }

        return Divider;
    }
}
