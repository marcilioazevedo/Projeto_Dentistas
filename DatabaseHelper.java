package com.example.dentistclinic;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "clinica.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT, senha TEXT)");
        db.execSQL("INSERT INTO usuarios (usuario, senha) VALUES ('admin', '1234')");
        db.execSQL("CREATE TABLE clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT, email TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS clientes");
        onCreate(db);
    }

    public boolean verificarLogin(String usuario, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE usuario = ? AND senha = ?", new String[]{usuario, senha});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
}