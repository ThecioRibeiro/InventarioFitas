package com.example.inventario;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Conexao extends AsyncTask<Void, Void, String> {



    public String codigo;
    public String descricao;

    public Conexao(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder stringBuilder = new StringBuilder();
        URL url;
        try {
            if(codigo != ""){
               url = new URL("http://192.168.0.206/Inventario/public/getFita/" + codigo);
            }else{
               url = new URL("http://192.168.0.206/Inventario/public/getFitaByDesc/" + descricao);
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(3000);
            conn.connect();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()){
                stringBuilder.append(scanner.next() + " ");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }





}
