package com.github.phajduk.rxvalidator.validators;

import java.util.regex.Pattern;

public class Ip4Validator extends PatternValidator {

  private static final String DEFAULT_MESSAGE = "Invalid IP";

  public Ip4Validator() {
    super(DEFAULT_MESSAGE, android.util.Patterns.IP_ADDRESS);
  }

  public Ip4Validator(String invalidIpMessage) {
    super(invalidIpMessage, android.util.Patterns.IP_ADDRESS);
  }

  public Ip4Validator(String invalidIpMessage, Pattern pattern) {
    super(invalidIpMessage, pattern);
  }
}
