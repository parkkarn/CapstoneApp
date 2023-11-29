package com.example.greenday.ui.dashboard;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.greenday.R;
import com.example.greenday.databinding.FragmentTemperatureBinding;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentTemperatureBinding binding;

    private LineChart lineChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(requireActivity()).get(DashboardViewModel.class);

        binding = FragmentTemperatureBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String newText) {
                // onChanged 메서드에서 데이터 변경을 감지하고 이를 처리
                textView.setText(newText);
                Typeface customTypeface = ResourcesCompat.getFont(requireContext(), R.font.dnfbitbit2);
                List<String> tempList = dashboardViewModel.getTempArray().getValue();
                List<String> postTimeArray = dashboardViewModel.getPostTimeArray().getValue();
                //그래프 그리기
                try {
                    if (tempList != null && !tempList.isEmpty()) {
                        // humidityList가 비어있지 않은 경우에만 첫 번째 요소에 접근
                        ArrayList<Entry> entry_chart1 = new ArrayList<>();

                        lineChart = root.findViewById(R.id.chart);

                        LineData chartData = new LineData();

                        for(int i=postTimeArray.size()-5;i<postTimeArray.size();i++){
                            entry_chart1.add(new Entry(i+1, Float.parseFloat(tempList.get(i))));
                        }

                        LineDataSet lineDataSet1 = new LineDataSet(entry_chart1, "Humidity");

                        lineChart.setBackgroundColor(Color.argb(100,231,255,231));
                        lineChart.getXAxis().setDrawGridLines(false);
                        lineChart.getAxisLeft().setDrawGridLines(false);
                        lineChart.getXAxis().setDrawLabels(false);

                        //Description 표기 삭제
                        Description description = new Description();
                        description.setEnabled(false);
                        lineChart.setDescription(description);

                        //Legend 글꼴 변경
                        Legend legend = lineChart.getLegend();
                        legend.setTypeface(customTypeface);
                        legend.setTextColor(Color.GRAY);

                        //Y글꼴 변경
                        YAxis leftAxis = lineChart.getAxisLeft();
                        leftAxis.setTypeface(customTypeface);
                        leftAxis.setTextColor(Color.argb(100,156,202,156));
                        YAxis RightAxis = lineChart.getAxisRight();
                        RightAxis.setTypeface(customTypeface);
                        RightAxis.setTextColor(Color.argb(100,156,202,156));

                        lineDataSet1.setColor(Color.argb(100,70,140,70));
                        lineDataSet1.setCircleColor(Color.argb(100,39,58,39));
                        lineDataSet1.setValueTextSize(10f);
                        lineDataSet1.setLineWidth(4f);
                        lineDataSet1.setCircleRadius(7f);
                        lineDataSet1.setFillAlpha(30);
                        lineDataSet1.setValueTypeface(customTypeface);


                        chartData.addDataSet(lineDataSet1);

                        lineChart.setData(chartData);
                        lineChart.invalidate();
                        lineChart.setTouchEnabled(false);
                    }
                }catch (RuntimeException e){

                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}