package com.github.phajduk.rxvalidator.validators;

import android.text.TextUtils;
import android.widget.EditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.Validator;
import rx.Observable;

public class NonEmptyValidator implements Validator<EditText> {
  private static final String DEFAULT_MESSAGE = "Cannot be empty";
  private String cannotBeEmptyMessage;

  public NonEmptyValidator() {
    cannotBeEmptyMessage = DEFAULT_MESSAGE;
  }

  public NonEmptyValidator(String cannotBeEmptyMessage) {
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
