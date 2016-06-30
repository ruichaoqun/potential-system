package utils;

import android.util.Log;

import bean.GirlsResult;
import bean.GirlsResultCall;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;


/**
 * Created by Administrator on 2016/6/14.
 */
public class HttpUtils {
    public static Retrofit retrofit =
            new Retrofit.Builder()
            .baseUrl(GankUrl.GANK_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    /*public static void getData(final String type,int count,int pageIndex, final IcallBack<GirlsResult> callback){
        Subscription subscription = retrofit.create(GirlsResultCall.class)
                .htttpsGetData(type,count,pageIndex)
                .map(new Func1<GirlsResult, Observable<GirlsResult>>() {
                    @Override
                    public Observable<GirlsResult> call(GirlsResult girlsResult) {
                        return null;
                    }
                })
        Call<GirlsResult> call = retrofit.create(GirlsResultCall.class).getData(type,count,pageIndex);
        final String key = type+count+pageIndex;
        call.enqueue(new Callback<GirlsResult>() {
            @Override
            public void onResponse(Response<GirlsResult> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    GirlsResult result = response.body();
                    Log.i("AAA",result.toString());
                    if(!result.isError()&&result.getResult()!=null){
                        callback.onSuccess(type, key, result);
                    }
                    else{
                        callback.onFailure(type,key,"数据错误");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(type,key,"请求失败");
            }
        });
    }*/

}
