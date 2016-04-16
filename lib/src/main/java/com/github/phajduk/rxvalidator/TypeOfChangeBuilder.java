package com.github.phajduk.rxvalidator;

public interface TypeOfChangeBuilder {
  RxValidator onFocusChanged();

  RxValidator onValueChanged();

  RxValidator onSubscribe();
}
