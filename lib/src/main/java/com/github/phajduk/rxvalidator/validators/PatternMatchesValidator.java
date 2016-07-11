package com.github.phajduk.rxvalidator.validators;

import android.text.TextUtils;
import android.widget.EditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.Validator;
import java.util.regex.Pattern;
import rx.Observable;

public class PatternMatchesValidator implements Validator<EditText> {

  private static final String DEFAULT_MESSAGE = "Invalid value";
  private String invalidValueMessage;
  private Pattern pattern;

  public PatternMatchesValidator() {
    this.invalidValueMessage = DEFAULT_MESSAGE;
    this.pattern = android.util.Patterns.EMAIL_ADDRESS;
  }

  public PatternMatchesValidator(String invalidValueMessage) {
    this.invalidValueMessage = invalidValueMessage;
    this.pattern = android.util.Patterns.EMAIL_ADDRESS;
  }

  public PatternMatchesValidator(String invalidValueMessage, Pattern pattern) {
    this.invalidValueMessage = invalidValueMessage;
    this.pattern = pattern;
  }

  public PatternMatchesValidator(String invalidValueMessage, String pattern) {
    this.invalidValueMessage = invalidValueMessage;
    this.pattern = Pattern.compile(pattern);
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    return Observable.just(validatePattern(item, text));
  }

  private RxValidationResult<EditText> validatePattern(EditText view, String value) {
    if (nonEmptyAndMatchPattern(value)) {
      return RxValidationResult.createSuccess(view);
    }
    return RxValidationResult.createImproper(view, invalidValueMessage);
  }

  private boolean nonEmptyAndMatchPattern(String value) {
    return !TextUtils.isEmpty(value) && pattern.matcher(value).matches();
  }
}
