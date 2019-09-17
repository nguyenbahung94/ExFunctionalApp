package com.example.exfunctionalkotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.exfunctionalkotlin.model.Todo
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoAdapter(context: Context, val todos: List<Todo>) : ArrayAdapter<Todo>(context, 0, todos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_todo, parent, false)

        view.textView.text = todos[position].text
        view.checkBox.isChecked = todos[position].status
        return view
    }

    override fun getItemId(position: Int): Long = todos[position].id
}