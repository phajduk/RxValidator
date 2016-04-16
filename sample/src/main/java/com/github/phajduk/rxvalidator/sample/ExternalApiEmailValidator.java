package com.github.phajduk.rxvalidator.sample;

import android.util.Log;
import android.widget.EditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.Validator;
import java.util.concurrent.TimeUnit;
import rx.Observable;

/*
Example external validator. It could for example make a call to external API to validate email
 */
public class ExternalApiEmailValidator implements Validator<EditText> {
  private static final String TAG = "RxValidator";

  @Override public Observable<RxValidationResult<EditText>> validate(String text, EditText item) {
    Log.i(TAG, "Calling custom email validation");
    RxValidationResult<EditText> result;
    if (text.equals("proper@github.com")) {
      result = RxValidationResult.createSuccess(item);
    } else {
      result = RxValidationResult.createImproper(item, "Improper mail");
    }

    return Observable.just(result).delay(1, TimeUnit.SECONDS);
  }
}
