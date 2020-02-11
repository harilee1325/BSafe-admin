package com.harilee.bsafeadmin;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("login-admin/{adminId}")
    Observable<LoginModel> loginUser(@Path("adminId") String number);

    @GET("admin/{adminId}")
    Observable<AdminModel> getAdmin(@Path("adminId") String adminId);

    @GET("passenger-data/{number}/{adminId}")
    Observable<PassModel> getPassengee(@Path("number") String preference,@Path("adminId") String s);

    @GET("accepted/{fcm}")
    Observable<NotificationModel> sendNotification(@Path("fcm") String preference);
}
