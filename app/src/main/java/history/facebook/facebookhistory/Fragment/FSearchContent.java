package history.facebook.facebookhistory.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import history.facebook.Data.NestGraphData;
import history.facebook.MVP.Presenter.PresenterSearchContent;
import history.facebook.MVP.View.IViewFSearchContent;
import history.facebook.UI.Adapter.NestGraphDataAdapter;
import history.facebook.facebookhistory.Fragment.Interface.IFragmentContent;
import history.facebook.facebookhistory.R;

public class FSearchContent extends Fragment implements IViewFSearchContent, IFragmentContent {
    /************************** VARIABLES AND CONSTANTS *************************************/
    //Toolbar Component
    private TextView edtSearchText;
    private ImageButton btnSearch;
    private ListView mListView;

    private NestGraphDataAdapter mAdapter;
    CalendarView calendarView;
    Calendar cal;

    private PresenterSearchContent pSearchContent;

    private static FSearchContent instance;
    public static FSearchContent getInstance() {
        if(instance == null) {
            instance = new FSearchContent();
        }
        return instance;
    }

    /*************************** OVERRIDE FUNCTION ******************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(history.facebook.facebookhistory.R.layout.fragment_searchcontent, null);
        initUI(view);

        pSearchContent = new PresenterSearchContent(getActivity() , null , this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loadData();
        super.onViewCreated(view, savedInstanceState);
    }

    /*************** OVERRIDE FUNCTION for IFragmentContent************************/
    @Override
    public View getViewToolBar(final LayoutInflater layoutInflater) {
        View view = layoutInflater.inflate(history.facebook.facebookhistory.R.layout.toolbar_searchcontent, null);
        edtSearchText = (TextView)view.findViewById(history.facebook.facebookhistory.R.id.edt_search_text);
        edtSearchText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                calendarView.setVisibility(View.VISIBLE);
                return true;
            }
        });

        btnSearch = (ImageButton)view.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setVisibility(View.GONE);
                String time = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH);
                pSearchContent.showSearchSuggest(time);
            }
        });

        return view;
    }

    @Override
    public boolean onBack() {
        return false;
    }

    /*************** OVERRIDE FUNCTION for IViewFSearchContent************************/
    @Override
    public void showSearch(List<NestGraphData> listData) {
        if(listData == null && listData.isEmpty()) {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            return;
        }

        long min = listData.get(listData.size() - 1).getDateTime().getTimeInMillis();
        int position = 0;
        for(int i = listData.size() - 1 ; i > -1 ; i--) {
            long range = Math.abs(listData.get(i).getDateTime().getTimeInMillis() - cal.getTimeInMillis());
            if(range <= min ) {
                min = range;
                position = i;
            } else {
                break;
            }
        }

        mAdapter = new NestGraphDataAdapter(getContext() , R.layout.layout_ex_graph_item , listData);
        mAdapter.setSelection(position);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mListView.setSelection(position);
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.setSelection(-1);
            }
        });
    }

    /*************************** UI FUNCTION ******************************************/
    /**
     * initial for View UI
     * @param rootView
     */
    private void initUI(View rootView) {
        //Init Ui
        calendarView = (CalendarView) rootView.findViewById(R.id.calendar_picktime);
        calendarView.setVisibility(View.GONE);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                cal.set(year,month,dayOfMonth);
                edtSearchText.setText(year + "-" + (month +1) + "-" + dayOfMonth);
            }
        });

        mAdapter = new NestGraphDataAdapter(getContext() , R.layout.layout_ex_graph_item , new ArrayList<NestGraphData>());
        //add ListView
        mListView = (ListView) rootView.findViewById(R.id.list_fb_history);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NestGraphData data = mAdapter.getItem(position);
                String idUri  = data.getId();
                String url = "http://www.facebook.com/"+ idUri;

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        //set Data for CalendarView
        calendarView.setDate(cal.getTimeInMillis());

    }
}
