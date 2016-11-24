package ca.uwaterloo.camevent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinnerBuilding;
    private EditText fromDateEtxt;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText keyWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        findViewsById();
        showSpinner();
        setDateTimeField();
    }

    private void findViewsById() {

        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.setFocusable(false);
        //findViewById(R.id.search_button);
        spinnerBuilding = (Spinner) findViewById(R.id.spinnerBuilding);
        keyWord = (EditText) findViewById(R.id.keyWord);
    }

    private void showSpinner(){
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterBuilding = ArrayAdapter.createFromResource(this,
                R.array.spinnerBuilding, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterBuilding.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerBuilding.setAdapter(adapterBuilding);

    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMinDate(new Date().getTime());
    }

    @Override
    public void onClick(View view) {
        fromDatePickerDialog.show();
    }

    public void search(View view){
        Intent intent = new Intent(this,MapsActivity.class);

        String buildingData = spinnerBuilding.getSelectedItem().toString();
        String fromDateData = fromDateEtxt.getText().toString();
        String keyWordData = keyWord.getText().toString();
        // this is a small bug here : must type keyword
        intent.putExtra("buildingData",buildingData);
        intent.putExtra("fromDateData",fromDateData);
        intent.putExtra("keyData",keyWordData);
        startActivity(intent);
    }

}