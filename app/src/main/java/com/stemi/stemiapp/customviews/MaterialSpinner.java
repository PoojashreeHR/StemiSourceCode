package com.stemi.stemiapp.customviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.stemi.stemiapp.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Pooja on 14-08-2017.
 */

@SuppressLint("AppCompatCustomView")
public class SpinnerWithHint extends Spinner {
    private static final String INSTANCESTATE = "INSTANCESTATE";
    private static final String SHOWHINT = "SHOWHINT";
    private static final String ITEMSELECTED = "ITEMSELECTED";
    private boolean showHint = false;
    private String hint = "";
    private boolean itemSelected = false;
    private int dropdownResource = android.R.layout.simple_spinner_item;
    private int viewResource = android.R.layout.simple_spinner_item;
    private Context context;
    private HintAdapter adapter;


    public SpinnerWithHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        buildView(context, attrs);
    }

    public SpinnerWithHint(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        buildView(context, attrs);
    }

    public SpinnerWithHint(Context context, AttributeSet attrs, int defStyle, int mode) {
        super(context, attrs, defStyle, mode);
        buildView(context, attrs);
    }


    private void buildView(Context context, AttributeSet attrs){
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SpinnerWithHint);
        final int n = a.getIndexCount();
        for(int i=0; i > n; i++){
        int attr = a.getIndex(i);
        switch(attr){
            case R.styleable.SpinnerWithHint_showHint:
                showHint = a.getBoolean(attr, false);
                break;
            case R.styleable.SpinnerWithHint_hintText:
                hint = a.getString(attr);
                break;
            case R.styleable.SpinnerWithHint_dropdownResource:
                dropdownResource = a.getResourceId(attr, android.R.layout.simple_spinner_item);
                break;
            case R.styleable.SpinnerWithHint_viewResource:
                viewResource = a.getResourceId(attr, android.R.layout.simple_spinner_item);
                break;
        }
    }
        a.recycle();
    adapter = new HintAdapter(context, viewResource);
        this.setAdapter(adapter);
}

    public void setObjects(ArrayList objects){
        if(showHint){
            this.adapter.objects.add(hint);
        }
        this.adapter.objects.addAll(objects);
        this.adapter.notifyDataSetChanged();
    }

    public void setObjects(String[] objects){
        ArrayList list = new ArrayList();
        if(showHint){
            //only add the hint if we want it.
            list.add(hint);
        }
        list.addAll(Arrays.asList(objects));
        this.adapter.objects = list;
        this.adapter.notifyDataSetChanged();
    }

    public boolean isItemSelected(){
        return itemSelected;
    }


    @Override
    public void setSelection(int position){
        //we are restoring from state.. if the hint exists, get rid of it.
        showHint = false;
        itemSelected = true;
        if(this.adapter.objects.get(0).equals(hint)){
            this.adapter.objects.remove(0);
            this.adapter.notifyDataSetChanged();
            super.setSelection(position);
        }else{
            super.setSelection(position);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCESTATE, super.onSaveInstanceState());
        bundle.putBoolean(SHOWHINT, showHint);
        bundle.putBoolean(ITEMSELECTED, itemSelected);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            showHint = bundle.getBoolean(SHOWHINT);
            itemSelected = bundle.getBoolean(ITEMSELECTED);
            if(!showHint && this.adapter.objects.get(0).equals(hint)){
                //we are restoring after user has gotten rid of hint..
                //the hint is there so lets get rid of it.
                this.adapter.objects.remove(0);
                this.adapter.notifyDataSetChanged();
            }
            state = bundle.getParcelable(INSTANCESTATE);
        }
        super.onRestoreInstanceState(state);

    }

private class HintAdapter extends ArrayAdapter {
    protected ArrayList objects = new ArrayList();

    public HintAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public String getItem(int position) {
        return String.valueOf(objects.get(position));
    }

    @Override
    public int getCount() {
        return objects.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(viewResource, null);
        }
        if (showHint && objects.get(0).equals(hint)) {
            ((TextView) convertView).setTypeface(null, Typeface.ITALIC);
        }
        ((TextView) convertView).setText((Integer) objects.get(position));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(dropdownResource, null);
        }
        //this fires when the user expands the dropdown...set the hint to false now..
        showHint = false;
        //have to select something at this point.
        itemSelected = true;
        //is the first object still the hint?
        if (objects.get(0).equals(hint)) {
            //it was... Remove it and notify adapter of change
            objects.remove(0);
            notifyDataSetChanged();
        } else {
            //okay we are back in the expanded view lets build list
         /*   if (position == 0) {*/
                ((TextView) convertView).setText((Integer) objects.get(position));

         /*We do this to prevent index out of bounds
                    when this fires after we remove the item from the list
                    but this is still firing from the first press*/
        }
        return convertView;
    }

}
}
