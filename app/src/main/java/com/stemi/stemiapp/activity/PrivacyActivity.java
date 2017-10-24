package com.stemi.stemiapp.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
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

public class PrivacyActivity extends AppCompatActivity {
    private List<Privacy> privacyLists = new ArrayList<>();
    SimpleAdapter mAdapter;
    TextView textView7;
    Toolbar toolbar;
    Spannable sb;
    SpannableStringBuilder total;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        toolbar = (Toolbar) findViewById(R.id.terms_toolbar);
        toolbar.setTitle("Privacy Policy");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textView7 = (TextView) findViewById(R.id.textView7);

        CommonUtils.setRobotoLightFonts(this,textView7);

        recyclerView = (RecyclerView) findViewById(R.id.privacy_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new SimpleAdapter(privacyLists, recyclerView, this);
        recyclerView.setAdapter(mAdapter);

        preparePrivacyList();
    }
    private void preparePrivacyList() {
        Privacy privacy = new Privacy(getResources().getString(R.string.collect_info),
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.collect_info_answer), "Contact Information", "Payment and billing information:", "Demographic information:", "Medical information:", "Information about your lifestyle:","Other information:"));
        privacyLists.add(privacy);

        privacy = new Privacy(getResources().getString(R.string.how_to_collect_info),
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.how_to_collect_info_ans), "Direct collection:"));
        privacyLists.add(privacy);

        privacy = new Privacy(getResources().getString(R.string.personal_info),
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.personal_info_ans), "To contact you:", "To improve our services:", "To ensure security:", "To abide by laws:"));
        privacyLists.add(privacy);

        privacy = new Privacy(getResources().getString(R.string.third_party_info),
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.third_party_ans)));
        privacyLists.add(privacy);

        privacy = new Privacy(getResources().getString(R.string.third_party_sites),
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.third_party_sites_ans)));
        privacyLists.add(privacy);

        privacy = new Privacy(getResources().getString(R.string.grievance_officer),
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.grievance_ans),"Grievance Officer Name:","Address","Phone"));
        privacyLists.add(privacy);

        privacy = new Privacy(getResources().getString(R.string.jurisdiction),
                CommonUtils.makeSectionOfTextBold(getResources().getString(R.string.jurisdiction_ans)));
        privacyLists.add(privacy);
    }

    private class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;
        private List<Privacy> privacyList;
        private int selectedItem = UNSELECTED;
        RecyclerView recyclerView;
        Context context;


        public SimpleAdapter(List<Privacy> privacyList, RecyclerView recyclerView, Context context) {
            this.privacyList = privacyList;
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
            return privacyList.size();
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
                Privacy privacy = privacyList.get(position);
                expandButton.setText(privacy.getQuestion());
                privacy_answer.setText(privacy.getAnswer());
                //   privacy_answer.setText(privacy.getAnswers());
                //  expandButton.setText(position + ". Tap to expand");
                expandButton.setSelected(false);
                expandableLayout.collapse(false);

                CommonUtils.setRobotoLightFonts(context,expandButton);
                CommonUtils.setRobotoLightFonts(context,privacy_answer);

            }

            @Override
            public void onClick(View view) {
                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                if (holder != null) {
                    holder.expandButton.setSelected(false);
                    holder.expandableLayout.collapse();
                    CommonUtils.setRobotoLightFonts(context,holder.expandButton);
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
