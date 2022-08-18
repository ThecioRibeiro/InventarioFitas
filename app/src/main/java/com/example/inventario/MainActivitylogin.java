package com.example.inventario;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivitylogin extends AppCompatActivity {

    private EditText editTextUsuario, editTextSenha;
    private androidx.appcompat.widget.AppCompatButton btnEntrar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activitylogin);
        getSupportActionBar().hide();
        IniciarComponentes();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean usuarioInformado = !TextUtils.isEmpty(editTextUsuario.getText());
                boolean senhaInformada = !TextUtils.isEmpty(editTextSenha.getText().toString());
                if (usuarioInformado & senhaInformada){
                    Logar(view);
                }else{
                    ExibirMensagem(view,"Digite todos os campos.", Color.WHITE, Color.RED);
                }
            }
        });

    }

    private void IniciarComponentes(){
        editTextUsuario = findViewById(R.id.editUsuario);
        editTextSenha = findViewById(R.id.editSenha);
        btnEntrar = findViewById(R.id.buttonEntrar);
        progressBar = findViewById(R.id.progresbar);
    }



    public void Logar(View view) {

        progressBar.setVisibility(View.VISIBLE);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

        ConexaoApi con = new ConexaoApi();
        con.setNomeUsuario(editTextUsuario.getText().toString());
        con.setSenha(editTextSenha.getText().toString());
        Ion.with(getApplicationContext()).load(con.getUri() + "Logar")
                .setBodyParameter("Nome", con.getNomeUsuario())
                .setBodyParameter("Senha", con.getSenha())
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    ExibirMensagem(view,e.getMessage(), Color.WHITE, Color.RED);
                } else if (result.get("status").getAsString().equals("Erro")) {
                    ExibirMensagem(view,result.get("mensagem").getAsString(), Color.WHITE, Color.RED);
                }else{
                    JsonObject usuario = result.get("usuario").getAsJsonObject();
                    con.setUsuarioId(usuario.get("Id").getAsString());
                    con.setToken(usuario.get("Token").getAsString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("idUsuario", con.getUsuarioId());
                    intent.putExtra("token", con.getToken());
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    public void ExibirMensagem(View view,String mensagem, int corFundo, int corTexto){
        Snackbar snackbar = Snackbar.make(view,mensagem,Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(corFundo);
        snackbar.setTextColor(corTexto);
        OcultarTeclado(view);
        snackbar.show();
    }

    private  void OcultarTeclado(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
    }






}