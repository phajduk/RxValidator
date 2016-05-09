package com.github.phajduk.rxvalidator.sample;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.RxValidator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "RxValidator";
  private static final String dateFormat = "dd-MM-yyyy";
  private static final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    EditText email = (EditText) findViewById(R.id.etEmail);
    EditText password = (EditText) findViewById(R.id.etPassword);
    EditText confirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
    EditText birthday = (EditText) findViewById(R.id.etBirthday);
    setDatePickerListener(birthday);

    RxValidator.createFor(email)
        .nonEmpty()
        .email()
        .with(new CustomEmailDomainValidator())
        .with(new ExternalApiEmailValidator())
        .onValueChanged()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<RxValidationResult<EditText>>() {
          @Override public void call(RxValidationResult<EditText> result) {
            result.getItem().setError(result.isProper() ? null : result.getMessage());
            Log.i(TAG, "Validation result " + result.toString());
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Log.e(TAG, "Validation error", throwable);
          }
        });

    RxValidator.createFor(password)
        .nonEmpty()
        .length("Min length is 5", 5)
        .onFocusChanged()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<RxValidationResult<EditText>>() {
          @Override public void call(RxValidationResult<EditText> result) {
            result.getItem().setError(result.isProper() ? null : result.getMessage());
            Log.i(TAG, "Validation result " + result.toString());
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Log.e(TAG, "Validation error", throwable);
          }
        });

    RxValidator.createFor(confirmPassword)
        .sameAs(password, "Password does not match")
        .onFocusChanged()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<RxValidationResult<EditText>>() {
          @Override public void call(RxValidationResult<EditText> result) {
            result.getItem().setError(result.isProper() ? null : result.getMessage());
            Log.i(TAG, "Validation result " + result.toString());
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Log.e(TAG, "Validation error", throwable);
          }
        });

    RxValidator.createFor(birthday)
        .age("You have to be 18y old", 18, sdf)
        .onValueChanged()
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<RxValidationResult<EditText>>() {
          @Override public void call(RxValidationResult<EditText> result) {
            result.getItem().setError(result.isProper() ? null : result.getMessage());
            Log.i(TAG, "Validation result " + result.toString());
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Log.e(TAG, "Validation error", throwable);
          }
        });
  }

  private void setDatePickerListener(final EditText birthday) {
    birthday.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showDatePicker((EditText) v);
      }
    });
  }

  private void showDatePicker(final EditText birthday) {
    DatePickerDialog dpd =
        new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Date selectedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
            birthday.setText(sdf.format(selectedDate));
          }
        }, 2016, 0, 1);
    dpd.show();
  }
}
