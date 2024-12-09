package com.example.d308.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308.R;
import com.example.d308.database.Repository;
import com.example.d308.entity.Excursion;
import com.example.d308.entity.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class VacationDetails extends AppCompatActivity {

    String title;
    String incExursions = " ";
    String place;
    String startDate1;
    String endDate1;

    int id;
    EditText editTitle;
    EditText editPlace;
    EditText editStart;
    EditText editEnd;
    Repository repository;
    Vacation currentVacation;
    DatePickerDialog.OnDateSetListener startDate;

    DatePickerDialog.OnDateSetListener endDate;

    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacations);


        Button btn2 = findViewById(R.id.button3);
        //retrieve stringExtra and set field items
        title=getIntent().getStringExtra("title");
        editTitle=findViewById(R.id.titleEdit);
        editTitle.setText(title);

        place=getIntent().getStringExtra("place");
        editPlace=findViewById(R.id.placeEdit);
        editPlace.setText(place);


        endDate1=getIntent().getStringExtra("end date");
        editEnd=findViewById(R.id.endEdit);
        editEnd.setText(endDate1);

        startDate1=getIntent().getStringExtra("start date");
        editStart=findViewById(R.id.startEdit);
        editStart.setText(startDate1);

        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);


        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelStart();
            }
        };
        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, month);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelEnd();
            }
        };
        editStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editStart.getText().toString();
                if(info.equals(""))info="11/2/2024";
                try{
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart.get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editEnd.getText().toString();
                if(info.equals(""))info="11/2/2024";
                try{
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd.get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH), myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        id=getIntent().getIntExtra("id", -1);

        RecyclerView recyclerView = findViewById(R.id.ExcursionRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> bookedExcursions = new ArrayList<>();
        for(Excursion e: repository.getAllExcursions()) {
            if(e.getVacationID() == id) bookedExcursions.add(e);
        }
        excursionAdapter.setExcursions(bookedExcursions);




        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", id);
                intent.putExtra("end date",endDate1);
                intent.putExtra("start date", startDate1);
                startActivity(intent);
            }
        });

    }

    public void updateLabelStart() {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editStart.setText(sdf.format(myCalendarStart.getTime()));

    }
    public void updateLabelEnd() {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editEnd.setText(sdf.format(myCalendarEnd.getTime()));

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {



            if(item.getItemId()==R.id.save) {
                String dateOfStart = editStart.getText().toString();
                String dateOfEnd = editEnd.getText().toString();
                String dateFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
                Date start = null;
                Date end = null;
                try{
                    start = sdf.parse(dateOfStart);
                    end = sdf.parse(dateOfEnd);
                }catch (ParseException e) {
                    e.printStackTrace();
                }

                if(start.after(end)){
                    Toast.makeText(VacationDetails.this,"Start date needs to be before end date",Toast.LENGTH_LONG).show();
                }else{

                    Vacation vacation;
                    if(id==-1){
                        if(repository.getAllVacations().size()==0)id=1;
                        else id=repository.getAllVacations().get(repository.getAllVacations().size() -1).getVacationID() + 1;
                        vacation = new Vacation(id, editTitle.getText().toString(), editPlace.getText().toString(), editStart.getText().toString(), editEnd.getText().toString());
                        repository.insert(vacation);
                        this.finish();
                    }
                    else {
                        vacation= new Vacation(id,editTitle.getText().toString(),editPlace.getText().toString(),editStart.getText().toString(),editEnd.getText().toString());
                        repository.update(vacation);
                        this.finish();
                    }
            }
        }
        if(item.getItemId()==R.id.delete) {
            for(Vacation vacation: repository.getAllVacations()){
                if(vacation.getVacationID()==id) currentVacation=vacation;
            }
            int numExcursions = 0;
            for(Excursion excursion : repository.getAllExcursions()){
                if(excursion.getVacationID() == id)++numExcursions;
            }
            if(numExcursions == 0){
                repository.delete(currentVacation);
                Toast.makeText(this, currentVacation.getTitle() + "has been removed", Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
            }
            else {
                Toast.makeText(VacationDetails.this, "Not allowed to remove a Vacation that includes excursions", Toast.LENGTH_LONG).show();
            }}
        if(item.getItemId() == R.id.notifyStart) {
            String dateOfEvent = editStart.getText().toString();
            String dateFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
            Date date = null;
            try{
                date = sdf.parse(dateOfEvent);
            }catch (ParseException e) {
                e.printStackTrace();
                }
            try{
                Long notificationTrigger = date.getTime();
                Intent intent = new Intent(VacationDetails.this, ExcursionReceiver.class);
                intent.putExtra("key", "Your vacation starts today!");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTrigger, sender);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
            }
        if(item.getItemId() == R.id.notifyEnd) {
            String dateOfEvent = editEnd.getText().toString();
            String dateFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
            Date date = null;
            try{
                date = sdf.parse(dateOfEvent);
            }catch (ParseException e) {
                e.printStackTrace();
            }
            try{
                Long notificationTrigger = date.getTime();
                Intent intent = new Intent(VacationDetails.this, ExcursionReceiver.class);
                intent.putExtra("key", "Your vacation ends today :(");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTrigger, sender);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
            }
        if(item.getItemId() == R.id.shareVacation) {

            List<Excursion> associatedExcursions = new ArrayList<>();
            for(Excursion e: repository.getAllExcursions()) {
                if(e.getVacationID() == id) {
                    incExursions = incExursions + e.getTitle() + " " + e.getDate() + "\n";
                }
            }

            String textToShare = title + " " + startDate1 + " - " + endDate1 + " in " + place + " " + incExursions;
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
            shareIntent.putExtra(Intent.EXTRA_TITLE, "Check out my Vacation!");
            shareIntent.setType("text/plain");
            Intent sendIntent = Intent.createChooser(shareIntent, null);
            startActivity(sendIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume(){
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.ExcursionRecyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> bookedExcursions = new ArrayList<>();
        for(Excursion e: repository.getAllExcursions()) {
            if(e.getVacationID() == id) bookedExcursions.add(e);
        }
        excursionAdapter.setExcursions(bookedExcursions);
    }
}