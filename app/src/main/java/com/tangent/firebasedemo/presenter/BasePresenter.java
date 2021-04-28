//package com.example.firebasedemo.presenter;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.common.api.Response;
//
//public abstract class BasePresenter<B extends BaseBody, M extends BaseModel, R> {
//
//    protected static final String mainURL = "https://selfishh.herokuapp.com/";
//    protected final String localURI = "http://localhost:8080";
//
//    protected static final String authTokenForDemo = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJoMVA0R1lzdjc5WUFZSDVKL0RxYmNWU0JIMUV1bHBHazNJenRhT3FLL3V3PSIsImVtYWlsIjoiYWJjQGdtYWlsLmNvbSIsImlhdCI6MTYxNjE2OTE4OX0.r8WMFNBTWG2xJLJNW8XIAW2sxyPxFWcwRue3JaZpjuo";
//
//    protected final Retrofit mRetrofit;
//
//    protected final Context context;
//
//    protected final IView<M> view;
//
//    protected Presenter name;
//
//    protected BasePresenter(Context context, IView<M> view, Presenter name) {
//        this.context = context;
//        this.view = view;
//        this.name = name;
//        this.mRetrofit = new Retrofit.Builder()
//                .baseUrl(mainURL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//    }
//
//    protected abstract R createRetroInterface();
//
//    public abstract void sendRequest(B b);
//
//    protected Callback<M> mCallback = new Callback<M>() {
//        @Override
//        public void onResponse(@NonNull Call<M> call, @NonNull Response<M> response) {
//            Timber.d("response: %s", response.raw());
//            if (response.isSuccessful()) {
//                view.onResponseSuccess(response.body(), name);
//            } else {
//                Timber.d("responseError: %s", response.errorBody());
//                view.onResponseFailure();
//            }
//        }
//
//        @Override
//        public void onFailure(@NonNull Call<M> call, @NonNull Throwable t) {
//            Timber.d("Failure: Throwable:%s", t.getMessage());
//            view.onResponseFailure();
//        }
//    };
//
//    public interface IView<M extends BaseModel> {
//        void onResponseSuccess(M m, Presenter name);
//        void onResponseFailure();
//    }
//
//}
