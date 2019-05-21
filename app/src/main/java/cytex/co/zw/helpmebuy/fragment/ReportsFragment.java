package cytex.co.zw.helpmebuy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;

import cytex.co.zw.helpmebuy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment {


    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reports, container, false);



        final AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);

        final Pie pie = AnyChart.pie();

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Apples", 6371664));
        data.add(new ValueDataEntry("Pears", 789622));
        data.add(new ValueDataEntry("Bananas", 7216301));
        data.add(new ValueDataEntry("Grapes", 1486621));
        data.add(new ValueDataEntry("Oranges", 1200000));

        pie.data(data);

        pie.title("Fruits imported in 2015 (in kg)");

        anyChartView.setChart(pie);

        final AnyChartView anyChartView1 = view.findViewById(R.id.any_chart_view1);
        APIlib.getInstance().setActiveAnyChartView(anyChartView1);

        final Pie pie1 = AnyChart.pie();

        pie1.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data1 = new ArrayList<>();
        data1.add(new ValueDataEntry("Apples", 6371664));
        data1.add(new ValueDataEntry("Pears", 789622));
        data1.add(new ValueDataEntry("Bananas", 7216301));
        data1.add(new ValueDataEntry("Grapes", 1486621));
        data1.add(new ValueDataEntry("Oranges", 1200000));

        pie1.data(data1);

        pie1.title("Fruits imported in 2015 (in kg)");

        anyChartView1.setChart(pie1);

        AnyChartView anyChartView2 = view.findViewById(R.id.any_chart_view2);
        APIlib.getInstance().setActiveAnyChartView(anyChartView2);

        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data2 = new ArrayList<>();
        data2.add(new ValueDataEntry("sweets", 10));
        data2.add(new ValueDataEntry("drinks", 8));
        data2.add(new ValueDataEntry("braids", 7));
        data2.add(new ValueDataEntry("toys", 9));
        data2.add(new ValueDataEntry("royco", 14));
        data2.add(new ValueDataEntry("beer", 7));
        data2.add(new ValueDataEntry("clothes", 22));

        Column column = cartesian.column(data2);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");

        cartesian.title("Top Ranking According To Search");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Product");
        cartesian.yAxis(0).title("Number Of Times");

        anyChartView2.setChart(cartesian);

//        final int delayMillis = 500;
//        handler = new Handler();
//        runnable = new Runnable() {
//            public void run() {
//                APIlib.getInstance().setActiveAnyChartView(anyChartView);
//                List<DataEntry> data = new ArrayList<>();
//                data.add(new ValueDataEntry("Apples", new Random().nextDouble() * 140d));
//                data.add(new ValueDataEntry("Pears", new Random().nextDouble() * 140d));
//                pie.data(data);
//                handler.postDelayed(this, delayMillis);
//
//                APIlib.getInstance().setActiveAnyChartView(anyChartView1);
//                List<DataEntry> data1 = new ArrayList<>();
//                data1.add(new ValueDataEntry("Apples", new Random().nextDouble() * 140d));
//                data1.add(new ValueDataEntry("Pears", new Random().nextDouble() * 140d));
//                pie1.data(data1);
//                handler.postDelayed(this, delayMillis);
//            }
//        };
//        handler.postDelayed(runnable, delayMillis);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        handler.removeCallbacks(runnable);
    }

}
