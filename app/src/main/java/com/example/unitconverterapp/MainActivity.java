package com.example.unitconverterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner fromSpinner, toSpinner;
    EditText inputValue;
    Button convertButton;
    TextView resultView;

    String[] units = {
            "inch", "cm", "foot", "yard", "mile", "km",
            "pound", "kg", "ounce", "g", "ton",
            "Celsius", "Fahrenheit", "Kelvin"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        inputValue = findViewById(R.id.inputValue);
        convertButton = findViewById(R.id.convertButton);
        resultView = findViewById(R.id.resultView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                units
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndConvert();
            }
        });
    }

    private void validateAndConvert() {
        String fromUnit = fromSpinner.getSelectedItem().toString();
        String toUnit = toSpinner.getSelectedItem().toString();
        String inputStr = inputValue.getText().toString();

        if (inputStr.isEmpty()) {
            resultView.setText("Please enter a value!");
            return;
        }

        double inputNumber;
        try {
            inputNumber = Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            resultView.setText("Invalid number format!");
            return;
        }

        if (fromUnit.equals(toUnit)) {
            resultView.setText("Source and destination units must be different!");
            return;
        }

        if ((isLength(fromUnit) && !isLength(toUnit)) ||
                (isWeight(fromUnit) && !isWeight(toUnit)) ||
                (isTemperature(fromUnit) && !isTemperature(toUnit))) {
            resultView.setText("Cannot convert between different categories (e.g., weight to length)");
            return;
        }

        double result = performConversion(fromUnit, toUnit, inputNumber);
        resultView.setText(String.format("%.2f %s", result, toUnit));
    }

    private double performConversion(String from, String to, double value) {
        switch (from + "_to_" + to) {
            // Length
            case "inch_to_cm": return value * 2.54;
            case "cm_to_inch": return value / 2.54;
            case "foot_to_cm": return value * 30.48;
            case "cm_to_foot": return value / 30.48;
            case "yard_to_cm": return value * 91.44;
            case "cm_to_yard": return value / 91.44;
            case "mile_to_km": return value * 1.60934;
            case "km_to_mile": return value / 1.60934;

            // Weight
            case "pound_to_kg": return value * 0.453592;
            case "kg_to_pound": return value / 0.453592;
            case "ounce_to_g": return value * 28.3495;
            case "g_to_ounce": return value / 28.3495;
            case "ton_to_kg": return value * 907.185;
            case "kg_to_ton": return value / 907.185;
            case "g_to_kg": return value / 1000;
            case "kg_to_g": return value * 1000;

            // Temperature
            case "Celsius_to_Fahrenheit": return (value * 1.8) + 32;
            case "Fahrenheit_to_Celsius": return (value - 32) / 1.8;
            case "Celsius_to_Kelvin": return value + 273.15;
            case "Kelvin_to_Celsius": return value - 273.15;

            default:
                Toast.makeText(this, "Unsupported conversion!", Toast.LENGTH_SHORT).show();
                return 0;
        }
    }

    private boolean isLength(String unit) {
        return unit.equals("inch") || unit.equals("cm") || unit.equals("foot")
                || unit.equals("yard") || unit.equals("mile") || unit.equals("km");
    }

    private boolean isWeight(String unit) {
        return unit.equals("pound") || unit.equals("kg") || unit.equals("ounce")
                || unit.equals("g") || unit.equals("ton");
    }

    private boolean isTemperature(String unit) {
        return unit.equals("Celsius") || unit.equals("Fahrenheit") || unit.equals("Kelvin");
    }
}
