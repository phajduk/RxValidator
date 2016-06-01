package com.github.phajduk.rxvalidator;

import android.widget.EditText;
import java.util.List;

public class ValidationResultHelper {
  public static RxValidationResult<EditText> getFirstBadResultOrSuccess(
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
}
