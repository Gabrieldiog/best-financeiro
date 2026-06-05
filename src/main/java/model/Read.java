package model;

public class Read {
    private int id;
    private int idUsuario;
    private String tipo;
    private double valor;
    private String descricao;
    private String dataMovimentacao;
    private String categoria;
    private int frequencia = 1;


    public Read() {
    }


    public Read(int id, int idUsuario, String tipo, double valor, String descricao, String dataMovimentacao, String categoria) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.dataMovimentacao = dataMovimentacao;
        this.categoria = categoria;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public double getValorMensal() {
        return valor * frequencia;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(String dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
