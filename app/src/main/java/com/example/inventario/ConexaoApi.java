package com.example.inventario;

public class ConexaoApi {

    public String Status;
    public String Mensagem;
    public String UsuarioId;
    public String Token;
    public String NomeUsuario;
    public  String Senha;
    public String IdGerado;
    public String uri;

    public void setStatus(String status) {
        Status = status;
    }

    public void setMensagem(String mensagem) {
        Mensagem = mensagem;
    }

    public void setUsuarioId(String usuarioId) {
        UsuarioId = usuarioId;
    }

    public void setToken(String token) {
        Token = token;
    }

    public void setNomeUsuario(String nomeUsuario) {
        NomeUsuario = nomeUsuario;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public void setIdGerado(String idGerado) {
        IdGerado = idGerado;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getStatus() {
        return Status;
    }

    public String getMensagem() {
        return Mensagem;
    }

    public String getUsuarioId() {
        return UsuarioId;
    }

    public String getToken() {
        return Token;
    }

    public String getNomeUsuario() {
        return NomeUsuario;
    }

    public String getSenha() {
        return Senha;
    }

    public String getIdGerado() {
        return IdGerado;
    }

    public String getUri() {
        //return "http://192.168.0.206/Inventario/public/api/";
        //return "https://dentoid-loops.000webhostapp.com/api/";
        return "http://189.23.221.116:38877/Inventario/public/api/";
    }






}
