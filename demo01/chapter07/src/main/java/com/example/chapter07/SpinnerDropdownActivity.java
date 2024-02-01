package com.example.chapter07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

public class SpinnerDropdownActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {
    private Spinner sp_dropdown;

    private final static String[] starArray = {"Mars","Jupyter","Earth"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_dropdown);

        // dropdown
        sp_dropdown = findViewById(R.id.sp_dropdown);
        ArrayAdapter<String> startAdapter = new ArrayAdapter<>(this, R.layout.item_select, starArray);
        sp_dropdown.setAdapter(startAdapter);
        sp_dropdown.setSelection(0);

        sp_dropdown.setOnItemSelectedListener(this);

        // checkbox
        CheckBox ck_system = findViewById(R.id.ck_system);
        ck_system.setOnCheckedChangeListener(this);

        // viewpager
        ViewPager vp_content = findViewById(R.id.vp_content);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println(starArray[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String desc = String.format("You %s this CheckBox", isChecked ? "Checked":"Unchecked");
        buttonView.setText(desc);
    }
}