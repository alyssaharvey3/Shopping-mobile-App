package com.handstandsam.maintainableespresso.features.login;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.handstandsam.maintainableespresso.R;
import com.handstandsam.maintainableespresso.mockaccount.ProduceMockAccount;
import com.handstandsam.maintainableespresso.models.User;
import com.handstandsam.maintainableespresso.network.model.LoginRequest;
import com.handstandsam.maintainableespresso.preferences.UserPreferences;
import com.handstandsam.maintainableespresso.repository.SessionManager;
import com.handstandsam.maintainableespresso.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class LoginPresenter {

    private final LoginActivity.LoginView view;

    @Inject
    SessionManager sessionManager;

    @Inject
    UserPreferences userPreferences;

    @Inject
    UserRepository userRepository;


    public LoginPresenter(@NonNull LoginActivity.LoginView view) {
        this.view = view;
        view.getAppComponent().inject(this);
    }

    public void onResume(Intent intent) {

        if (sessionManager.isLoggedIn()) {
            view.kickToHomeScreen();
        }

        //Nothing for now...
        String username = userPreferences.getLastLoggedInUsername();
        if (username != null) {
            view.setUsername(username);
        }

        view.setRememberMe(userPreferences.getRememberMe());
    }

    public void loginClicked() {
        final boolean rememberMe = view.isRememberMeChecked();
        final String username = view.getUsername();
        final String password = view.getPassword();
        userRepository.login(new LoginRequest(username, password)).subscribe(new SingleObserver<User>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(User user) {
                userPreferences.setRememberMe(rememberMe, view.getUsername());
                sessionManager.setCurrentUser(new ProduceMockAccount().getUser());
                view.startHomeActivity();
            }

            @Override
            public void onError(Throwable e) {
                view.showToast(R.string.invalid_username_or_password);
            }
        });
    }
}
