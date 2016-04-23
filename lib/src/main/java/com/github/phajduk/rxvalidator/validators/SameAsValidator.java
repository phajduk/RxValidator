package com.github.phajduk.rxvalidator.validators;

import android.widget.EditText;
import android.widget.TextView;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.Validator;
import rx.Observable;

public class SameAsValidator implements Validator<EditText> {
  public static final String DEFAULT_MESSAGE = "Must be the same";
  private String sameAsMessage;
  private TextView anotherTextView;

  public SameAsValidator() {
    sameAsMessage = DEFAULT_MESSAGE;
  }

  public SameAsValidator(TextView anotherTextView, String message) {
    this.anotherTextView = anotherTextView;
    this.sameAsMessage = message;
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    if (anotherTextView.getText().toString().equals(text)) {
      return Observable.just(RxValidationResult.createSuccess(item));
    }

    return Observable.just(RxValidationResult.createImproper(item, sameAsMessage));
  }
}
