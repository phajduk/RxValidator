package com.github.phajduk.rxvalidator.validators;

import android.text.TextUtils;
import android.widget.EditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.Validator;
import java.util.regex.Pattern;
import rx.Observable;

public class PatternFindValidator implements Validator<EditText> {

  private static final String DEFAULT_MESSAGE = "Invalid value";
  private String invalidValueMessage;
  private Pattern pattern;

  public PatternFindValidator() {
    this.invalidValueMessage = DEFAULT_MESSAGE;
    this.pattern = android.util.Patterns.EMAIL_ADDRESS;
  }

  public PatternFindValidator(String invalidValueMessage) {
    this.invalidValueMessage = invalidValueMessage;
    this.pattern = android.util.Patterns.EMAIL_ADDRESS;
  }

  public PatternFindValidator(String invalidValueMessage, Pattern pattern) {
    this.invalidValueMessage = invalidValueMessage;
    this.pattern = pattern;
  }

  public PatternFindValidator(String invalidValueMessage, String pattern) {
    this.invalidValueMessage = invalidValueMessage;
    this.pattern = Pattern.compile(pattern);
  }

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    return Observable.just(validatePattern(item, text));
  }

  private RxValidationResult<EditText> validatePattern(EditText view, String value) {
    if (nonEmptyAndFoundPattern(value)) {
      return RxValidationResult.createSuccess(view);
    }
    return RxValidationResult.createImproper(view, invalidValueMessage);
  }

  private boolean nonEmptyAndFoundPattern(String value) {
    return !TextUtils.isEmpty(value) && pattern.matcher(value).find();
  }
}
