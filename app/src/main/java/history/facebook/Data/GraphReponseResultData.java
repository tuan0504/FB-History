package history.facebook.Data;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Tuan on 7/30/2016.
 */
public class GraphReponseResultData {
    private List<NestGraphData> data;
    private PagingGraphData paging;

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public boolean isLookingDate(String s) {
        try {
            if (data != null && !data.isEmpty()) {
                Log.e("TUAN" , "REQUEST POSTS : " + data.get(0).getCreatedTime());
                long endTime = formatter.parse(data.get(0).getCreatedTime()).getTime();
                long startTime = formatter.parse(data.get(data.size() - 1).getCreatedTime()).getTime();
                long value = formatter.parse(s).getTime();

                if (startTime <= value && value <= endTime) {
                    return true;
                }
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public boolean isNextPage() {
        if(paging != null && paging.getNext() != null && paging.getNext().length() > 1) {
            return  true;
        }
        return false;
    }

    public String getNextPage() {
        return paging.getNext();
    }
    public String getPrevPage() {
        return paging.getPrev();
    }

    public List<NestGraphData> getData() {
        return data;
    }
}
