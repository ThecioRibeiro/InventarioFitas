package com.example.inventario;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventario.model.Fita;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPesquisar extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    Button button;
    EditText editText;
    ProgressBar progressBar;
    ConexaoApi con = new ConexaoApi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pesquisar);
        getSupportActionBar().hide();
        listView = findViewById(R.id.listView);
        button = findViewById(R.id.buttonPesquisar);
        button.setOnClickListener(this);
        editText = findViewById(R.id.editTextTextPersonName);
        editText.requestFocus();
        progressBar = findViewById(R.id.progresbar_pesquisar);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                progressBar.setVisibility(View.VISIBLE);
                selecionarItem(i);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            con.setUsuarioId(extras.getString("idUsuario"));
            con.setToken(extras.getString("token"));
            con.setMensagem("");
        };
    }

    List<String> fitasListView = new ArrayList<>();
    Fita[] fitas;

    @Override
    public void onClick(View view) {
        if (editText.getText().equals("")){
            Toast.makeText(getBaseContext(),"Informe a descrição.",Toast.LENGTH_LONG).show();
        }else{
            try {
                BuscarFitas(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void BuscarFitas(View v) throws Exception{

        progressBar.setVisibility(View.VISIBLE);
        listView.setAdapter(null);


        String descricao = editText.getText().toString();

        Ion.with(getBaseContext())
                .load(con.getUri() + "getFitaByDesc")
                .setHeader("Authorization", "Bearer " + con.getToken())
                .setBodyParameter("desc", descricao)
                .setBodyParameter("Usuario_id", con.getUsuarioId())
                .as(new TypeToken<List<Fita>>(){})
                .setCallback(new FutureCallback<List<Fita>>() {
                    @Override
                    public void onCompleted(Exception e, List<Fita> fit) {
                        if (e != null){
                            ExibirMensagem(v ,e.getMessage(), Color.WHITE, Color.RED);
                        }else if(fit.size() == 0){
                            progressBar.setVisibility(View.INVISIBLE);
                            ExibirMensagem(v ,"Nenhum item foi emcontrado.", Color.WHITE, Color.RED);
                        }else{
                            String item;
                            fitas = fit.toArray(new Fita[0]);
                            for (int i = 0; i < fitas.length; i++){
                                item = fitas[i].getCodigo() +" - "+ fitas[i].getDescricao();
                                fitasListView.add(item);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.listview_text_black, fitasListView);
                                listView.setAdapter(adapter);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });
    }

    public void selecionarItem(int i){

        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("codigo",fitas[i].getCodigo());
        intent.putExtra("token", con.getToken());
        intent.putExtra("idUsuario", con.getUsuarioId());
        startActivity(intent);
        finish();

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

    /*public void BuscarFita()throws Exception{
        String dados = null;
        String descricao_fita = editText.getText().toString();
        Conexao conexao = new Conexao("", descricao_fita);
        try {
            dados = conexao.execute().get();
            if (dados == ""){
                throw new Exception("Item não encontrado!");
            }
            Gson gsonFitas = new GsonBuilder().setPrettyPrinting().create();
            Fita[] fitas = gsonFitas.fromJson(dados, Fita[].class);

            String item;
            for (int i = 0; i < fitas.length; i++){
                item = fitas[i].getCodigo() +" - "+ fitas[i].getDescricao();
                fitasListView.add(item);
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fitasListView);
            listView.setAdapter(adapter);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();;
        }
    }*/


}