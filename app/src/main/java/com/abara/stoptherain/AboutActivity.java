package com.abara.stoptherain;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

import com.abara.stoptherain.adapters.AboutAdapter;
import com.abara.stoptherain.utils.OnRowClickListener;

/**
 * Created by Abb on 8/27/2015.
 */
public class AboutActivity extends AppCompatActivity {

    RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar appbar = (Toolbar) findViewById(R.id.about_bar);
        setSupportActionBar(appbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        mList = (RecyclerView) findViewById(R.id.about_list);
        mList.setHasFixedSize(true);
        mList.setItemAnimator(new DefaultItemAnimator());
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(new AboutAdapter(this, new OnRowClickListener() {
            @Override
            public void onRowClick(int position) {
                switch (position){
                    case 0:
                        Intent gplus = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+AbarajithanLv"));
                        startActivity(gplus);
                        break;
                    case 1:
                        Intent rating = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName()));
                        startActivity(rating);
                        break;
                    case 2:
                        Intent dev = new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/dev?id=5738152916341248637"));
                        startActivity(dev);
                        break;
                }
            }
        }));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
