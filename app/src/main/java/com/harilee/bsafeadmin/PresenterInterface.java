package com.harilee.bsafeadmin;

import android.app.Notification;

public interface PresenterInterface {

    void loginUser(String number);

    void handleSuccess(LoginModel loginModel);

    void handleFailure(Throwable throwable);

    void getAdminDetails(String adminId);

    void handleSuccess(AdminModel adminModel);

    void getPassenger(String preference, String s);

    void handleSuccess(PassModel passModel);

    void sendNotification(String preference);

    void handleSuccess(NotificationModel notificationModel);
}
