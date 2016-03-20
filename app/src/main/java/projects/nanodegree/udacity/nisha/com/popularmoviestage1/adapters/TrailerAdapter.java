package projects.nanodegree.udacity.nisha.com.popularmoviestage1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import projects.nanodegree.udacity.nisha.com.popularmoviestage1.R;
import projects.nanodegree.udacity.nisha.com.popularmoviestage1.rest.model.TrailerKeys;

/**
 * Created by shani on 3/15/16.
 */
public class TrailerAdapter extends  RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{


    private ArrayList<TrailerKeys> mTrailerKeyList;
    private Context mContext;

    public TrailerAdapter(ArrayList<TrailerKeys> trailerKeyList,Context context){
       mTrailerKeyList = trailerKeyList;
        mContext = context;
    }


    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.col_trailer_list,parent,false);
        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(v);
        return trailerViewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Picasso.with(mContext)
                .load("http://img.youtube.com/vi/" + mTrailerKeyList.get(position).getKey() + "/0.jpg")
                .into(holder.trailer_imageView);
    }

    @Override
    public int getItemCount() {
        return mTrailerKeyList.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView trailer_imageView;

        public TrailerViewHolder(View view){
            super(view);
            trailer_imageView = (ImageView)view.findViewById(R.id.trailer_thumbnail);
        }


        @Override
        public void onClick(View v) {

        }
    }

}
