package com.github.phajduk.rxvalidator;

import android.text.TextUtils;
import android.widget.EditText;
import rx.Observable;

public class RxDigitValidator implements Validator<EditText> {
  private static final String DEFAULT_MESSAGE = "Digits only";
  private String digitOnlyMessage;

  public RxDigitValidator() {
    digitOnlyMessage = DEFAULT_MESSAGE;
  }

  public RxDigitValidator(String digitOnlyMessage) {
    this.digitOnlyMessage = digitOnlyMessage;
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    if (TextUtils.isDigitsOnly(text)) {
      return Observable.just(RxValidationResult.createSuccess(item));
    }
    return Observable.just(RxValidationResult.createImproper(item, digitOnlyMessage));
  }
}
