package com.github.phajduk.rxvalidator;

import android.text.TextUtils;
import android.widget.EditText;
import rx.Observable;

public class RxLengthValidator implements Validator<EditText> {
  private static final String DEFAULT_MESSAGE = "Bad length";
  private static final int DEFAULT_PROPER_LENGTH = 5;
  private String lengthMessage;
  private int properLength;

  public RxLengthValidator() {
    lengthMessage = DEFAULT_MESSAGE;
    properLength = DEFAULT_PROPER_LENGTH;
  }

  public RxLengthValidator(String lengthMessage, int properLength) {
    this.lengthMessage = lengthMessage;
    this.properLength = properLength;
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    if (TextUtils.isEmpty(text)) {
      return Observable.just(RxValidationResult.createImproper(item, lengthMessage));
    }

    if (text.trim().length() == properLength) {
      return Observable.just(RxValidationResult.createSuccess(item));
    }

    return Observable.just(RxValidationResult.createImproper(item, lengthMessage));
  }
}
