package com.github.phajduk.rxvalidator;

import android.widget.EditText;
import android.widget.TextView;
import com.github.phajduk.rxvalidator.validators.AgeValidator;
import com.github.phajduk.rxvalidator.validators.DigitValidator;
import com.github.phajduk.rxvalidator.validators.EmailValidator;
import com.github.phajduk.rxvalidator.validators.InListValidator;
import com.github.phajduk.rxvalidator.validators.Ip4Validator;
import com.github.phajduk.rxvalidator.validators.LengthValidator;
import com.github.phajduk.rxvalidator.validators.MaxLengthValidator;
import com.github.phajduk.rxvalidator.validators.MinLengthValidator;
import com.github.phajduk.rxvalidator.validators.NonEmptyValidator;
import com.github.phajduk.rxvalidator.validators.PatternValidator;
import com.github.phajduk.rxvalidator.validators.SameAsValidator;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class RxValidator {

  private List<Validator<EditText>> validators = new ArrayList<>();
  private List<Validator<EditText>> externalValidators = new ArrayList<>();
  private Observable<String> changeEmitter;
  private EditText et;

  private RxValidator(EditText et) {
    this.et = et;
  }

  public static RxValidator createFor(EditText et) {
    return new RxValidator(et);
  }

  public RxValidator onFocusChanged() {
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

  public RxValidator onValueChanged() {
    this.changeEmitter = RxTextView.textChanges(et).skip(1).map(new Func1<CharSequence, String>() {
      @Override public String call(CharSequence charSequence) {
        return charSequence.toString();
      }
    });
    return this;
  }

  public RxValidator onSubscribe() {
    this.changeEmitter = Observable.create(new Observable.OnSubscribe<String>() {
      @Override public void call(Subscriber<? super String> subscriber) {
        subscriber.onNext(et.getText().toString());
        subscriber.onCompleted();
      }
    });
    return this;
  }

  public RxValidator email() {
    this.validators.add(new EmailValidator());
    return this;
  }

  public RxValidator email(String invalidEmailMessage) {
    this.validators.add(new EmailValidator(invalidEmailMessage));
    return this;
  }

  public RxValidator email(String invalidEmailMessage, Pattern pattern) {
    this.validators.add(new EmailValidator(invalidEmailMessage, pattern));
    return this;
  }

  public RxValidator ip4() {
    this.validators.add(new Ip4Validator());
    return this;
  }

  public RxValidator ip4(String invalidIp4Message) {
    this.validators.add(new Ip4Validator(invalidIp4Message));
    return this;
  }

  public RxValidator pattern(String invalidValueMessage, Pattern pattern) {
    this.validators.add(new PatternValidator(invalidValueMessage, pattern));
    return this;
  }

  public RxValidator pattern(String invalidValueMessage, String pattern) {
    this.validators.add(new PatternValidator(invalidValueMessage, pattern));
    return this;
  }

  public RxValidator nonEmpty() {
    this.validators.add(new NonEmptyValidator());
    return this;
  }

  public RxValidator nonEmpty(String cannotBeEmptyMessage) {
    this.validators.add(new NonEmptyValidator(cannotBeEmptyMessage));
    return this;
  }

  public RxValidator digitOnly() {
    this.validators.add(new DigitValidator());
    return this;
  }

  public RxValidator digitOnly(String digitOnlyErrorMessage) {
    this.validators.add(new DigitValidator(digitOnlyErrorMessage));
    return this;
  }

  public RxValidator minLength(int length) {
    this.validators.add(new MinLengthValidator(length));
    return this;
  }

  public RxValidator minLength(int length, String badLengthMessage) {
    this.validators.add(new MinLengthValidator(badLengthMessage, length));
    return this;
  }

  public RxValidator maxLength(int length) {
    this.validators.add(new MaxLengthValidator(length));
    return this;
  }

  public RxValidator maxLength(int length, String badLengthMessage) {
    this.validators.add(new MaxLengthValidator(badLengthMessage, length));
    return this;
  }

  public RxValidator length(int length) {
    this.validators.add(new LengthValidator(length));
    return this;
  }

  public RxValidator length(int length, String badLengthMessage) {
    this.validators.add(new LengthValidator(badLengthMessage, length));
    return this;
  }

  public RxValidator age(String badAgeMessage, int age, SimpleDateFormat sdf) {
    this.validators.add(new AgeValidator(badAgeMessage, sdf, age));
    return this;
  }

  public RxValidator sameAs(TextView anotherTextView, String message) {
    this.validators.add(new SameAsValidator(anotherTextView, message));
    return this;
  }

  public RxValidator in(String message, List<String> properValues) {
    this.validators.add(new InListValidator(message, properValues));
    return this;
  }

  public RxValidator with(Validator<EditText> externalValidator) {
    this.externalValidators.add(externalValidator);
    return this;
  }

  public Observable<RxValidationResult<EditText>> toObservable() {
    if (changeEmitter == null) {
      throw new ChangeEmitterNotSetException(
          "Change emitter have to be set. Did you forget to set onFocusChanged, onValueChanged or onSubscribe?");
    }
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
                return ValidationResultHelper.getFirstBadResultOrSuccess(objects);
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
                          return ValidationResultHelper.getFirstBadResultOrSuccess(objects);
                        }
                      });
            } else {
              return Observable.just(result);
            }
          }
        });
  }
}

