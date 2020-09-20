package com.thienbinh.apartmentsearch.model

import io.reactivex.subjects.BehaviorSubject

class VariableObservable <T>(defaultValue: T) {
  var value: T = defaultValue
    set(value) {
      field = value
      observable.onNext(value)
    }
  val observable = BehaviorSubject.createDefault(value)
}