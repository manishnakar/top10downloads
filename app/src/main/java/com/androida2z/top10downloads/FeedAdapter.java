package com.androida2z.top10downloads;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by manishnakar on 17/09/16.
 */

public class FeedAdapter extends ArrayAdapter {


    private static final String TAG = "FeedAdapter";

    private final LayoutInflater layoutInflater;
    private final int layoutResources;
    private List<FeedEntry> applications;


    public FeedAdapter(Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);

        this.layoutResources = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView == null ){
            convertView = layoutInflater.inflate(layoutResources, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        FeedEntry currentApp = applications.get(position);

        viewHolder.tvName.setText(currentApp.getName());
        viewHolder.tvArtist.setText(currentApp.getArtist());
        viewHolder.tvSummary.setText(currentApp.getSummary());

        return convertView;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    private class ViewHolder{

        final TextView tvName;
        final TextView tvArtist;
        final TextView tvSummary;

        ViewHolder(View v){

            tvName = (TextView) v.findViewById(R.id.tvName);
            tvArtist = (TextView) v.findViewById(R.id.tvArtist);
            tvSummary = (TextView) v.findViewById(R.id.tvSummary);
        }


    }


}


