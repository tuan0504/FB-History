package history.facebook.MVP.Presenter;

import android.app.Activity;

import history.facebook.Data.GraphReponseResultData;
import history.facebook.Data.NestGraphData;
import history.facebook.MVP.Model.IModelSearchData;
import history.facebook.MVP.View.IViewFSearchContent;
import history.facebook.MyApplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

/**
 * Created by tuann on 3/22/16.
 */
public class PresenterSearchContent {
    /************************** VARIABLES AND CONSTANTS *************************************/
    private List<GraphReponseResultData> listGraphData  = new ArrayList<>();

    private final String TAG = "FB HISTORY";
    private final String GRAPH_POSTS = "me/posts";
    private final String GRAPH_PHOTOS = "me/photos";
    private final String GRAPH_LIKES = "me/likes";

    private final String GRAPH_PARA_LIMIT = "limit";
    private final String GRAPH_PARA_ACCESS_TOKEN = "access_token";
    private final String GRAPH_PARA_FORMAT = "format";
    private final int GRAPH_LIMIT_NUMBER = 100;
    private final int GRAPH_MAX_RECORD = 20;

    private String timeText = "2014-05-18";

    IViewFSearchContent fSearchContent;
    IModelSearchData iModelSearchData;

    ProgressDialog progressDialog ;

    /************************** PUBLIC FUNCTION *************************************/
    public PresenterSearchContent(Activity activity, IModelSearchData iModelDictData, IViewFSearchContent fragment) {
        fSearchContent = fragment;

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Please wait a little....");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
    }

    public void setFSearchContent(IViewFSearchContent fragment) {
        fSearchContent = fragment;
    }

    public void setiModelSearchData(IModelSearchData iModelDictData) {
        iModelSearchData = iModelDictData;
    }

    public void showSearchSuggest(String timeText) {
        progressDialog.show();
        listGraphData.clear();
        this.timeText = timeText;
        callGraphApi(null);
    }

    /************************** PRIVATE FUNCTION *************************************/
    private void callGraphApi(Bundle parameter) {
        final AccessToken token = MyApplication.getInstance().getToken();
        if(parameter == null) {
            parameter = new Bundle();
            parameter.putInt(GRAPH_PARA_LIMIT, GRAPH_LIMIT_NUMBER);
        }

        /* make the API call */
        new GraphRequest(
                token,
                GRAPH_POSTS,
                parameter,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        String sReponse = response.getRawResponse();
                        if(sReponse != null) {
                            Gson gson = new Gson();
                            GraphReponseResultData resultData = gson.fromJson(sReponse, GraphReponseResultData.class);

                            if(listGraphData.size() >= GRAPH_MAX_RECORD) {
                                listGraphData.remove(0);
                            }
                            listGraphData.add(resultData);

                            if(!resultData.isLookingDate(timeText)) {
                                if(resultData.isNextPage()) {
                                    Bundle parameter = new Bundle();
                                    Map<String , String> paraValue = splitQuery(resultData.getNextPage());
                                    paraValue.remove(GRAPH_PARA_LIMIT);
                                    paraValue.remove(GRAPH_PARA_ACCESS_TOKEN);
                                    paraValue.remove(GRAPH_PARA_FORMAT);
                                    if(paraValue != null) {
                                        List<String> keys = new ArrayList<String>(paraValue.keySet());
                                        for(String key : keys ) {
                                            String value = paraValue.get(key);
                                            if(value.matches("[0_9]+")) {
                                                parameter.putInt(key, Integer.valueOf(value));
                                            } else {
                                                parameter.putString(key, value);
                                            }
                                        }
                                    }
                                    parameter.putInt(GRAPH_PARA_LIMIT, GRAPH_LIMIT_NUMBER);
                                    callGraphApi(parameter);
                                } else {
                                    showSearchResult();
                                }
                            } else {
                                showSearchResult();
                            }
                        }
                    }
                }
        ).executeAsync();
    }

    private static Map<String, String> splitQuery(String stringURL) {
        if(stringURL == null || !stringURL.contains("?")) {
            return null;
        }
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = stringURL.substring(stringURL.indexOf('?') + 1 , stringURL.length());
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(pair.substring(0, idx),pair.substring(idx + 1));
        }
        return query_pairs;
    }

    private void showSearchResult() {
        List<NestGraphData> result = new ArrayList<>();
        for(GraphReponseResultData data : listGraphData) {
            if(data.getData() != null) {
                result.addAll(data.getData());
            }
        }
        progressDialog.dismiss();
        fSearchContent.showSearch(result);
    }
}

