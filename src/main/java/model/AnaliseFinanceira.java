package model;

import java.util.List;

public class AnaliseFinanceira {

    private final double receitaMes;
    private final double despesaMes;
    private final double saldoMes;
    private final double taxaComprometimento;
    private final double taxaPoupanca;
    private final String diagnostico;
    private final List<Sugestao> sugestoes;
    private final double potencialMensal;
    private final double potencialAnual;

    public AnaliseFinanceira(double receitaMes, double despesaMes, double taxaComprometimento,
                             double taxaPoupanca, String diagnostico, List<Sugestao> sugestoes,
                             double potencialMensal, double potencialAnual) {
        this.receitaMes = receitaMes;
        this.despesaMes = despesaMes;
        this.saldoMes = receitaMes - despesaMes;
        this.taxaComprometimento = taxaComprometimento;
        this.taxaPoupanca = taxaPoupanca;
        this.diagnostico = diagnostico;
        this.sugestoes = sugestoes;
        this.potencialMensal = potencialMensal;
        this.potencialAnual = potencialAnual;
    }

    public double getReceitaMes() {
        return receitaMes;
    }

    public double getDespesaMes() {
        return despesaMes;
    }

    public double getSaldoMes() {
        return saldoMes;
    }

    public double getTaxaComprometimento() {
        return taxaComprometimento;
    }

    public double getTaxaPoupanca() {
        return taxaPoupanca;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public List<Sugestao> getSugestoes() {
        return sugestoes;
    }

    public double getPotencialMensal() {
        return potencialMensal;
    }

    public double getPotencialAnual() {
        return potencialAnual;
    }
}
