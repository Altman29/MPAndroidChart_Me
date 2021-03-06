
package com.xxmassdeveloper.mpchartexample;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class CombinedChartActivity extends AppCompatActivity {

    /**
     *
     */

    private CombinedChart chart;
    private final int count = 7;//7 or 14
    private boolean isShowValues = false;//Bar,Line是否显示数值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_combined);

        setTitle("CombinedChartActivity");

        chart = findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(false);
        chart.setExtraBottomOffset(14f);

        // draw bars behind lines
        chart.setDrawOrder(new DrawOrder[]{
                DrawOrder.BAR, DrawOrder.LINE
        });

        //设置图例相关
        Legend l = chart.getLegend();
        l.setEnabled(false);
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setForm(Legend.LegendForm.CIRCLE);//统一设置图例样式
        l.setDrawInside(false);
        l.setTextSize(12f);
        l.setFormToTextSpace(8f);//设置图例(形状)和标签的间距
        l.setXEntrySpace(8f);//X轴上图例间距
        l.setYEntrySpace(8f);//Y轴上图例间距
        //以百分比为单位，将整个图表视图相对整个父类View设置百分比。默认0.95f(95%).超过则换行。(但是换行后因为设置了xEntrySpace不再居左)
        l.setMaxSizePercent(0.95f);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setGranularity(1);
        rightAxis.setTextColor(Color.parseColor("#FFC4C4CC"));
        rightAxis.setTextSize(12f);
        rightAxis.setAxisMaximum(5f);//基本坐标0-5，真实值在setValuesFormatter中设置
        rightAxis.setYOffset(-10f);//right Y values向上偏移
        rightAxis.setDrawAxisLine(false);//只画值，不画线
        //右侧Y轴显示
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value * 20 + "%";
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setGranularity(1f);
        leftAxis.setTextColor(Color.parseColor("#FFC4C4CC"));
        leftAxis.setTextSize(12f);
        leftAxis.setAxisMaximum(5f);//基本坐标0-5，真实值在setValuesFormatter中设置
        leftAxis.setGridLineWidth(0.5f);
        leftAxis.setYOffset(-10f);//left Y values向上偏移
        leftAxis.setGridDashedLine(new DashPathEffect(new float[]{8f, 8f}, 8f));
        leftAxis.setGridColor(Color.parseColor("#E1E1E1"));
        leftAxis.setDrawAxisLine(false);//只画值，不画线
        //左侧Y轴显示
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value * 2 + "";
            }
        });

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);//X轴设置在底部
        xAxis.setDrawGridLines(false);//只画值，不画线
        xAxis.setAxisMinimum(0f);//最小0
        xAxis.setGranularity(1f);//粒度1
        xAxis.setTextColor(Color.parseColor("#FFC4C4CC"));
        xAxis.setTextSize(12f);
        xAxis.setCenterAxisLabels(true);//针对每个X坐标，X的坐标显示居中
        //底部X轴显示
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "";
            }
        });

        CombinedData data = new CombinedData();

        data.setData(generateLineData());
        data.setData(generateBarData());
        // data.setData(generateBubbleData());
        // data.setData(generateScatterData());
        // data.setData(generateCandleData());
        // data.setValueTypeface(tfLight);//字体

        xAxis.setAxisMaximum(data.getXMax() + 0.5f);//just show nice

        chart.setData(data);
        chart.invalidate();
    }

    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }

    private LineData generateLineData() {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<>();

        //需要数据转换，外部传入数据，并处理
        for (int index = 0; index < count; index++)
            entries.add(new Entry(index + 0.5f, getRandom(5, 0)));

        LineDataSet set = new LineDataSet(entries, "睡眠效率");
        set.setColor(Color.parseColor("#FFFFC166"));
        set.setLineWidth(3.5f);
        set.setDrawCircles(false);
        // set.setCircleColor(Color.rgb(240, 238, 70));
        // set.setCircleRadius(0f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);//三次贝塞尔曲线
        set.setDrawValues(isShowValues);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.parseColor("#FFC4C4CC"));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();

        //需要数据转换，外部传入数据，并处理
        for (int index = 0; index < count; index++) {
            entries1.add(new BarEntry(0, getRandom(5, 0)));

            // stacked
            entries2.add(new BarEntry(0, new float[]{getRandom(2, 0), getRandom(3, 0)}));
        }

        BarDataSet set1 = new BarDataSet(entries1, "约定时长");
        set1.setColor(Color.parseColor("#FF72A4E1"));
        set1.setDrawValues(isShowValues);
        set1.setValueTextColor(Color.parseColor("#FFC4C4CC"));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(entries2, "");
        set2.setStackLabels(new String[]{"睡眠时长", "卧床时长"});
        set2.setColors(Color.parseColor("#FF7B72E1"), Color.parseColor("#FFE4E2FB"));
        set2.setDrawValues(isShowValues);
        set2.setValueTextColor(Color.parseColor("#FFC4C4CC"));
        set2.setValueTextSize(10f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.3f;
        float barSpace = 0.1f; // x2 dataset
        float barWidth = 0.25f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set2, set1);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }

    private ScatterData generateScatterData() {

        ScatterData d = new ScatterData();

        ArrayList<Entry> entries = new ArrayList<>();

        for (float index = 0; index < count; index += 0.5f)
            entries.add(new Entry(index + 0.25f, getRandom(10, 55)));

        ScatterDataSet set = new ScatterDataSet(entries, "Scatter DataSet");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setScatterShapeSize(7.5f);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        d.addDataSet(set);

        return d;
    }

    private CandleData generateCandleData() {

        CandleData d = new CandleData();

        ArrayList<CandleEntry> entries = new ArrayList<>();

        for (int index = 0; index < count; index += 2)
            entries.add(new CandleEntry(index + 1f, 90, 70, 85, 75f));

        CandleDataSet set = new CandleDataSet(entries, "Candle DataSet");
        set.setDecreasingColor(Color.rgb(142, 150, 175));
        set.setShadowColor(Color.DKGRAY);
        set.setBarSpace(0.3f);
        set.setValueTextSize(10f);
        set.setDrawValues(false);
        d.addDataSet(set);

        return d;
    }

    private BubbleData generateBubbleData() {

        BubbleData bd = new BubbleData();

        ArrayList<BubbleEntry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++) {
            float y = getRandom(10, 105);
            float size = getRandom(100, 105);
            entries.add(new BubbleEntry(index + 0.5f, y, size));
        }

        BubbleDataSet set = new BubbleDataSet(entries, "Bubble DataSet");
        set.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.WHITE);
        set.setHighlightCircleWidth(1.5f);
        set.setDrawValues(true);
        bd.addDataSet(set);

        return bd;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.combined, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewGithub: {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/CombinedChartActivity.java"));
                startActivity(i);
                break;
            }
            case R.id.actionToggleLineValues: {
                for (IDataSet set : chart.getData().getDataSets()) {
                    if (set instanceof LineDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
            case R.id.actionToggleBarValues: {
                for (IDataSet set : chart.getData().getDataSets()) {
                    if (set instanceof BarDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                chart.invalidate();
                break;
            }
            case R.id.actionRemoveDataSet: {
                int rnd = (int) getRandom(chart.getData().getDataSetCount(), 0);
                chart.getData().removeDataSet(chart.getData().getDataSetByIndex(rnd));
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
                chart.invalidate();
                break;
            }
        }
        return true;
    }

}
