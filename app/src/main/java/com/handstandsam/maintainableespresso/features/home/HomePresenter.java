package com.handstandsam.maintainableespresso.features.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;
import com.handstandsam.maintainableespresso.MyAbstractApplication;
import com.handstandsam.maintainableespresso.models.Category;
import com.handstandsam.maintainableespresso.models.User;
import com.handstandsam.maintainableespresso.repository.CategoryRepository;
import com.handstandsam.maintainableespresso.repository.SessionManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class HomePresenter {

    private final Context applicationContext;
    private final HomeActivity.HomeView view;

    @Inject
    SessionManager sessionManager;

    @Inject
    CategoryRepository categoryRepository;

    public HomePresenter(@NonNull HomeActivity.HomeView homeView) {
        this.view = homeView;
        this.applicationContext = homeView.getContext().getApplicationContext();
        ((MyAbstractApplication) applicationContext).getAppComponent().inject(this);
        Timber.d("HomePresenter: " + new GsonBuilder().create().toJson(categoryRepository));
    }

    public void onResume(Intent intent) {
        categoryRepository.getCategories().subscribe(new SingleObserver<List<Category>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(List<Category> categories) {
                view.showCategories(categories);
            }

            @Override
            public void onError(Throwable e) {

            }
        });

        User currentUser = sessionManager.getCurrentUser();
        String welcomeStr = "Welcome back " + currentUser.getFirstname() + " " + currentUser.getLastname();
        view.setWelcomeMessage(welcomeStr);
    }
}
