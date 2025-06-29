package com.example.dentistclinic;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText edtUsuario, edtSenha;
    Button btnLogin;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsuario = findViewById(R.id.edtUsuario);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnLogin);
        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String user = edtUsuario.getText().toString();
            String pass = edtSenha.getText().toString();

            if (db.verificarLogin(user, pass)) {
                startActivity(new Intent(this, ClienteActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}