package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import holder.RecyclerViewHolder;

/**
 * Created by Administrator on 2016/6/12.
 */
public abstract class RecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements RecyclerViewHolder.OnItemClickListener{
    protected OnItemClickListener mOnItemClickListener;
    protected Context mContext;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClic(RecyclerView.ViewHolder viewHolder,int position,View view);
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder vh, int position, View v) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClic(vh,position,v);
        }
    }

}
