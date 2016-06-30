package utils;

/**
 * Created by Administrator on 2016/6/14.
 */
public interface IcallBack<T> {
    public void onSuccess(String flag, String key, T t);

    public void onFailure(String flag, String key, String why);
}
