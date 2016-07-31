package history.facebook.facebookhistory.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import history.facebook.MyApplication;
import history.facebook.facebookhistory.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    /*************** VARIABLES AND CONSTANT VARIABLES************************/
    private CallbackManager callbackManager;

    private String TAG = "LOGIN_FACEBOOK";

    /*************** OVERRIDE ACTIVITY FUNCTION************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        uiLoginFacebook();

        if(MyApplication.getInstance().getToken() != null) {
            startFbHistory();
        }
    }

    /*************** UI FUNCTION************************/
    /**
     * Login with Facebook Account
     */
    public void uiLoginFacebook() {
        Button btnFacebook = (Button) findViewById(R.id.btn_facebook);
        final LoginManager manager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG , "login Success");
                startFbHistory();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

        btnFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.logInWithReadPermissions(LoginActivity.this , Arrays.asList("public_profile", "user_friends" , "user_posts" , "user_status"));
            }
        });
    }

    private void startFbHistory(){
        Intent intent = new Intent(this , FHistoryActivity.class);
        startActivity(intent);
        finish();
    }
}

