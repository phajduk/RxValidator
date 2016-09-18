package com.github.phajduk.rxvalidator.validators;

import android.text.TextUtils;
import android.widget.EditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.Validator;
import rx.Observable;

public class MaxLengthValidator implements Validator<EditText> {
  private static final String DEFAULT_MESSAGE = "Bad length";

  private String lengthMessage;
  private int properLength;

  public MaxLengthValidator(int properLength) {
    this(properLength, DEFAULT_MESSAGE);
  }

  public MaxLengthValidator(int properLength, String lengthMessage) {
    this.lengthMessage = lengthMessage;
    this.properLength = properLength;
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    if (TextUtils.isEmpty(text)) {
      return Observable.just(RxValidationResult.createImproper(item, lengthMessage));
    }

    if (text.trim().length() <= properLength) {
      return Observable.just(RxValidationResult.createSuccess(item));
    }

    return Observable.just(RxValidationResult.createImproper(item, lengthMessage));
  }
}
