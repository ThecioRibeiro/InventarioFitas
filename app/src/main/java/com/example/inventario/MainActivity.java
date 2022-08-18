package com.example.inventario;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.inventario.model.Fita;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    ConexaoApi con = new ConexaoApi();
    Fita fita = new Fita();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        this.mViewHolder.editCodigo = findViewById(R.id.editTextCodigo);
        this.mViewHolder.editMedidaMaior = findViewById(R.id.editTextMedidaMaior);
        this.mViewHolder.editMedidaMenor = findViewById(R.id.editTextMedidaMenor);
        this.mViewHolder.editResultado = findViewById(R.id.editTextResultado);
        this.mViewHolder.buttonMenos = findViewById(R.id.buttonMenos);
        this.mViewHolder.buttonMais = findViewById(R.id.buttonMais);
        this.mViewHolder.textViewDescricao = findViewById(R.id.textViewDescricao);
        this.mViewHolder.buttonSalvar = findViewById(R.id.buttonSalvar);
        this.mViewHolder.imageView = findViewById(R.id.imageView);
        this.mViewHolder.imageList = findViewById(R.id.imageList);
        this.mViewHolder.editTextQtde = findViewById(R.id.editTextQtde);
        this.mViewHolder.progressBar = findViewById(R.id.progresbar_inventario);
        this.mViewHolder.buttonMenos.setOnClickListener(this);
        this.mViewHolder.buttonMais.setOnClickListener(this);
        this.mViewHolder.buttonSalvar.setOnClickListener(this);
        this.ClearValues();
        this.mViewHolder.editCodigo.requestFocus();
        this.mViewHolder.imageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirFormMovimentos();
            }
        });
        this.mViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbrirFormPesquisar();
            }
        });
    }

    private  static  class ViewHolder{
        EditText editCodigo;
        EditText editMedidaMaior;
        EditText editMedidaMenor;
        EditText editResultado;
        androidx.appcompat.widget.AppCompatButton buttonCalcular;
        androidx.appcompat.widget.AppCompatButton buttonSalvar;
        androidx.appcompat.widget.AppCompatButton buttonMenos;
        androidx.appcompat.widget.AppCompatButton buttonMais;
        TextView textViewDescricao;
        EditText editTextQtde;
        ImageView imageView;
        ImageView imageList;
        ProgressBar progressBar;
    }

    @Override
    public void onClick(View v) {

        boolean dadosValidados = validarFormulario(v);
        if (dadosValidados){
            int i = Integer.parseInt(this.mViewHolder.editTextQtde.getText().toString());

            if(v.getId() == R.id.buttonSalvar){
                this.mViewHolder.progressBar.setVisibility(View.VISIBLE);
            try {
                Salvar(v);
            } catch (Exception e) {
                ExibirMensagem(v ,e.getMessage(), Color.WHITE, Color.RED);
                this.mViewHolder.progressBar.setVisibility(View.INVISIBLE);
            }
//teste
            }else if(v.getId() == R.id.buttonMais){
                this.mViewHolder.editTextQtde.setText(String.valueOf(++i));
            }else if(v.getId() == R.id.buttonMenos){
                i = i == 1? 2 : i;
                this.mViewHolder.editTextQtde.setText(String.valueOf(--i));
            }
        }else{
            ExibirMensagem(v ,"Campo não digitado", Color.WHITE, Color.RED);
        }

    }


    public void ValidarMedidas(View v) throws Exception {

        fita.setCodigo(Integer.parseInt(this.mViewHolder.editCodigo.getText().toString()));
        fita.setMaiorMedidaRolo(Integer.parseInt(this.mViewHolder.editMedidaMaior.getText().toString()));
        fita.setMenorMedidaRolo(Integer.parseInt(this.mViewHolder.editMedidaMenor.getText().toString()));
    }

    private boolean validarFormulario(View v) {
        boolean retorno = false;
        boolean buttonSalvar = v.getId() == R.id.buttonSalvar;
        boolean buttonQtde = v.getId() == R.id.buttonMais || v.getId() == R.id.buttonMenos;
        boolean editCodigo = !TextUtils.isEmpty(this.mViewHolder.editCodigo.getText());
        boolean editMedidaMaior = !TextUtils.isEmpty(this.mViewHolder.editMedidaMaior.getText());
        boolean editMedidaMenor = !TextUtils.isEmpty(this.mViewHolder.editMedidaMenor.getText());

        if (buttonSalvar){
            if (editCodigo & editMedidaMaior & editMedidaMenor){
                retorno = true;
            }
        }else if (buttonQtde){
            retorno = true;
        }
        return retorno;
    }

    private void ClearValues(){
        this.mViewHolder.editMedidaMaior.setText("");
        this.mViewHolder.editMedidaMenor.setText("");
        this.mViewHolder.editResultado.setText("");
        this.mViewHolder.editTextQtde.setText("1");
        this.mViewHolder.editCodigo.requestFocus();
        this.mViewHolder.editCodigo.selectAll();
    }

    public void AbrirFormPesquisar(){
        Intent intent = new Intent(this, MainActivityPesquisar.class);
        intent.putExtra("idUsuario", con.getUsuarioId());
        intent.putExtra("token", con.getToken());
        startActivity(intent);
    }

    public void AbrirFormMovimentos(){
        Intent intent = new Intent(this, movimentos.class);
        intent.putExtra("idUsuario", con.getUsuarioId());
        intent.putExtra("token", con.getToken());
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String codigo = String.valueOf(extras.getInt("codigo"));
            codigo = (codigo.equals("0") ? "" : codigo);
            this.mViewHolder.editCodigo.setText(codigo) ;
            this.mViewHolder.textViewDescricao.setText(extras.getString("descricao"));
            con.setUsuarioId(extras.getString("idUsuario"));
            con.setToken(extras.getString("token"));
        }

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

    private void Salvar(View v)throws Exception{

        ValidarMedidas(v);
        Ion.with(getApplicationContext()).load(con.getUri() + "InventarioRegistar")
                .setHeader("Authorization", "Bearer " + con.getToken())
                .setBodyParameter("Usuario_id", con.getUsuarioId())
                .setBodyParameter("Codigo", String.valueOf(fita.getCodigo()))
                .setBodyParameter("Medida_1", String.valueOf(fita.getMaiorMedidaRolo()))
                .setBodyParameter("Medida_2", String.valueOf(fita.getMenorMedidaRolo()))
                .setBodyParameter("Qtdade", String.valueOf(mViewHolder.editTextQtde.getText()))
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    con.setMensagem(e.getMessage());
                } else if (result.get("status").getAsString().equals("Erro")) {
                    con.setMensagem(result.get("mensagem").getAsString());
                    ExibirMensagem(v ,con.getMensagem(), Color.WHITE, Color.RED);
                }else{
                    JsonObject fitaJ = result.get("Fita").getAsJsonObject();
                    fita.setDescricao(fitaJ.get("Descricao").getAsString());
                    fita.setMetragem(Double.parseDouble(result.get("Metragem").getAsString()));
                    mViewHolder.editResultado.setText(result.get("Metragem").getAsString());
                    mViewHolder.textViewDescricao.setText(fita.getDescricao());
                    ExibirMensagem(v ,result.get("mensagem").getAsString(), Color.WHITE, Color.GREEN);
                    ClearValues();
                }
                mViewHolder.progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }


    /*public void CalcularFita(View v){

        mViewHolder.progressBar.setVisibility(View.VISIBLE);
        fita.setCodigo(Integer.parseInt(this.mViewHolder.editCodigo.getText().toString()));
        Ion.with(getApplicationContext()).load(con.getUri() + "GetFitaByCod")
                .setHeader("Authorization", "Bearer " + con.getToken())
                .setBodyParameter("Codigo", String.valueOf(fita.getCodigo()))
                .setBodyParameter("Usuario_id", con.getUsuarioId())
                .asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    con.setMensagem(e.getMessage());
                } else if (result.get("status").getAsString().equals("Erro")) {
                    con.setMensagem(result.get("mensagem").getAsString());
                    mViewHolder.progressBar.setVisibility(View.INVISIBLE);
                    ExibirMensagem(v ,con.getMensagem(), Color.WHITE, Color.RED);
                }else{
                    JsonObject fitaJ = result.get("fita").getAsJsonObject();
                    fita.setEspessura(Double.parseDouble(fitaJ.get("Espessura").getAsString()));
                    fita.setDescricao(fitaJ.get("Descricao").getAsString());
                    fita.setId(Integer.parseInt(fitaJ.get("Id").getAsString()));
                    mViewHolder.textViewDescricao.setText(fita.getDescricao());
                    //CalcularMetragem(v);
                }
            }
        });
    }*/




    }