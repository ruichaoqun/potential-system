package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mystudy.R;

import adapter.RecyclerViewAdapter;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/6/12.
 */
public abstract class BaseFragment extends Fragment {
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView recyclerView;
    protected RecyclerViewAdapter<?> adapter;
    protected RecyclerView.LayoutManager layoutManager;
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener(){
        int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if(newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem +1 == layoutManager.getItemCount()){
                addSubscription(initData(true));
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            lastVisibleItem =findLastVisibleItemPosition();
        }
    };

    private CompositeSubscription mCompositeSubscription;

    public CompositeSubscription getCompositeSubscription(){
        if(mCompositeSubscription == null){
            mCompositeSubscription = new CompositeSubscription();
        }

        return this.mCompositeSubscription;
    }

    public void addSubscription(Subscription s) {
        if (s == null) {
            return;
        }

        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_layout,container,false);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipelayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        setAdapter();
        recyclerView.setAdapter(adapter);
        setLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClic(RecyclerView.ViewHolder viewHolder, int position, View view) {
                onRecyclerItemClick(viewHolder, position, view);
            }
        });
        recyclerView.addOnScrollListener(mScrollListener);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addSubscription(initData(false));
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addSubscription(initData(false));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCompositeSubscription != null){
            mCompositeSubscription.unsubscribe();
        }
    }

    abstract void onRecyclerItemClick(final RecyclerView.ViewHolder viewHolder,final int position, View view);
    public abstract void setLayoutManager();
    public abstract void setAdapter();
    protected abstract Subscription initData(boolean loadMore);
    protected abstract int findLastVisibleItemPosition();
}
