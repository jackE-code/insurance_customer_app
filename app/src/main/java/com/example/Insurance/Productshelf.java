package com.example.Insurance;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.NumberFormat;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;

public class Productshelf extends AppCompatActivity {

    private EditText editTextInterestAmount;
    private EditText editTextDuration;
    private GraphView graph;
    private LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productshelf);

        editTextInterestAmount = findViewById(R.id.editTextInterestAmount);
        editTextDuration = findViewById(R.id.editTextDuration);
        graph = findViewById(R.id.graph);

        // Initialize the graph series
        series = new LineGraphSeries<>();
        graph.addSeries(series);

        // Handle changes to interest amount and duration
        editTextInterestAmount.setOnEditorActionListener((v, actionId, event) -> {
            updateGraph();
            return false;
        });

        editTextDuration.setOnEditorActionListener((v, actionId, event) -> {
            updateGraph();
            return false;
        });

        // Initial graph update
        updateGraph();
    }

    private void updateGraph() {
        // Get interest amount and duration from inputs
        double interestAmount = Double.parseDouble(editTextInterestAmount.getText().toString());
        double duration = Double.parseDouble(editTextDuration.getText().toString());

        // Calculate and update the graph data points for compound interest
        series.resetData(generateData(interestAmount, duration));

        // Customize the graph labels using a StaticLabelsFormatter
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Label1", "Label2", "Label3", "Label4", "Label5"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
    }

    private DataPoint[] generateData(double principal, double years) {
        int dataPoints = (int) (years * 12); // Assume monthly data points
        DataPoint[] values = new DataPoint[dataPoints];

        double interestRate = 0.05; // Example interest rate (5% annually)
        for (int i = 0; i < dataPoints; i++) {
            double time = i / 12.0; // Convert months to years
            principal *= (1 + interestRate / 12); // Calculate compound interest
            values[i] = new DataPoint(time, principal);
        }

        return values;
    }
}
