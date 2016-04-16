package com.github.phajduk.rxvalidator;

import android.widget.TextView;
import rx.Observable;

public interface Validator<T extends TextView> {
  Observable<RxValidationResult<T>> validate(String text, T item);
}
