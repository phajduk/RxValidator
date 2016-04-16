package com.github.phajduk.rxvalidator;

import android.widget.EditText;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class RxValidator implements TypeOfChangeBuilder {

  private List<Validator<EditText>> validators = new ArrayList<>();
  private List<Validator<EditText>> externalValidators = new ArrayList<>();
  private Observable<String> changeEmitter;
  private EditText et;
  public RxValidator(EditText et) {
    this.et = et;
  }

  private static RxValidationResult<EditText> getFirstBadResultOrSuccess(
      List<RxValidationResult<EditText>> results) {
    RxValidationResult<EditText> firstBadResult = null;
    for (RxValidationResult<EditText> result : results) {
      if (!result.isProper()) {
        firstBadResult = result;
        break;
      }
    }
    if (firstBadResult == null) {
      // if there is no bad result, then return first success
      return results.get(0);
    } else {
      return firstBadResult;
    }
  }

  public static TypeOfChangeBuilder createFor(EditText et) {
    return new RxValidator(et);
  }

  @Override public RxValidator onFocusChanged() {
    this.changeEmitter = RxView.focusChanges(et).skip(1).filter(new Func1<Boolean, Boolean>() {
      @Override public Boolean call(Boolean hasFocus) {
        return !hasFocus;
      }
    }).map(new Func1<Boolean, String>() {
      @Override public String call(Boolean aBoolean) {
        return et.getText().toString();
      }
    });
    return this;
  }

  @Override public RxValidator onValueChanged() {
    this.changeEmitter = RxTextView.textChanges(et).skip(1).map(new Func1<CharSequence, String>() {
      @Override public String call(CharSequence charSequence) {
        return charSequence.toString();
      }
    });
    return this;
  }

  @Override public RxValidator onSubscribe() {
    this.changeEmitter = Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(Subscriber<? super String> subscriber) {
        subscriber.onNext(et.getText().toString());
        subscriber.onCompleted();
      }
    });
    return this;
  }

  public RxValidator email() {
    this.validators.add(new RxEmailValidator());
    return this;
  }

  public RxValidator email(String invalidEmailMessage) {
    this.validators.add(new RxEmailValidator(invalidEmailMessage));
    return this;
  }

  public RxValidator nonEmpty() {
    this.validators.add(new RxNonEmptyValidator());
    return this;
  }

  public RxValidator nonEmpty(String cannotBeEmptyMessage) {
    this.validators.add(new RxNonEmptyValidator(cannotBeEmptyMessage));
    return this;
  }

  public RxValidator digitOnly(String digitOnlyErrorMessage) {
    this.validators.add(new RxDigitValidator(digitOnlyErrorMessage));
    return this;
  }

  public RxValidator length(String badLengthMessage, int length) {
    this.validators.add(new RxLengthValidator(badLengthMessage, length));
    return this;
  }

  public RxValidator with(Validator<EditText> externalValidator) {
    this.externalValidators.add(externalValidator);
    return this;
  }

  public Observable<RxValidationResult<EditText>> toObservable() {
    Observable<RxValidationResult<EditText>> validationResultObservable =
        changeEmitter.concatMap(new Func1<String, Observable<RxValidationResult<EditText>>>() {
          @Override public Observable<RxValidationResult<EditText>> call(final String value) {
            return Observable.from(validators)
                .concatMap(
                    new Func1<Validator<EditText>, Observable<RxValidationResult<EditText>>>() {
                      @Override public Observable<RxValidationResult<EditText>> call(
                          Validator<EditText> validator) {
                        return validator.validate(value, et);
                      }
                    });
          }
        })
            .buffer(validators.size())
            .map(new Func1<List<RxValidationResult<EditText>>, RxValidationResult<EditText>>() {
              @Override
              public RxValidationResult<EditText> call(List<RxValidationResult<EditText>> objects) {
                return getFirstBadResultOrSuccess(objects);
              }
            });

    if (externalValidators.isEmpty()) {
      return validationResultObservable;
    }
    return validationResultObservable.flatMap(
        new Func1<RxValidationResult<EditText>, Observable<RxValidationResult<EditText>>>() {
          @Override public Observable<RxValidationResult<EditText>> call(
              final RxValidationResult<EditText> result) {
            // if normal validators doesn't found error, launch external one
            if (result.isProper()) {
              return Observable.from(externalValidators)
                  .concatMap(
                      new Func1<Validator<EditText>, Observable<RxValidationResult<EditText>>>() {
                        @Override public Observable<RxValidationResult<EditText>> call(
                            Validator<EditText> validator) {
                          return validator.validate(result.getValidatedText(), result.getItem());
                        }
                      })
                  .buffer(externalValidators.size())
                  .map(
                      new Func1<List<RxValidationResult<EditText>>, RxValidationResult<EditText>>() {
                        @Override public RxValidationResult<EditText> call(
                            List<RxValidationResult<EditText>> objects) {
                          return getFirstBadResultOrSuccess(objects);
                        }
                      });
            } else {
              return Observable.just(result);
            }
          }
        });
  }
}

