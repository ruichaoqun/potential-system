package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.mystudy.DetailActivity;
import com.example.mystudy.MainActivity;

import java.util.Arrays;

import adapter.GirlsAdapter;
import adapter.RecyclerViewAdapter;
import bean.GirlsResult;
import bean.GirlsResultCall;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import utils.FragmentType;
import utils.GankUrl;
import utils.HttpUtils;
import utils.IcallBack;

/**
 * Created by Administrator on 2016/6/14.
 */
public class GirlsFragment extends BaseFragment{
    private GirlsResult articles;
    private GirlsResult girls;
    private FragmentType type;
    private String keyUrl;
    private int count = 20;
    private RecyclerViewAdapter mAdapter;

    public GirlsFragment(FragmentType type) {
        this.type = type;
        switch (type){
            case Android:
                keyUrl = GankUrl.FLAG_Android;
                break;
            case Xiatuijian:
                keyUrl = GankUrl.FLAG_Recommend;
                break;
            case tuozhanzhiyuan:
                keyUrl = GankUrl.FLAG_Expand;
                break;
            case Xiuxishipin:
                keyUrl = GankUrl.FLAG_Video;
                break;
        }
    }

    /*private IcallBack<GirlsResult> icallBack = new IcallBack<GirlsResult>() {
        @Override
        public void onSuccess(String flag, String key, GirlsResult girlsResult) {
            if(flag != GankUrl.FLAG_GIRLS){
                articles = girlsResult;
                ((GirlsAdapter)adapter).setResult(articles);
                if(flag == GankUrl.FLAG_Expand){
                    ((GirlsAdapter)adapter).setStaggerHeights(articles);
                }
                HttpUtils.getData(GankUrl.FLAG_GIRLS,count,1,icallBack);
            }else{
                girls = girlsResult;
                ((GirlsAdapter)adapter).setGirls(girls);
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }

        @Override
        public void onFailure(String flag, String key, String why) {
            swipeRefreshLayout.setRefreshing(false);
        }
    };
*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter=new GirlsAdapter(type,getActivity());
    }

    /**
     *
     * @param viewHolder
     * @param position
     * @param view
     * 处理item点击跳转事件
     */
    @Override
    void onRecyclerItemClick(RecyclerView.ViewHolder viewHolder, int position, View view) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("WEB_URL",articles.getResult().get(position).getUrl());
                intent.putExtra("WEB_WHO",articles.getResult().get(position).getWho());
                intent.putExtra("GIRL_URL", girls.getResult().get(position).getUrl());
                startActivity(intent);
    }

    @Override
    public void setLayoutManager() {
        switch (type){
            case Android:
                layoutManager = new LinearLayoutManager(getActivity());
                break;
            case Xiatuijian:
                layoutManager = new GridLayoutManager(getActivity(),2);
                break;
            case tuozhanzhiyuan:
                layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                break;
            case Xiuxishipin:
                layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                break;
        }
    }

    @Override
    public void setAdapter() {
        adapter = mAdapter;
    }

    @Override
    protected int findLastVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager1 = recyclerView.getLayoutManager();
        int lastVisibleItemPosition = 0;
        switch (type){
            case Android:
                lastVisibleItemPosition = ((LinearLayoutManager)layoutManager1).findLastCompletelyVisibleItemPosition();
                break;
            case Xiatuijian:
                lastVisibleItemPosition = ((GridLayoutManager)layoutManager1).findLastCompletelyVisibleItemPosition();
                break;
            case tuozhanzhiyuan:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager1;
                int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastPositions);
                Arrays.sort(lastPositions);
                lastVisibleItemPosition = lastPositions[1];
                break;
            case Xiuxishipin:
                break;
        }
        return lastVisibleItemPosition;
    }

    @Override
    protected Subscription initData(boolean loadMore) {
        swipeRefreshLayout.measure(View.MEASURED_SIZE_MASK, View.MEASURED_HEIGHT_STATE_SHIFT);
        if(loadMore == true){
            count +=10;
        }
        return HttpUtils.createService(GirlsResultCall.class)
                .htttpsGetData(keyUrl, count, 1)
                //使用flatmap实现嵌套的网络请求
                .flatMap(new Func1<GirlsResult, Observable<GirlsResult>>() {
                    @Override
                    public Observable<GirlsResult> call(GirlsResult girlsResult) {
                        saveData(girlsResult, false);
                        return HttpUtils.createService(GirlsResultCall.class)
                                .htttpsGetData(GankUrl.FLAG_GIRLS, count, 1);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GirlsResult>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        swipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(GirlsResult girlsResult) {
                        saveData(girlsResult, true);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void saveData(GirlsResult girlsResult,boolean isGirls) {
        if(!isGirls){
            articles = girlsResult;
            ((GirlsAdapter)adapter).setResult(articles);
            if(keyUrl == GankUrl.FLAG_Expand){
                ((GirlsAdapter)adapter).setStaggerHeights(articles);
            }
        }else{
            girls = girlsResult;
            ((GirlsAdapter)adapter).setGirls(girls);
            if(girls.getResult().size()!=0){
                adapter.notifyDataSetChanged();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
