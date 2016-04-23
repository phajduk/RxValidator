package com.github.phajduk.rxvalidator.validators;

import android.text.TextUtils;
import android.widget.EditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.Validator;
import rx.Observable;

public class EmailValidator implements Validator<EditText> {

  private static final String DEFAULT_MESSAGE = "Invalid email";
  private String invalidEmailMessage;

  public EmailValidator() {
    invalidEmailMessage = DEFAULT_MESSAGE;
  }

  public EmailValidator(String invalidEmailMessage) {
    this.invalidEmailMessage = invalidEmailMessage;
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    return Observable.just(validateEmailText(item, text));
  }

  private RxValidationResult<EditText> validateEmailText(EditText view, String value) {
    if (isValidEmail(value)) {
      return RxValidationResult.createSuccess(view);
    }
    return RxValidationResult.createImproper(view, invalidEmailMessage);
  }

  private boolean isValidEmail(String value) {
    return !TextUtils.isEmpty(value) && android.util.Patterns.EMAIL_ADDRESS.matcher(value)
        .matches();
  }
}
