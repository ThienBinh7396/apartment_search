package com.thienbinh.apartmentsearch.ui.customView

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.thienbinh.apartmentsearch.R
import com.thienbinh.apartmentsearch.databinding.WidgetInputNumberLayoutBinding
import com.thienbinh.apartmentsearch.model.VariableObservable
import com.thienbinh.apartmentsearch.model.enum.ETypeWidgetInputNumber
import com.thienbinh.apartmentsearch.store
import com.thienbinh.apartmentsearch.store.action.ApartmentAction
import com.thienbinh.apartmentsearch.viewModel.WidgetInputNumberViewModel
import java.lang.Error

class WidgetInputNumber @JvmOverloads constructor(
  context: Context,
  attributeSet: AttributeSet,
  defStyleAttr: Int = 0
) :
  LinearLayout(context, attributeSet, defStyleAttr) {

  private val contentView =
    inflate(
      context,
      R.layout.widget_input_number_layout,
      this
    )

  private val tvNumber: TextView = contentView.findViewById(R.id.tvNumber)
  private val btnMinus: ImageButton = contentView.findViewById<ImageButton>(R.id.btnMinus).apply {
    setOnClickListener {
      numberValue--

      handleUpdateToStoreValue()
    }
  }
  val btnPlus = contentView.findViewById<ImageButton>(R.id.btnPlus).apply {
    setOnClickListener {
      numberValue++

      handleUpdateToStoreValue()
    }
  }

  private fun handleUpdateToStoreValue() {
    store.state.apartmentState.apartmentFilterModel.apply {

      when (type) {
        ETypeWidgetInputNumber.ADULT.ordinal -> {
          if (adultsAmount != numberValue)
            store.dispatch(
              ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_FILTER(
                adults = numberValue
              )
            )
        }

        ETypeWidgetInputNumber.CHILDREN.ordinal -> {
          if (childrenAmount != numberValue)
            store.dispatch(
              ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_FILTER(
                children = numberValue
              )
            )
        }

        ETypeWidgetInputNumber.INFANT.ordinal -> {
          if (infantsAmount != numberValue)
            store.dispatch(
              ApartmentAction.Apartment_ACTION_UPDATE_APARTMENT_FILTER(
                infants = numberValue
              )
            )
        }
      }
    }
  }

  private var min = 0
  private var max = 10

  private var type = ETypeWidgetInputNumber.ADULT.ordinal

  private var numberValue = min
    set(value) {
      val realValue = if (value < min) min else if (value > max) max else value

      field = realValue
      tvNumber.text = realValue.toString()

      tvNumber.setTextColor(
        if (realValue > 0) ContextCompat.getColor(
          context,
          R.color.colorText
        ) else ContextCompat.getColor(context, R.color.colorTextDisabled)
      )

      btnMinus.visibility = if (realValue > min) View.VISIBLE else View.INVISIBLE

      btnMinus.isEnabled = realValue > min

      btnPlus.visibility = if (realValue < max) View.VISIBLE else View.INVISIBLE

      btnPlus.isEnabled = realValue < max
    }

  fun updateNumberValue(num: Int) {
    Log.d("Binh", "Update  number: $num")
    numberValue = num
  }

  init {

    Log.d("Binh", "Content View; $contentView")

    val attributeArray = context.theme.obtainStyledAttributes(
      attributeSet,
      R.styleable.WidgetInputNumber, 0, 0
    )

    try {
      numberValue = attributeArray.getInt(R.styleable.WidgetInputNumber_numberValue, 0)

      max = attributeArray.getInt(R.styleable.WidgetInputNumber_max, 10)
      min = attributeArray.getInt(R.styleable.WidgetInputNumber_min, 0)

      type = attributeArray.getInt(
        R.styleable.WidgetInputNumber_typeChange,
        ETypeWidgetInputNumber.ADULT.ordinal
      )


      Log.d("Binh", "Widget button error: $max $min $type")

    } catch (err: Error) {
      Log.d("Binh", "Widget button error: ${err.message}")
    }

  }


}