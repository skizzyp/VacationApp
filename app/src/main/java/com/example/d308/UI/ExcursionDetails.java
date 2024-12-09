

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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.d308.R;
import com.example.d308.dao.ExcursionDao;
import com.example.d308.database.Repository;
import com.example.d308.entity.Excursion;
import com.example.d308.entity.Vacation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class ExcursionDetails extends AppCompatActivity {
    String title;
    String end_date = "";
    String start_date = "";
    String date;
    int vacationID;
    int excursionID;
    EditText editTitle;
    EditText editDate;
    EditText editNote;
    Repository repository;
    DatePickerDialog.OnDateSetListener startDate;


    Excursion currentExcursion;
    final Calendar myCalendarStart = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursions);
        repository = new Repository(getApplication());
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        vacationID = getIntent().getIntExtra("vacationID", -1);
        editDate = findViewById(R.id.editDate2);
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editTitle = findViewById(R.id.editTitle2);
        editTitle.setText(title);
        editDate.setText(date);
        editNote = findViewById(R.id.editNote);
        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editDate.getText().toString();
                if (info.equals("")) info = "11/02/2024";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, startDate, myCalendarStart.get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ArrayList<Vacation> vacationArrayList = new ArrayList<>();
        vacationArrayList.addAll(repository.getAllVacations());
        ArrayList<Integer> vacationIdList = new ArrayList<>();
        for (Vacation vacation : vacationArrayList) {
            vacationIdList.add(vacation.getVacationID());
        }
        ArrayAdapter<Vacation> vacationIDAdapter = new ArrayAdapter<Vacation>(this, android.R.layout.simple_spinner_item, vacationArrayList);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(vacationIDAdapter);

    }

    private void updateLabelStart() {
        String dateFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveExcursion) {


            Date start = myCalendarStart.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
            List<Vacation> vacations = repository.getAllVacations();
            for (Vacation v : vacations) {
                if (v.getVacationID() == vacationID) {
                    start_date = v.getStartDate();
                    end_date = v.getEndDate();
                }
            }
            // start_date = getIntent().getStringExtra("start date");
            // end_date = getIntent().getStringExtra("end date");

            Date sDate = null;
            Date eDate = null;

            vacationID = getIntent().getIntExtra("vacationID", -1);
            excursionID = getIntent().getIntExtra("excursionID", -1);
            try {
                eDate = sdf.parse(end_date);
                sDate = sdf.parse(start_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if ((start.before(sDate)) || (start.after(eDate))) {
                Toast.makeText(ExcursionDetails.this, "Excursion must be scheduled during vacation!", Toast.LENGTH_LONG).show();
            } else {
                Excursion excursion;
                if (excursionID == -1) {
                    if (repository.getAllExcursions().size() == 0) excursionID = 1;
                    else
                        excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
                    excursion = new Excursion(excursionID, editTitle.getText().toString(), editDate.getText().toString(), vacationID);
                    repository.insert(excursion);
                    this.finish();
                } else {
                    excursion = new Excursion(excursionID, editTitle.getText().toString(), editDate.getText().toString(), vacationID);
                    repository.update(excursion);
                    this.finish();
                }
            }

        }
        if (item.getItemId() == R.id.share) {
            String textToShare = editNote.getText().toString();
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare + " " + title + " " + date);
            shareIntent.putExtra(Intent.EXTRA_TITLE, "Check out my excursion!");
            shareIntent.setType("text/plain");
            Intent sendIntent = Intent.createChooser(shareIntent, null);
            startActivity(sendIntent);
            return true;
        }
        if (item.getItemId() == R.id.notify) {
            String dateOfEvent = editDate.getText().toString();
            String dateFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
            Date date = null;
            try {
                date = sdf.parse(dateOfEvent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Long notificationTrigger = date.getTime();
                Intent intent = new Intent(ExcursionDetails.this, ExcursionReceiver.class);
                intent.putExtra("key", "Your excursion is scheduled for today!");
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTrigger, sender);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if (item.getItemId() == R.id.deleteExcursion) {
            currentExcursion = repository.getAllExcursions().get(excursionID);
            repository.delete(currentExcursion);

            Toast.makeText(ExcursionDetails.this, "Excursion Deleted", Toast.LENGTH_LONG).show();


        }
        return super.onOptionsItemSelected(item);

    }

}