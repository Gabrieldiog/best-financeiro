package model;

public class Sugestao {

    private final String categoria;
    private final double valorAtual;
    private final int percentualCorte;
    private final double economiaMensal;
    private final double economiaAnual;
    private final String classificacao;

    public Sugestao(String categoria, double valorAtual, int percentualCorte,
                    double economiaMensal, double economiaAnual, String classificacao) {
        this.categoria = categoria;
        this.valorAtual = valorAtual;
        this.percentualCorte = percentualCorte;
        this.economiaMensal = economiaMensal;
        this.economiaAnual = economiaAnual;
        this.classificacao = classificacao;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getValorAtual() {
        return valorAtual;
    }

    public int getPercentualCorte() {
        return percentualCorte;
    }

    public double getEconomiaMensal() {
        return economiaMensal;
    }

    public double getEconomiaAnual() {
        return economiaAnual;
    }

    public String getClassificacao() {
        return classificacao;
    }
}
