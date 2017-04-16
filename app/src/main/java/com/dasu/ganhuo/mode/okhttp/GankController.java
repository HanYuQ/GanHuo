package com.dasu.ganhuo.mode.okhttp;

import com.dasu.ganhuo.BuildConfig;
import com.dasu.ganhuo.mode.logic.category.GanHuoEntity;
import com.dasu.ganhuo.mode.logic.home.SomedayGanHuoEntity;
import com.dasu.ganhuo.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by dasu on 2017/4/8.
 */
public class GankController {
    private static final String TAG = GankController.class.getSimpleName();
    //干货类型
    public static final String TYPE_ANDROID = "Android";
    public static final String TYPE_WEB = "前端";
    public static final String TYPE_RECOMMENT = "瞎推荐";
    public static final String TYPE_IOS = "iOS";
    public static final String TYPE_VIDEO = "休息视频";
    public static final String TYPE_MEIZI = "福利";
    public static final String TYPE_APP = "App";
    public static final String TYPE_OTHER = "扩展资源";
    //默认每次只加载10个数据
    private static final int DEFAULT_LOAD_COUNTS = 10;

    private static Gson sGson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:SS")
            .serializeNulls()
            .create();

    private static GankApi getGankApi() {
        return GankApiSingleton.mInstance;
    }

    /**
     * 获取指定类型的干货，默认一次加载10条信息
     *
     * @param type     取值有：{@link #TYPE_ANDROID}, {@link #TYPE_APP}, {@link #TYPE_IOS}
     *                 {@link #TYPE_MEIZI}, {@link #TYPE_OTHER}, {@link #TYPE_RECOMMENT},
     *                 {@link #TYPE_VIDEO}, {@link #TYPE_WEB}
     * @param page     分页加载
     * @param callback
     */
    public static void getSpecifyGanHuo(final String type, int page, final RetrofitListener<List<GanHuoEntity>> callback) {
        LogUtils.d(TAG, "[getSpecifyGanHuo] 正在下载干货，类型： " + type + " ，下载数量： " + DEFAULT_LOAD_COUNTS + " ，下载第 " + page + " 页数据");
        getGankApi().getSpecifyGanHuo(type, DEFAULT_LOAD_COUNTS, page).enqueue(new Callback<GankResEntity>() {
            @Override
            public void onResponse(Call<GankResEntity> call, Response<GankResEntity> response) {
                if (response.isSuccessful()) {
                    LogUtils.d(TAG, "[getSpecifyGanHuo]" + type + "干货下载成功： " + response.body().toString());
                    Object results = response.body().getResults();
                    Type t = new TypeToken<List<GanHuoEntity>>() {
                    }.getType();
                    List<GanHuoEntity> data = sGson.fromJson(sGson.toJson(results), t);
                    callback.onSuccess(data);
                } else {
                    //获取数据失败，可能是 404 之类的原因
                    LogUtils.e(TAG, "[getSpecifyGanHuo]" + type + "干货下载失败，code： " + response.code());
                    callback.onError("请求失败，code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GankResEntity> call, Throwable t) {
                //可能是网络问题，请求发送失败
                LogUtils.e(TAG, "[getSpecifyGanHuo]" + type + "干货下载失败", t);
                callback.onError(t.getMessage());
            }
        });
    }

    /**
     * 获取发过干货的日期
     *
     * @param callback
     */
    public static void getPublishDate(final RetrofitListener<List<String>> callback) {
        LogUtils.d(TAG, "[getPublishDate] 正在获取发过干货的日期列表...");
        getGankApi().getPublishDate().enqueue(new Callback<GankResEntity>() {
            @Override
            public void onResponse(Call<GankResEntity> call, Response<GankResEntity> response) {
                if (response.isSuccessful()) {
                    LogUtils.d(TAG, "[getPublishDate] 发过干货的日期： " + response.body().toString());
                    Object results = response.body().getResults();
                    Type t = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> data = sGson.fromJson(sGson.toJson(results), t);
                    callback.onSuccess(data);
                } else {
                    //获取数据失败，可能是 404 之类的原因
                    LogUtils.e(TAG, "[getPublishDate] 请求失败，code: " + response.code());
                    callback.onError("请求失败，code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GankResEntity> call, Throwable t) {
                //可能是网络问题，请求发送失败
                LogUtils.e(TAG, "[getPublishDate] 请求失败", t);
                callback.onError(t.getMessage());
            }
        });
    }

    /**
     * 获取某一天的干货数据
     *
     * @param someday 格式：2017-04-06 yyyy-MM-dd
     * @param callback
     */
    public static void getSomedayGanHuo(final String someday, final RetrofitListener<SomedayGanHuoEntity> callback) {
        LogUtils.d(TAG, "[getSomedayGanHuo] 请求" + someday + " 的数据...");
        final String[] date = someday.split("-");
        if (date.length != 3) {
            LogUtils.e(TAG, "[getSomedayGanHuo] 参数 " + someday + " 格式错误，请修正，格式如：2017-05-01");
            return;
        }
        getGankApi().getSomedayGanHuo(date[0], date[1], date[2]).enqueue(new Callback<GankResEntity>() {
            @Override
            public void onResponse(Call<GankResEntity> call, Response<GankResEntity> response) {
                if (response.isSuccessful()) {
                    LogUtils.d(TAG, "[getSomedayGanHuo] " + someday + " 的数据： " + response.body().toString());

                    Object results = response.body().getResults();
                    Type t = new TypeToken<SomedayGanHuoEntity.Results>() {
                    }.getType();
                    SomedayGanHuoEntity.Results resultEntity = sGson.fromJson(sGson.toJson(results), t);
                    SomedayGanHuoEntity data = new SomedayGanHuoEntity();
                    data.setCategory(response.body().getCategory());
                    data.setResults(resultEntity);
                    callback.onSuccess(data);
                } else {
                    //返回404之类的错误
                    LogUtils.e(TAG, "[getSomedayGanHuo] 请求失败，code: " + response.code());
                    callback.onError("请求失败，code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GankResEntity> call, Throwable t) {
                //可能是网络问题，请求发送失败
                callback.onError(t.getMessage());
                LogUtils.e(TAG, "[getSomedayGanHuo] 请求失败", t);
            }
        });
    }

    private static class GankApiSingleton {
        private static GankApi mInstance = RetrofitHelper.newRetrofit(BuildConfig.GAND_SERVICE).create(GankApi.class);
    }

}