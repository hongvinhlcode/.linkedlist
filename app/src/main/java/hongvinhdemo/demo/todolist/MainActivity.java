package hongvinhdemo.demo.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    Database database;
    ToDoAdapter adapter;
    ListView lvToDo;
    LinkedList<ToDo> arrayList;
    EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ControlList();
        database = new Database(this, "todo.sqlite", null, 1);

        database.QueryData("CREATE TABLE IF NOT EXISTS ToDo(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(200))");

        AddToDo();
    }

    private void ControlList(){
        lvToDo = (ListView) findViewById(R.id.listViewToDo);
        arrayList = new LinkedList<>();
        adapter = new ToDoAdapter(this, R.layout.row_todo, arrayList);
        lvToDo.setAdapter(adapter);
    }


    private void AddToDo() {
        Cursor dataToDo = database.GetData("SELECT * FROM ToDo");
        arrayList.clear();
        while (dataToDo.moveToNext()) {
            String name = dataToDo.getString(1);
            int id = dataToDo.getInt(0);
            arrayList.add(new ToDo(id, name));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAdd:
                ShowDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_todo);
        final EditText edtContent = (EditText) dialog.findViewById(R.id.edtContent);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnClickAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnClickCancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtContent.getText().toString().trim();
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Please input content...", Toast.LENGTH_SHORT).show();
                } else {
                    database.QueryData("INSERT INTO ToDo VALUES(null,'" + name + "')");
                    Toast.makeText(MainActivity.this, "Add Succesful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    AddToDo();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void ShowDialogEdit(String oldName, final int id) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_edit);
        final EditText edtContent = (EditText) dialog.findViewById(R.id.edtContentEdit);
        Button btnConfirm = (Button) dialog.findViewById(R.id.btnClickAddEdit);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnClickCancelEdit);

        edtContent.setText(oldName);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = edtContent.getText().toString().trim();
                database.QueryData("UPDATE ToDo SET name = '" + newName + "' WHERE id='" + id + "'");
                Toast.makeText(MainActivity.this, "Update Succesful", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                AddToDo();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void ShowDialogDel(final int getID) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete this node");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE FROM ToDo WHERE id='" + getID + "'");
                Toast.makeText(MainActivity.this, "Delete Succesful", Toast.LENGTH_SHORT).show();
                AddToDo();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
