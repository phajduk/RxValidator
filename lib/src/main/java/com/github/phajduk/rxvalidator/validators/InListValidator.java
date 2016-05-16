package com.github.phajduk.rxvalidator.validators;

import android.widget.EditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.Validator;
import java.util.List;
import rx.Observable;

public class InListValidator implements Validator<EditText> {
  private final String errorMessage;
  private final List<String> properValues;

  public InListValidator(String errorMessage, List<String> properValues) {
    this.errorMessage = errorMessage;
    this.properValues = properValues;
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    if (properValues != null && properValues.contains(text)) {
      return Observable.just(RxValidationResult.createSuccess(item));
    }

    return Observable.just(RxValidationResult.createImproper(item, errorMessage));
  }
}
