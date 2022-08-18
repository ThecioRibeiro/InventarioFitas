package com.example.inventario.model;

public class FitaLancamento {

    private int Codigo;
    private String Descricao;
    private int Lanc_id;
    private  int Medida_1;
    private  int Medida_2;
    private  double Metragem_unit;
    private int Qtdade;
    private String Data;

    public int getCodigo() {
        return Codigo;
    }

    public void setCodigo(int codigo) {
        Codigo = codigo;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public int getLanc_id() {
        return Lanc_id;
    }

    public void setLanc_id(int lanc_id) {
        Lanc_id = lanc_id;
    }

    public int getMedida_1() {
        return Medida_1;
    }

    public void setMedida_1(int medida_1) {
        Medida_1 = medida_1;
    }

    public int getMedida_2() {
        return Medida_2;
    }

    public void setMedida_2(int medida_2) {
        Medida_2 = medida_2;
    }

    public double getMetragem_unit() {
        return Metragem_unit;
    }

    public void setMetragem_unit(double metragem_unit) {
        Metragem_unit = metragem_unit;
    }

    public int getQtdade() {
        return Qtdade;
    }

    public void setQtdade(int qtdade) {
        Qtdade = qtdade;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
