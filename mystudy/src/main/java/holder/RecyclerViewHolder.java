package holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by Administrator on 2016/6/12.
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener{
        void onItemClick(RecyclerView.ViewHolder vh,int position,View v);
    }
    public RecyclerViewHolder(View itemView,OnItemClickListener onItemClickListener) {
        super(itemView);
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(this,getLayoutPosition(),v);
        }
    }
}
