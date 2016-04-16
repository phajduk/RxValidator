package com.github.phajduk.rxvalidator;

import android.widget.TextView;

public class RxValidationResult<T extends TextView> {

  private T item;
  private boolean isProper;
  private String message;

  private RxValidationResult(T item, boolean isProper, String message) {
    this.item = item;
    this.isProper = isProper;
    this.message = message;
  }

  public static <T extends TextView> RxValidationResult<T> createImproper(T item, String message) {
    return new RxValidationResult<>(item, false, message);
  }

  public static <T extends TextView> RxValidationResult<T> createSuccess(T item) {
    return new RxValidationResult<>(item, true, "");
  }

  public String getValidatedText() {
    return item.getText().toString();
  }

  public T getItem() {
    return item;
  }

  public void setItem(T item) {
    this.item = item;
  }

  public boolean isProper() {
    return isProper;
  }

  public String getMessage() {
    return message;
  }

  @Override public String toString() {
    return "RxValidationResult{" +
        "itemValue=" + item.getText().toString() +
        ", isProper=" + isProper +
        ", message='" + message + '\'' +
        '}';
  }
}
