package diana.cabinetmedical;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Diane on 6/27/2016.
 */
public class ReviewAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Review> reviews;

    public ReviewAdapter(Activity activity, List<Review> reviews){
        this.activity = activity;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null){
            inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView==null){
            convertView= inflater.inflate(R.layout.custom_review,null);
        }

        TextView numeUserComment = (TextView) convertView.findViewById(R.id.userComment_nume);
        RatingBar rateUser = (RatingBar) convertView.findViewById(R.id.customRate);
        TextView comment = (TextView) convertView.findViewById(R.id.customComment);

        Review i = reviews.get(position);
        numeUserComment.setText(i.getCommentUserName());
        rateUser.setRating(i.getRate());
        comment.setText(i.getComment());

        return convertView;
    }
}
