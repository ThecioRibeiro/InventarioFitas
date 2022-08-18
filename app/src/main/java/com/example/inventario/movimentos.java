package com.example.inventario;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inventario.model.FitaLancamento;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class movimentos extends AppCompatActivity {

    ListView listViewData;
    ProgressBar progressBar;
    ConexaoApi con = new ConexaoApi();
    List<String> LancamentosListView = new ArrayList<>();
    String[] fitasListViewArrayStr;
    List<String> Lanc_ids = new ArrayList<>();
    List<String> Lanc_ids_selected = new ArrayList<>();
    List<Integer> ItensChecadoslistViewData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentos);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9bc393")));
        listViewData = findViewById(R.id.listView_data);
        progressBar = findViewById(R.id.progresbar_movimentos);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            con.setUsuarioId(extras.getString("idUsuario"));
            con.setToken(extras.getString("token"));
            con.setMensagem("");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return  true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_done){
            for (int i = 0; i < listViewData.getCount(); i++) {
                if (listViewData.isItemChecked(i)){
                    ItensChecadoslistViewData.add(i);
                    Lanc_ids_selected.add(Lanc_ids.get(i));
                }
            }
            if (ItensChecadoslistViewData.size() == 0){
                ExibirMensagem("Deletar", "Nenhum item foi selecionado!");
            }else{
                mensagenConfirm("Deletar", "Deseja deletar o(s) item(ns) marcado(s)?");
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void BuscarLancamentos(){

        Ion.with(getBaseContext())
                .load(con.getUri() + "InventarioGetLancToday")
                .setHeader("Authorization", "Bearer " + con.getToken())
                .setBodyParameter("Usuario_id", con.getUsuarioId())
                .as(new TypeToken<List<FitaLancamento>>(){})
                .setCallback(new FutureCallback<List<FitaLancamento>>() {
                    @Override
                    public void onCompleted(Exception e, List<FitaLancamento> fitLanc) {
                        if (e != null){
                            ExibirMensagem("Erro" ,e.getMessage());
                        }else if(fitLanc.size() == 0){
                            ExibirMensagem("Lançamentos" ,"Nenhum lançamento foi emcontrado.");
                            progressBar.setVisibility(View.INVISIBLE);
                        }else{
                            FitaLancamento[] fitaLancamentos;
                            String item;
                            fitaLancamentos = fitLanc.toArray(new FitaLancamento[0]);
                            for (int i = 0; i < fitaLancamentos.length; i++){
                                item =  fitaLancamentos[i].getDescricao() +" - "+
                                        fitaLancamentos[i].getMedida_1()+ "x" +
                                        fitaLancamentos[i].getMedida_2()+ " - " +
                                        fitaLancamentos[i].getQtdade();
                                        LancamentosListView.add(item);
                                        Lanc_ids.add(String.valueOf(fitaLancamentos[i].getLanc_id()));
                            }
                            fitasListViewArrayStr = LancamentosListView.toArray(new String[0]);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.listview_check,fitasListViewArrayStr);
                            listViewData.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }


    public void mensagenConfirm(String titulo, String texto){

        progressBar.setVisibility(View.VISIBLE);
        AlertDialog.Builder mensagem = new AlertDialog.Builder(this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);
        mensagem.setNeutralButton("Cancelar", null);
        mensagem.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeletarMovimentos();
            }
        });
        mensagem.show();
    }


    public void ExibirMensagem(String titulo, String texto){

        AlertDialog.Builder mensagem = new AlertDialog.Builder(this);
        mensagem.setTitle(titulo);
        mensagem.setMessage(texto);
        mensagem.setPositiveButton("ok", null);
        mensagem.show();
    }


    public  void DeletarMovimentos(){

        for (int i = 0; i < Lanc_ids_selected.size(); i++) {
            int j = i;
            Ion.with(getApplicationContext()).load(con.getUri() + "InventarioApagar")
                    .setHeader("Authorization", "Bearer " + con.getToken())
                    .setBodyParameter("Usuario_id", con.getUsuarioId())
                    .setBodyParameter("Lanc_id", Lanc_ids_selected.get(i))
                    .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {
                    if (e != null) {
                        ExibirMensagem("Erro" ,e.getMessage());
                    } else if (result.get("status").getAsString().equals("Erro")) {
                        ExibirMensagem("Erro" ,result.get("mensagem").getAsString());
                    }else{
                        if (j == Lanc_ids_selected.size() - 1){
                            ExibirMensagem("sucesso" ,result.get("mensagem").getAsString());
                        }
                        RemoverDoListView();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

    }


    public  void RemoverDoListView(){

        Collections.sort(ItensChecadoslistViewData, Collections.reverseOrder());
        for (int i = 0; i < ItensChecadoslistViewData.size(); i++) {
            int iRemove = ItensChecadoslistViewData.get(i);
            LancamentosListView.remove(iRemove);
        }
        ItensChecadoslistViewData.clear();
        Lanc_ids_selected.clear();
        fitasListViewArrayStr = LancamentosListView.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(), R.layout.listview_check,fitasListViewArrayStr);
        listViewData.setAdapter(adapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        BuscarLancamentos();
    }

}