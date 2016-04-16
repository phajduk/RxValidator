package com.github.phajduk.rxvalidator;

import android.widget.EditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import rx.Observable;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class RxAgeValidator implements Validator<EditText> {

  private static final String DEFAULT_MESSAGE = "At least 18y old";
  private static final int DEFAULT_AGE = 18;
  private final String message;
  private final SimpleDateFormat sdf;
  private final int age;

  public RxAgeValidator() {
    message = DEFAULT_MESSAGE;
    age = DEFAULT_AGE;
    sdf = new SimpleDateFormat();
  }

  public RxAgeValidator(String message, SimpleDateFormat sdf, int age) {
    this.message = message;
    this.sdf = sdf;
    this.age = age;
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    try {
      Date date = sdf.parse(text);
      if (ageIsValid(date)) {
        return Observable.just(RxValidationResult.createSuccess(item));
      }
      return Observable.just(RxValidationResult.createImproper(item, message));
    } catch (ParseException e) {
      return Observable.error(e);
    }
  }

  private int getDiffYears(Date first, Date last) {
    Calendar a = getCalendar(first);
    Calendar b = getCalendar(last);
    int diff = b.get(YEAR) - a.get(YEAR);
    if (a.get(MONTH) > b.get(MONTH) || (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(
        DATE))) {
      diff--;
    }
    return diff;
  }

  private Calendar getCalendar(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
  }

  private boolean ageIsValid(Date dateOfBirth) {
    return getDiffYears(dateOfBirth, new Date()) >= age;
  }
}
