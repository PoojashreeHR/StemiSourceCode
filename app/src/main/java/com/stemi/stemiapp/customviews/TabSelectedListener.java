package com.stemi.stemiapp.customviews;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Pooja on 12-10-2017.
 */

public class TabSelectedListener implements ActionBar.TabListener {
    public Fragment fragment;
    private final Runnable onSelect;
    Context context;

    public TabSelectedListener(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.onSelect = null;
    }

    public TabSelectedListener(Fragment fragment, Runnable onSelect) {
        this.fragment = fragment;
        this.onSelect = onSelect;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (onSelect != null) {
            fragment.getActivity().runOnUiThread(onSelect);

            Toast.makeText(context, "Tab is selected from " + fragment.getId(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
