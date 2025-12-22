package com.ext.smartmaterialspinner

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import com.google.android.material.bottomsheet.BottomSheetDialog


class SmartMaterialSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    private lateinit var editText: TextInputEditText
    private var items: List<String> = emptyList()

    private var spinnerMode = MODE_DIALOG
    private var enableSearch = false
    private var spinnerTitle: String? = null
    private var onItemSelected: ((item: String, position: Int) -> Unit)? = null


    companion object {
        private const val MODE_DIALOG = 0
        private const val MODE_BOTTOM_SHEET = 1
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SmartMaterialSpinner,
            0,
            0
        ).apply {
            try {
                spinnerMode = getInt(
                    R.styleable.SmartMaterialSpinner_spinnerMode,
                    MODE_DIALOG
                )
                enableSearch = getBoolean(
                    R.styleable.SmartMaterialSpinner_enableSearch,
                    false
                )
                spinnerTitle = getString(
                    R.styleable.SmartMaterialSpinner_spinnerTitle
                )
            } finally {
                recycle()
            }
        }

        initView()
    }


    private fun initView() {
        inflate(context, R.layout.view_smart_spinner, this)
        editText = findViewById(R.id.etSpinner)

        editText.setOnClickListener {
            when (spinnerMode) {
                MODE_DIALOG -> showItemsDialog()
                MODE_BOTTOM_SHEET -> showBottomSheet()
            }
        }
    }


    /** ✅ THIS IS WHAT YOU WERE MISSING */
    fun setItems(items: List<String>) {
        this.items = items
    }

    private fun showItemsDialog() {
        if (items.isEmpty()) return

        val filteredItems = items.toMutableList()
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, filteredItems)

        val dialogView = inflate(context, R.layout.view_spinner_search, null)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val etSearch = dialogView.findViewById<EditText>(R.id.etSearch)

        listView.adapter = adapter

        if (!enableSearch) {
            etSearch.visibility = View.GONE
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filteredItems.clear()
                filteredItems.addAll(
                    items.filter { it.contains(s.toString(), true) }
                )
                adapter.notifyDataSetChanged()
            }
        })

        val builder = AlertDialog.Builder(context)
            .setView(dialogView)

        spinnerTitle?.let {
            builder.setTitle(it)
        }

        val dialog = builder.create()

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = filteredItems[position]
            editText.setText(selectedItem)

            val originalPosition = items.indexOf(selectedItem)
            onItemSelected?.invoke(selectedItem, originalPosition)

            dialog.dismiss()
        }


        dialog.show()
    }

    private fun showBottomSheet() {
        if (items.isEmpty()) return

        val filteredItems = items.toMutableList()
        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, filteredItems)

        val sheet = BottomSheetDialog(context)
        val view = inflate(context, R.layout.view_spinner_search, null)

        val listView = view.findViewById<ListView>(R.id.listView)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)

        listView.adapter = adapter

        if (!enableSearch) {
            etSearch.visibility = View.GONE
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filteredItems.clear()
                filteredItems.addAll(
                    items.filter { it.contains(s.toString(), true) }
                )
                adapter.notifyDataSetChanged()
            }
        })

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = filteredItems[position]
            editText.setText(selectedItem)

            val originalPosition = items.indexOf(selectedItem)
            onItemSelected?.invoke(selectedItem, originalPosition)

            sheet.dismiss()
        }

        sheet.setContentView(view)
        sheet.show()
    }


    fun setOnItemSelectedListener(listener: (String, Int) -> Unit) {
        onItemSelected = listener
    }



}
