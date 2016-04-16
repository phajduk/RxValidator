package com.github.phajduk.rxvalidator;

import android.text.TextUtils;
import android.widget.EditText;
import rx.Observable;

public class RxNonEmptyValidator implements Validator<android.widget.EditText> {
  private static final String DEFAULT_MESSAGE = "Cannot be empty";
  private String cannotBeEmptyMessage;

  public RxNonEmptyValidator() {
    cannotBeEmptyMessage = DEFAULT_MESSAGE;
  }

  public RxNonEmptyValidator(String cannotBeEmptyMessage) {
    this.cannotBeEmptyMessage = cannotBeEmptyMessage;
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    if (isEmpty(text)) {
      return Observable.just(RxValidationResult.createImproper(item, cannotBeEmptyMessage));
    }
    return Observable.just(RxValidationResult.createSuccess(item));
  }

  private boolean isEmpty(String value) {
    return TextUtils.isEmpty(value) || value.trim().length() == 0;
  }
}
