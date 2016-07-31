package history.facebook.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import history.facebook.Data.NestGraphData;
import history.facebook.facebookhistory.R;


/**
 * Created by Tuan on 4/10/2016.
 */
public class NestGraphDataAdapter extends ArrayAdapter<NestGraphData> {
    List<NestGraphData> listData = null;
    Context mContext = null;
    int firstPos  = -1;

    public NestGraphDataAdapter(Context context, int resource, List<NestGraphData> items) {
        super(context, resource, items);
        this.listData = items;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position < 0 || position > listData.size()) {
            return null;
        }

        View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_ex_graph_item , null);
        if(position == firstPos) {
            layout.setBackgroundColor(Color.parseColor("#ffffff00"));
        }

        TextView ttYear = (TextView) layout.findViewById(R.id.txt_sort_time_year);
        TextView ttMonth = (TextView) layout.findViewById(R.id.txt_sort_time_month);

        TextView tt1 = (TextView) layout.findViewById(R.id.txt_title);
        TextView tt2 = (TextView) layout.findViewById(R.id.txt_time);

        Calendar calendar = listData.get(position).getDateTime();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        int preYear = 0;
        int preMonth = 0;
        if(position > 0) {
            Calendar preCalendar = listData.get(position - 1).getDateTime();
            preYear = preCalendar.get(Calendar.YEAR);
            preMonth = preCalendar.get(Calendar.MONTH) + 1;
        }

        if(year != preYear) {
            ttYear.setText("Year: " + year);
            ttYear.setVisibility(View.VISIBLE);
        }
        if(month != preMonth) {
            ttMonth.setText("Month: " + month);
            ttMonth.setVisibility(View.VISIBLE);
        }

        tt1.setText(listData.get(position).getTitle());
        tt2.setText(listData.get(position).getCreatedTime());

        return layout;
    }

    public void setSelection(int currentPosition) {
        this.firstPos = currentPosition;
    }
}
