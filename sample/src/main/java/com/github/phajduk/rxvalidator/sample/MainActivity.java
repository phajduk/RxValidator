package com.github.phajduk.rxvalidator.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import com.github.phajduk.rxvalidator.RxValidationResult;
import com.github.phajduk.rxvalidator.RxValidator;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "RxValidator";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    EditText email = (EditText) findViewById(R.id.etEmail);

    RxValidator.createFor(email)
        .onValueChanged()
        .nonEmpty()
        .email()
        .with(new CustomEmailDomainValidator())
        .with(new ExternalApiEmailValidator())
        .toObservable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<RxValidationResult<EditText>>() {
          @Override public void call(RxValidationResult<EditText> result) {
            if (result.isProper()) {
              result.getItem().setError(null);
              return;
            }
            result.getItem().setError(result.getMessage());
            Log.e(TAG, "Validation result " + result.toString());
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            Log.e(TAG, "Validation error", throwable);
          }
        });
  }
}
