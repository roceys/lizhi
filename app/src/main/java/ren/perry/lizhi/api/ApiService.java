package ren.perry.lizhi.api;

import ren.perry.lizhi.bean.SplashBean;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author perrywe
 * @date 2019/4/10
 * WeChat  917351143
 */
public interface ApiService {

    @POST("/lizhi/splash")
    Observable<SplashBean> splash();

}