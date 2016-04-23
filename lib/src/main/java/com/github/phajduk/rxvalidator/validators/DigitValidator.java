package com.github.phajduk.rxvalidator.validators;

import android.text.TextUtils;
import android.widget.EditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.Validator;
import rx.Observable;

public class DigitValidator implements Validator<EditText> {
  private static final String DEFAULT_MESSAGE = "Digits only";
  private String digitOnlyMessage;

  public DigitValidator() {
    digitOnlyMessage = DEFAULT_MESSAGE;
  }

  public DigitValidator(String digitOnlyMessage) {
    this.digitOnlyMessage = digitOnlyMessage;
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    if (TextUtils.isDigitsOnly(text)) {
      return Observable.just(RxValidationResult.createSuccess(item));
    }
    return Observable.just(RxValidationResult.createImproper(item, digitOnlyMessage));
  }
}
