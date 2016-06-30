package adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mystudy.R;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bean.GirlsResult;
import holder.GirlsViewHolder;
import holder.RecyclerViewHolder;
import utils.FragmentType;
import utils.ImageloadFresco;

/**
 * Created by Administrator on 2016/6/13.
 */
public class GirlsAdapter extends RecyclerViewAdapter<RecyclerViewHolder>{
    private List<GirlsResult.Result> resultList = new ArrayList<>();
    private List<GirlsResult.Result> girls = new ArrayList<>();
    private List<Integer> mHeights = new ArrayList<>();
    private FragmentType fragmentType;

    public GirlsAdapter(FragmentType fragmentType,Context context){
        this.fragmentType = fragmentType;
        this.mContext = context;
    }

    public void setResult(GirlsResult result){
        this.resultList.clear();
        this.resultList.addAll(result.getResult());
    }

    public void setGirls(GirlsResult result){
        this.girls.clear();
        this.girls.addAll(result.getResult());
    }

    public void setStaggerHeights(GirlsResult result){
        for (int i = 0; i < result.getResult().size(); i++) {
            mHeights.add((int) (200 + Math.random() * 200));
        }
    }

    @Override
    public GirlsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = null;
        switch (fragmentType){
            case Android:
                item = LayoutInflater.from(mContext).inflate(R.layout.android_item,parent,false);
                break;
            case Xiatuijian:
                item = LayoutInflater.from(mContext).inflate(R.layout.grid_item,parent,false);
                break;
            case Xiuxishipin:
                break;
            case tuozhanzhiyuan:
                item = LayoutInflater.from(mContext).inflate(R.layout.grid_item,parent,false);
                break;
        }

        return new GirlsViewHolder(item,this);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if(fragmentType == FragmentType.tuozhanzhiyuan){
            ViewGroup.LayoutParams params = ((GirlsViewHolder) holder).getImageView().getLayoutParams();
            params.height = mHeights.get(position);
        }
        GirlsViewHolder holder1 = (GirlsViewHolder) holder;
        GirlsResult.Result data = resultList.get(position);
        int girlSize = girls.size();
        GirlsResult.Result meizi = position<girlSize?girls.get(position):girls.get((int) (Math.random()*girlSize));
        holder1.getTitle().setText(data.getDesc());
        holder1.getTime().setText(data.getPublishedAt().substring(0,10));
        String url = meizi.getUrl();

        new ImageloadFresco.ImageloadFrescoBuilder(url,mContext,holder1.getImageView())
                .setProgressBarImage(null)
                .build();
        /*Picasso.with(mContext)
                .load(url)
                .placeholder(R.mipmap.placeholder)
                .error(R.mipmap.error)
                .fit()
                .centerCrop()
                .tag(holder1.getImageView().getContext())
                .into(holder1.getImageView());*/
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

}
