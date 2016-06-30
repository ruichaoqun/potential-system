package bean;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Administrator on 2016/6/14.
 */
public interface GirlsResultCall {

    @GET("data/{type}/{count}/{pageIndex}")
    Call<GirlsResult> getData(@Path("type") String type,
                              @Path("count") int count,
                              @Path("pageIndex") int pageIndex);

    @GET("data/{type}/{count}/{pageIndex}")
    Observable<GirlsResult> htttpsGetData(@Path("type") String type,
                                      @Path("count") int count,
                                      @Path("pageIndex") int pageIndex);

}
