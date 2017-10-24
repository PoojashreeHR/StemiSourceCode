package com.stemi.stemiapp.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stemi.stemiapp.R;
import com.stemi.stemiapp.customviews.ExpandableLayout;
import com.stemi.stemiapp.model.Privacy;
import com.stemi.stemiapp.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class TermsAndConditionActivity extends AppCompatActivity {
    private List<Privacy> termsList = new ArrayList<>();
    Toolbar toolbar;
    TermsAdapter mAdapter;
    TextView tv, textView7;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        toolbar = (Toolbar) findViewById(R.id.terms_toolbar);
        toolbar.setTitle("Terms and Conditions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*profile_img = (ImageView) findViewById(R.id.profileImg);
        profile_img.setVisibility(View.GONE);*/
        textView7 = (TextView) findViewById(R.id.textView7);

     /*   tv = (TextView) findViewById(R.id.tv_toolbarText);
        tv.setText("Terms and Condition");
*/
        CommonUtils.setRobotoLightFonts(this, textView7);

        recyclerView = (RecyclerView) findViewById(R.id.terms_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new TermsAdapter(termsList, recyclerView, this);
        recyclerView.setAdapter(mAdapter);
        prepareTermsList();
    }

    private void prepareTermsList() {
        Privacy terms = new Privacy("Acceptance of Terms & Conditions",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.acceptance)));
        termsList.add(terms);

        terms = new Privacy("Copyrighted Materials for Limited Use",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.copyright)));
        termsList.add(terms);

        terms = new Privacy("User Information",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.user_info)));
        termsList.add(terms);

        terms = new Privacy("Consent",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.consent)));
        termsList.add(terms);

        terms = new Privacy("Use of App",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.use_of_app)));
        termsList.add(terms);

        terms = new Privacy("Indemnification",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.indemnification)));
        termsList.add(terms);

        terms = new Privacy("Disclaimer of Warranty",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.disclaime_of_warrenty)));
        termsList.add(terms);

        terms = new Privacy("Limited Warranty",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.limited_warrenty)));
        termsList.add(terms);

        terms = new Privacy("Limitation of Liability",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.liabalities)));
        termsList.add(terms);

        terms = new Privacy("App Feedback",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.feedback)));
        termsList.add(terms);

        terms = new Privacy("Governing Law",
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.governing_law)));
        termsList.add(terms);
    }

    private static class TermsAdapter extends RecyclerView.Adapter<TermsAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;
        private List<Privacy> termsLists;
        private int selectedItem = UNSELECTED;
        RecyclerView recyclerView;
        Context context;

        public TermsAdapter(List<Privacy> terms, RecyclerView recyclerView, Context context) {
            this.termsLists = terms;
            this.recyclerView = recyclerView;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.privacy_single_row, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return termsLists.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ExpandableLayout expandableLayout;
            private TextView expandButton, privacy_answer;
            private int position;
            //    RecyclerView recyclerView;

            public ViewHolder(View itemView) {
                super(itemView);

                expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
                expandButton = (TextView) itemView.findViewById(R.id.expand_button);
                privacy_answer = (TextView) itemView.findViewById(R.id.privacy_answer);

                expandButton.setOnClickListener(this);

            }

            public void bind(int position) {
                this.position = position;
                Privacy privacy = termsLists.get(position);
                expandButton.setText(privacy.getQuestion());
                privacy_answer.setText(privacy.getAnswer());
                //   privacy_answer.setText(privacy.getAnswers());
                //  expandButton.setText(position + ". Tap to expand");
                expandButton.setSelected(false);
                expandableLayout.collapse(false);

                CommonUtils.setRobotoLightFonts(context, expandButton);
                CommonUtils.setRobotoLightFonts(context, privacy_answer);

            }

            @Override
            public void onClick(View view) {
                TermsAdapter.ViewHolder holder = (TermsAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                if (holder != null) {
                    holder.expandButton.setSelected(false);
                    holder.expandableLayout.collapse();
                    CommonUtils.setRobotoLightFonts(context, holder.expandButton);
                }

                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {
                    expandButton.setSelected(true);
                    expandButton.setTypeface(Typeface.DEFAULT_BOLD);
                    expandableLayout.expand();
                    selectedItem = position;
                }
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
