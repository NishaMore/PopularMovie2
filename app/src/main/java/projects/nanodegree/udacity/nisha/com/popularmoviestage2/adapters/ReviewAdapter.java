package projects.nanodegree.udacity.nisha.com.popularmoviestage2.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.R;
import projects.nanodegree.udacity.nisha.com.popularmoviestage2.rest.model.ReviewDetails;

/**
 * Created by shani on 3/16/16.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    ArrayList<ReviewDetails> mReviewDetailses;

    public ReviewAdapter(ArrayList<ReviewDetails> reviewDetailses) {
        mReviewDetailses = reviewDetailses;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review_list, parent, false);
        ReviewHolder reviewHolder = new ReviewHolder(view);

        return reviewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {

        holder.author_textView.setText(mReviewDetailses.get(position).getAuthor());

        holder.review_textView.setText(mReviewDetailses.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewDetailses.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.author_textView)
        TextView author_textView;

        @Bind(R.id.review_textView)
        TextView review_textView;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
