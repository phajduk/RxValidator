# RxValidator
[![Build Status](https://travis-ci.org/phajduk/RxValidator.svg?branch=master)](https://travis-ci.org/phajduk/RxValidator)

Easy Android form validation in Rx way.

## Dependencies
- RxJava
- RxBinding

## Usage
RxValidator provides fluent API to define validation rules:
```
Observable<RxValidationResult> observable = 
    RxValidator.createFor(password)
        .nonEmpty()
        .length("Min length is 5", 5)
        .onFocusChanged()
        .toObservable();
```
In above example `nonEmpty` and `length` are validators, `.onFocusChanged()` decides about validation trigger. 

### Validators
At the moment we have following pre-defined validation rules:
- `email`
- `nonEmpty`
- `digitOnly`
- `length`
- `minLength`
- `maxLength`
- `age`
- `sameAs`
- `ip4`

You can also provide custom validation rule using `with(Validator<EditText> externalValidator)` operator. **It gives you possibility to define your own rules e.g. validate e-mail via HTTP call.**

### Validation trigger
In order to decide, when validation should be launched you can choose one from the following options: 
- `onFocusChanged` - validation result is emitted when `EditText` lost focus
- `onValueChanged` - validation result is emitted when value of `EditText` changed
- `onSubscribe` - validation result is emitted immediately

## Download
Gradle:
```groovy
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
    compile 'com.github.phajduk:RxValidator:master-SNAPSHOT'
}

```

# License
    Copyright 2016 Pawel Hajduk

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
