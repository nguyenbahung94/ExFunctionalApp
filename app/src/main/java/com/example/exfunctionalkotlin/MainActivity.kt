package com.example.exfunctionalkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.exfunctionalkotlin.model.*
import com.example.exfunctionalkotlin.store.Renderer
import com.example.exfunctionalkotlin.store.TodoStore
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.selector

class MainActivity : AppCompatActivity(), Renderer<TodoModel> {
    private lateinit var store: TodoStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store = ViewModelProviders.of(this).get(TodoStore::class.java)
        store.subscribe(this, mapStateToProps)

        addButton.setOnClickListener {
            store.disPath(AddTodo(editText.text.toString()))
            editText.text = null
        }
        fab.setOnClickListener { openDialog() }

        listView.adapter = TodoAdapter(this, listOf())
        listView.setOnItemClickListener { _, _, _, id ->
            store.disPath(ToggleTodo(id))
        }
        listView.setOnItemLongClickListener() { _, _, _, id ->
            store.disPath(RemoveTodo(id))
            true
        }

    }

    private fun openDialog() {
        val options = resources.getStringArray(R.array.filter_options).asList()
        selector(getString(R.string.filter_title), options, { _, i ->
            val visible = when (i) {
                1 -> Visibility.Active()
                2 -> Visibility.Completed()
                else -> Visibility.All()
            }
            store.disPath(Setvisibility(visible))
        })
    }

    private val mapStateToProps = Function<TodoModel, TodoModel> {
        val keep: (Todo) -> Boolean = when (it.visibility) {

            is Visibility.All -> { _ ->
                true
            }
            is Visibility.Active -> { t: Todo ->
                !t.status
            }
            is Visibility.Completed -> { t: Todo ->
                t.status
            }
        }
        return@Function it.copy(todos = it.todos.filter { keep(it) })
    }


    override fun render(model: LiveData<TodoModel>) {
        model.observe(this, Observer { newState ->
            listView.adapter = TodoAdapter(this, newState?.todos ?: listOf())
        })
    }
}
