package com.azubal.go4lunch.ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.azubal.go4lunch.R;
import com.azubal.go4lunch.models.Restaurant;
import com.azubal.go4lunch.utils.ToastUtil;
import com.azubal.go4lunch.viewmodels.UserViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    UserViewModel authAppViewModel;
    List<Restaurant> restaurantListLocal;
    List<Restaurant> restaurantListLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authAppViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        startSignIn();
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private void startSignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
                );

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setTheme(R.style.LoginTheme)
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();
        signInLauncher.launch(signInIntent);

    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            ToastUtil.displayToastLong(getString(R.string.ToastLoginSuccessfully),getApplicationContext());
            authAppViewModel.createUser();
            startActivityMain();
            finish();
        } else if(response == null) {
            finish();
        }else if (result.getResultCode() == RESULT_CANCELED){
            ToastUtil.displayToastLong(getString(R.string.ToastLoginFailed),getApplicationContext());
            finish();
        }
    }

    private void startActivityMain(){
        Intent MainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(MainActivity);
    }

}