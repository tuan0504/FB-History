package history.facebook.UI.Factory;

import android.support.v4.app.Fragment;

import history.facebook.facebookhistory.Fragment.FSearchContent;

/**
 * Created by Tuan on 4/10/2016.
 */
public class FactoryContentView {

    public final static int SEARCH_CONTENT = 0;
    public final static int SHARE_CONTENT = 1;
    public final static int LICENSE_CONTENT = 2;
    public final static int RATE_APP = 3;

    private static Fragment instance;

    public static Fragment getInstance(int type) {
        switch (type) {
            case SEARCH_CONTENT:
                instance = FSearchContent.getInstance();
                break;
            case RATE_APP:
                break;
            case SHARE_CONTENT:
                break;
            case LICENSE_CONTENT:
                break;
            default:
                break;
        }
        return instance;
    }
}
