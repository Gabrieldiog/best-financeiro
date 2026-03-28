package model;

public class Crud {
    private int id;
    private int usuario_id;
    private String tipo;
    private double valor;
    private String data;
    private String descricao;
    private String categoria;

    public Crud(){}

    public Crud(int usuario_id, String tipo, double valor, String data, String descricao, String categoria){
        this.usuario_id = usuario_id;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getUsuarioId(){
        return usuario_id;
    }

    public void setUsuarioId(int usuario_id){
        this.usuario_id = usuario_id;
    }

    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public double getValor(){
        return valor;
    }

    public void setValor(double valor){
        this.valor = valor;
    }

    public String getData(){
        return data;
    }

    public void setData(String data){
        this.data = data;
    }

    public String getDescricao(){
        return descricao;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public String getCategoria(){
        return categoria;
    }

    public void setCategoria(String categoria){
        this.categoria = categoria;
    }
}
