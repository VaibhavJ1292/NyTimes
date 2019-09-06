package com.nytimes.ui.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.support.annotation.NonNull;
import android.util.Log;
import com.nytimes.R;
import com.nytimes.network.NetworkClient;
import com.nytimes.network.NoConnectivityException;
import com.nytimes.network.RetrofitClientHelper;
import io.reactivex.disposables.Disposable;
import kotlin.jvm.Transient;
import retrofit2.HttpException;

import java.io.IOException;

public abstract class BaseViewModel extends AndroidViewModel implements Observable {
    protected Application app;
    @Transient
    private PropertyChangeRegistry mCallbacks = null;

    protected NetworkClient mNetworkClient = new NetworkClient(RetrofitClientHelper.getInstance().getNetworkService(), app);

    protected Disposable mDisposable;

    protected MutableLiveData<String> errorMessageData = new MutableLiveData<>();

    protected MutableLiveData<Integer> apiErrorCode = new MutableLiveData<>();
    protected MutableLiveData<Boolean> showProcess = new MutableLiveData<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
        this.app = application;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessageData;
    }

    public LiveData<Integer> getErrorCode() {
        return apiErrorCode;
    }

    public LiveData<Boolean> getProgress() {
        return showProcess;
    }

    protected void setErrorMessage(String errorMessage) {
        errorMessageData.setValue(errorMessage);
    }

    protected void setApiErrorCode(int errorCode) {
        apiErrorCode.setValue(errorCode);
    }

    protected void setProgress(boolean b) {
        showProcess.setValue(b);
    }

    @Override
    public void onCleared() {
        if (mDisposable != null)
            mDisposable.dispose();
        super.onCleared();
    }

    @Override
    public void addOnPropertyChangedCallback(Observable.OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                mCallbacks = new PropertyChangeRegistry();
            }
        }
        mCallbacks.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(Observable.OnPropertyChangedCallback callback) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.remove(callback);
    }


    public void notifyPropertyChanged(int fieldId) {
        synchronized (this) {
            if (mCallbacks == null) {
                return;
            }
        }
        mCallbacks.notifyCallbacks(this, fieldId, null);
    }


    /**
     * To handle error got from api
     */
    public void handleApiErrors(String message) {
        showProcess.setValue(false);
        errorMessageData.setValue(message);
    }

    public void handleUnknownError() {
        showProcess.setValue(false);
        errorMessageData.setValue(app.getResources().getString(R.string.error_no_network));
    }

    public void handleConnectionError() {
        showProcess.setValue(false);
        errorMessageData.setValue(app.getResources().getString(R.string.error_try_again));
    }


    /**
     * Generic error handler.
     *
     * @param throwable the exception.
     */
    public void handleErrors(Throwable throwable) {
        Log.e("ERROR", "Error msg" + throwable.getMessage());
        throwable.printStackTrace();
        // EventBus.getDefault().post(throwable);
        if (throwable instanceof NoConnectivityException) {
            handleApiErrors(app.getResources().getString(R.string.error_no_network));
        } else if (throwable instanceof IOException) {
            handleApiErrors(app.getResources().getString(R.string.error_connect_to_internet));
        } else if (throwable instanceof HttpException) {
            handleApiErrors(throwable.getMessage());
        } else {
            handleApiErrors(throwable.getMessage());
        }
    }


}
