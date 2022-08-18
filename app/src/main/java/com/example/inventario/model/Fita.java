package com.example.inventario.model;

public class Fita {
    private int Id;
    private int Codigo;
    private double Espessura;
    private String Descricao;
    private  int MaiorMedidaRolo;
    private  int MenorMedidaRolo;
    private  double Metragem;

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public int getCodigo() {
        return Codigo;
    }

    public void setCodigo(int codigo) {
        Codigo = codigo;
    }

    public double getEspessura() {
        return Espessura;
    }

    public void setEspessura(double espessura) {
        Espessura = espessura;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setCor(String descricao) {
        Descricao = descricao;
    }

    public int getMaiorMedidaRolo() {
        return MaiorMedidaRolo;
    }

    public void setMaiorMedidaRolo(int maiorMedidaRolo) throws Exception {
        if(maiorMedidaRolo > 500){
            throw new Exception("A maior medida do rolo não pode ser maior do que 500mm.");
        }
        MaiorMedidaRolo = maiorMedidaRolo;
    }

    public int getMenorMedidaRolo() {
        return MenorMedidaRolo;
    }

    public void setMenorMedidaRolo(int menorMedidaRolo) throws Exception {
        if (menorMedidaRolo > 200){
            throw  new Exception("A menor medida do rolo não pode ser maior do que 200mm.");
        }
        MenorMedidaRolo = menorMedidaRolo;
    }

    public double getMetragem() {
        return Metragem;
    }

    public void setMetragem(double metragem) {
        Metragem = metragem;
    }

    /*public double getMetragem() throws Exception{
        if (MaiorMedidaRolo < MenorMedidaRolo){
            throw  new Exception("A medida maior deve ser informada primeiro.");
        }else if(MenorMedidaRolo * 2 >= MaiorMedidaRolo){
            throw  new Exception("Uma das medidas está errada.");
        }
        Metragem = (((((MaiorMedidaRolo-MenorMedidaRolo)*3.14*(MenorMedidaRolo/Espessura))/1000)+((((Espessura-MenorMedidaRolo)*3.14*(MenorMedidaRolo/Espessura))/1000)*9/100)));
        return Metragem;
    }*/







}
