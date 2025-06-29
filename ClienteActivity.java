package com.example.dentistclinic;

import android.app.*;
import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ClienteActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText nome, telefone, email;
    Button salvar;
    ListView lista;

    ArrayAdapter<String> adapter;
    ArrayList<String> clientes;
    ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        db = new DatabaseHelper(this);
        nome = findViewById(R.id.nome);
        telefone = findViewById(R.id.telefone);
        email = findViewById(R.id.email);
        salvar = findViewById(R.id.salvar);
        lista = findViewById(R.id.lista);

        clientes = new ArrayList<>();
        ids = new ArrayList<>();

        carregarClientes();

        salvar.setOnClickListener(v -> {
            db.getWritableDatabase().execSQL("INSERT INTO clientes (nome, telefone, email) VALUES (?, ?, ?)",
                    new Object[]{nome.getText().toString(), telefone.getText().toString(), email.getText().toString()});
            carregarClientes();
            nome.setText(""); telefone.setText(""); email.setText("");
        });

        lista.setOnItemClickListener((parent, view, position, id) -> {
            int clienteId = ids.get(position);
            new AlertDialog.Builder(this)
                .setTitle("Opções")
                .setItems(new CharSequence[]{"Editar", "Excluir"}, (dialog, which) -> {
                    if (which == 0) {
                        editarCliente(clienteId);
                    } else {
                        db.getWritableDatabase().execSQL("DELETE FROM clientes WHERE id = ?", new Object[]{clienteId});
                        carregarClientes();
                    }
                }).show();
        });
    }

    private void carregarClientes() {
        clientes.clear();
        ids.clear();
        Cursor cursor = db.getReadableDatabase().rawQuery("SELECT * FROM clientes", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nome = cursor.getString(1);
            String telefone = cursor.getString(2);
            String email = cursor.getString(3);
            clientes.add(nome + " - " + telefone + " - " + email);
            ids.add(id);
        }
        cursor.close();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, clientes);
        lista.setAdapter(adapter);
    }

    private void editarCliente(int id) {
        Cursor c = db.getReadableDatabase().rawQuery("SELECT * FROM clientes WHERE id = ?", new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            nome.setText(c.getString(1));
            telefone.setText(c.getString(2));
            email.setText(c.getString(3));
        }
        c.close();

        salvar.setOnClickListener(v -> {
            db.getWritableDatabase().execSQL("UPDATE clientes SET nome = ?, telefone = ?, email = ? WHERE id = ?",
                    new Object[]{nome.getText().toString(), telefone.getText().toString(), email.getText().toString(), id});
            carregarClientes();
            nome.setText(""); telefone.setText(""); email.setText("");
            salvar.setOnClickListener(this::salvarNovoCliente);
        });
    }

    private void salvarNovoCliente(View view) {
        db.getWritableDatabase().execSQL("INSERT INTO clientes (nome, telefone, email) VALUES (?, ?, ?)",
                new Object[]{nome.getText().toString(), telefone.getText().toString(), email.getText().toString()});
        carregarClientes();
        nome.setText(""); telefone.setText(""); email.setText("");
    }
}