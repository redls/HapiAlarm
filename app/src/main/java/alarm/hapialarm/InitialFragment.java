package alarm.hapialarm;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by laura on 28/01/17.
 */
public class InitialFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    TextView textAlarmPrompt;
    int dayOfAlarm = 0;
   // int hourOfAlarm = 0;
    int yearOfAlarm = 0;
    int minuteOfAlarm = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.initial_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
   //     hourOfAlarm = hour;
        minuteOfAlarm = minute;
        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView alarmPrompt = (TextView) getActivity().findViewById(R.id.alarm_prompt);
        //alarmPrompt.setText("Your chosen time is...\n\n");
//        Toast.makeText(getActivity().getBaseContext(),  "Hour : " + String.valueOf(hourOfDay)
//                + "\nMinute : " + String.valueOf(minute) + "\n", Toast.LENGTH_SHORT).show();

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if(calSet.compareTo(calNow) <= 0){
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
        }

        Toast.makeText(getActivity().getBaseContext(),  "Hour : " + String.valueOf(calSet.get(Calendar.HOUR))
                + "\nMinute : " + String.valueOf(calSet.get(Calendar.MINUTE)) + "\n", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(view.getContext(), SetAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), 1, intent, 0);

        AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);

        Log.v("hahaha", "hihihi");
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);
        Toast.makeText(view.getContext(), "Alarm Set.", Toast.LENGTH_LONG).show();

    }
}
