package com.github.phajduk.rxvalidator.validators;

import java.util.regex.Pattern;

public class EmailValidator extends PatternValidator {

  private static final String DEFAULT_MESSAGE = "Invalid email";

  public EmailValidator() {
    super(DEFAULT_MESSAGE, android.util.Patterns.EMAIL_ADDRESS);
  }

  public EmailValidator(String invalidEmailMessage) {
    super(invalidEmailMessage, android.util.Patterns.EMAIL_ADDRESS);
  }

  public EmailValidator(String invalidEmailMessage, Pattern pattern) {
    super(invalidEmailMessage, pattern);
  }
}
