package com.example.shopify.ui.announce;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.shopify.databinding.FragmentChartBinding;

import java.util.ArrayList;
import java.util.List;

public class ChartDialog extends DialogFragment {
    protected FragmentChartBinding binding;

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(LayoutInflater.from(requireContext()));
        APIlib.getInstance().setActiveAnyChartView(binding.myChart);
        binding.myChart.setProgressBar(binding.progressBar);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("2016", 26837));
        data.add(new ValueDataEntry("2017", 59463));
        data.add(new ValueDataEntry("2018", 72027));
        data.add(new ValueDataEntry("2019", 97847));
        data.add(new ValueDataEntry("2020", 75398));
        data.add(new ValueDataEntry("2021", 104967));

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}");
        cartesian.animation(true);
        cartesian.title("Yearly Transaction");
        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).labels().format("{%Value}");
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Year");
        cartesian.yAxis(0).title("Transaction");
        binding.myChart.setChart(cartesian);

        return  new AlertDialog.Builder(requireActivity()).setView(binding.getRoot()).create();
    }

    // jangan lupa set binding menjadi null apabila digunakan di fragment
    // agar tidak terjadi memory leak
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
