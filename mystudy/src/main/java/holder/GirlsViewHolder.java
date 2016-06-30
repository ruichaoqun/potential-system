package holder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mystudy.R;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/6/13.
 */
public class GirlsViewHolder extends RecyclerViewHolder{
    public CardView getCardView() {
        return cardView;
    }

    private CardView cardView;
    private SimpleDraweeView imageView;
    private TextView title;
    private TextView time;
    public GirlsViewHolder(View itemView, OnItemClickListener onItemClickListener) {
        super(itemView, onItemClickListener);
        imageView = (SimpleDraweeView) itemView.findViewById(R.id.girl);
        title = (TextView) itemView.findViewById(R.id.title);
        time = (TextView) itemView.findViewById(R.id.time);
        cardView = (CardView) itemView.findViewById(R.id.cardview);
        itemView.setOnClickListener(this);
    }

    public SimpleDraweeView getImageView() {
        return imageView;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getTime() {
        return time;
    }
}
