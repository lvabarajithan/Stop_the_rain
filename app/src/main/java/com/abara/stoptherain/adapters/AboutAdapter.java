package com.abara.stoptherain.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abara.stoptherain.R;
import com.abara.stoptherain.utils.OnRowClickListener;

/**
 * Created by Abb on 8/28/2015.
 */
public class AboutAdapter extends RecyclerView.Adapter<AboutHolder> {

    private Context mContext;
    private String[] title = {"View Google+ profile","Rate my app","More apps"};
    private int[] images = {R.drawable.ic_google_plus,R.drawable.ic_action_grade,R.drawable.ic_more_apps};
    private OnRowClickListener onRowClickListener;

    public AboutAdapter(Context context,OnRowClickListener listener){
        mContext = context;
        onRowClickListener = listener;
    }

    @Override
    public AboutHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.about_single_row,parent,false);

        return new AboutHolder(view,onRowClickListener);
    }

    @Override
    public void onBindViewHolder(AboutHolder holder, int position) {
        holder.text.setText(title[position]);
        holder.image.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return title.length;
    }
}
class AboutHolder extends RecyclerView.ViewHolder{

    TextView text;
    ImageView image;
    LinearLayout row;

    public AboutHolder(View view, final OnRowClickListener listener) {
        super(view);

        text = (TextView) view.findViewById(R.id.about_title_row);
        image = (ImageView) view.findViewById(R.id.about_image_row);
        row = (LinearLayout) view.findViewById(R.id.single_row_about);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRowClick(getAdapterPosition());
            }
        });

    }
}