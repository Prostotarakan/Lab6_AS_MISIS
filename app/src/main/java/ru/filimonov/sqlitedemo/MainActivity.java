package ru.filimonov.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ru.filimonov.sqlitedemo.data.DbHelper;
import ru.filimonov.sqlitedemo.data.StudentDbContract.*;

public class MainActivity extends AppCompatActivity {

    private DbHelper dbHelper = null;
    private EditText surnameText = null;
    private EditText nameText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DbHelper(this);
        surnameText = (EditText) findViewById(R.id.SurnameEditText);
        nameText = (EditText) findViewById(R.id.NameEditText);
    }

    public void insertButtonClick(View view) {
        //
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(StudentsEntry.TABLE_NAME).append("(")
                .append(StudentsEntry.COLUMN_SURNAME).append(", ")
                .append(StudentsEntry.COLUMN_NAME)
                .append(") values(")
                .append("'").append(surnameText.getText().toString()).append("'")
                .append(", ")
                .append("'").append(nameText.getText().toString()).append("'")
                .append(")");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql.toString());
    }

    public void viewButtonClick(View view) {
        TextView res = (TextView) findViewById(R.id.textView3);
        res.clearComposingText();
        res.setText("");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(StudentsEntry.TABLE_NAME);
        Cursor cursor = db.rawQuery(sql.toString(), null);
        try {
            String surname;
            String name;
            String info;
            int surnameIndex = cursor.getColumnIndex(StudentsEntry.COLUMN_SURNAME);
            int nameIndex = cursor.getColumnIndex(StudentsEntry.COLUMN_NAME);
            while (cursor.moveToNext()){
                surname = cursor.getString(surnameIndex);
                name = cursor.getString(nameIndex);
                info = String.format("Фамилия: %1$s; Имя: %2$s\n", surname, name);
                res.append(info);
            }
        } finally {
            cursor.close();
        }



    }

    public void delButtonClick(View view) {
        //
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ").append(StudentsEntry.TABLE_NAME).append(" WHERE ")
                .append(StudentsEntry.COLUMN_SURNAME).append(" = ")
                .append("'").append(surnameText.getText().toString()).append("' AND ")
                .append(StudentsEntry.COLUMN_NAME).append(" = ")
                .append("'").append(nameText.getText().toString()).append("'");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql.toString());
    }

    public void findButtonClick(View view) {
        TextView res = (TextView) findViewById(R.id.textView3);
        res.clearComposingText();
        res.setText("");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(StudentsEntry.TABLE_NAME);
        Cursor cursor = db.rawQuery(sql.toString(), null);
        try {
            String surname=surnameText.getText().toString();
            String name=nameText.getText().toString();
            String info;
            int surnameIndex = cursor.getColumnIndex(StudentsEntry.COLUMN_SURNAME);
            int nameIndex = cursor.getColumnIndex(StudentsEntry.COLUMN_NAME);
            while (cursor.moveToNext()){
                if  ((surname.equals(cursor.getString(surnameIndex))) && (name.equals(cursor.getString(nameIndex)))) {
                info = String.format("Фамилия: %1$s; Имя: %2$s\n", surname, name);
                res.append(info);}
            }
        } finally {
            cursor.close();
            if (res.length()<1) { res.append("Такого нет"); }
        }

    }
}