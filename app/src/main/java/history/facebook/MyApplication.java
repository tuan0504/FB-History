package history.facebook;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import history.facebook.Data.GraphReponseResultData;
import history.facebook.MVP.Model.IModelSearchData;

/**
 * Created by tuann on 4/12/16.
 */
public class MyApplication  extends Application {
    /************************** VARIABLES AND CONSTANTS *************************************/
    private AccessToken token ;

    private static MyApplication application;
    public static  MyApplication getInstance(){
        if(application == null) {
            application = new MyApplication();
        }
        return application;
    }

    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /************************** PUBLIC FUNCTION *************************************/
    public AccessToken  getToken() {
        if(token == null) {
            String stringToken = "EAACEdEose0cBACQKbIgtd7aMKXkmPlVPVyyUGSx0w1XreSk3ebHeGYlcyU9goYkLwZBnQoQP2r0TeKLrIpJh0Pk53yRs4lla4MFA574BwqhzKMsadbzppfSzG7FRWCzJZBDVIlpuIyf9O294ERZBHinUwVUTVXJNwTwiXNSvwZDZD";
            String facebokAppId = "1664360920556651";
            String userId = "1758722161082879";

            token = new AccessToken(stringToken, facebokAppId, userId, null, null, null, null, null);
        }
        return token;
    }

    public void setToken(AccessToken realToken) {
    }
}

