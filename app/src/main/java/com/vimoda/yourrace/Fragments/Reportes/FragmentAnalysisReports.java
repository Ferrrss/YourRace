package com.vimoda.yourrace.Fragments.Reportes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vimoda.yourrace.R;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentAnalysisReports extends Fragment {

    private PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysis_reports, container, false);
        pieChart = view.findViewById(R.id.pieChart);

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(25f, "ZAPATILLAS"));
        entries.add(new PieEntry(42f, "ZAPATOS ELEGANTES"));
        entries.add(new PieEntry(18f, "ZAPATILLAS DEPORTIVAS"));
        entries.add(new PieEntry(15f, "TACONES"));

        PieDataSet dataSet = new PieDataSet(entries, "Ventas");
        dataSet.setColors(new int[]{Color.CYAN, Color.LTGRAY, Color.DKGRAY, Color.MAGENTA});
        dataSet.setValueTextColor(Color.GRAY);
        dataSet.setValueTextSize(20f);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleRadius(28f);
        pieChart.setTransparentCircleRadius(28f);

        Button savePdfButton = view.findViewById(R.id.savePdfButton);
        savePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePdf();
            }
        });
        return view;
    }

    private void generatePdf() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pieChart.getWidth(), pieChart.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        pieChart.draw(canvas);

        document.finishPage(page);

        // Guardar el PDF en el almacenamiento externo
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "grafico_pastel.pdf");
        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(getActivity(), "PDF guardado correctamente en Descargas", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error al guardar el PDF", Toast.LENGTH_SHORT).show();
        }
        document.close();
    }
}
