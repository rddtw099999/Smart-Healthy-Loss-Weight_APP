package com.minkai.lossweight_app.ui.status;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.minkai.lossweight_app.MainActivity;
import com.minkai.lossweight_app.R;

import java.util.ArrayList;
import java.util.List;


public class StatusFragment extends Fragment {

    private StatusViewModel toolsViewModel;
    PieChart pieChart;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(StatusViewModel.class);
        View root = inflater.inflate(R.layout.fragment_status, container, false);

        final TextView ConnDeivces=root.findViewById(R.id.textView6);


        //ConnDeivces.setText(((MainActivity)getActivity()).connectName);

        pieChart=root.findViewById(R.id.pie_chart);
        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(30f,""));
        strings.add(new PieEntry(70f,"飽足感"));

        PieDataSet dataSet = new PieDataSet(strings,"");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.colorPrimary));
        colors.add(getResources().getColor(R.color.ChartMainColor));

        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.setHoleColor(Color.WHITE);
        pieChart.getDescription().setEnabled(false);
        pieChart.setTransparentCircleRadius(80f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setHoleRadius(80f);
        pieChart.setCenterTextColor(R.color.white);
        pieChart.setCenterText("70%");
        pieChart.setDrawEntryLabels(false);
        pieChart.setCenterTextSize(28);
        pieChart.getLegend().setEnabled(false);

        FloatingActionButton addDev =root.findViewById(R.id.floatingActionButton);

        addDev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_devices);
            }
        });
        return root;
    }


}