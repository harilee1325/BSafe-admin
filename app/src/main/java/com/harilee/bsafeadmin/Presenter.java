package com.harilee.bsafeadmin;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Presenter implements PresenterInterface {

    private ViewInterface viewInterface;
    private CompositeDisposable compositeDisposable;
    private ApiInterface apiInterFace;

    public Presenter(ViewInterface viewInterface) {

        this.viewInterface = viewInterface;
        this.compositeDisposable = new CompositeDisposable();
        this.apiInterFace = ApiClient.getClient().create(ApiInterface.class);
    }

    private HomeInterface homeInterface;

    public Presenter(HomeInterface homeInterface) {
        this.homeInterface = homeInterface;
        this.compositeDisposable = new CompositeDisposable();
        this.apiInterFace = ApiClient.getClient().create(ApiInterface.class);
    }


    @Override
    public void loginUser(String number) {
        Observable<LoginModel> observable = apiInterFace.loginUser(number);
        compositeDisposable.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccess, this::handleFailure));
    }

    @Override
    public void handleSuccess(LoginModel loginModel) {

        if (loginModel.getSuccess().equalsIgnoreCase("yes")) {
            viewInterface.getResponse(loginModel);
        } else {
            viewInterface.showMessage(loginModel.getMessage());
        }
    }

    @Override
    public void handleFailure(Throwable throwable) {
        if (viewInterface != null) {
            viewInterface.showMessage(throwable.getLocalizedMessage());
        }
        else if (homeInterface!=null){
            Log.e(TAG, "handleFailure: "+throwable.getLocalizedMessage() );
            homeInterface.showMessages(throwable.getLocalizedMessage());
        }
    }

    @Override
    public void getAdminDetails(String adminId) {
        Observable<AdminModel> observable = apiInterFace.getAdmin(adminId);
        compositeDisposable.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccess, this::handleFailure));
    }

    @Override
    public void handleSuccess(AdminModel adminModel) {

        if (adminModel.getSuccess().equalsIgnoreCase("yes")){
            homeInterface.getResponse(adminModel);
        }else{
            homeInterface.showMessages(adminModel.getMessage());
        }
    }

    @Override
    public void getPassenger(String preference, String s) {
        Observable<PassModel> observable = apiInterFace.getPassengee(preference,s);
        compositeDisposable.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccess, this::handleFailure));
    }

    @Override
    public void handleSuccess(PassModel passModel) {
        homeInterface.getResponse(passModel);
    }

    @Override
    public void sendNotification(String preference) {
        Observable<NotificationModel> observable = apiInterFace.sendNotification(preference);
        compositeDisposable.add(observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleSuccess, this::handleFailure));
    }

    @Override
    public void handleSuccess(NotificationModel notificationModel) {

        homeInterface.getResponse();
    }


    public interface ViewInterface {

        void showMessage(String message);

        void getResponse(LoginModel loginModel);
    }

    public interface HomeInterface {

        void showMessages(String message);

        void getResponse(AdminModel adminModel);

        void getResponse(PassModel passModel);

        void getResponse();
    }

}
