package model;

import java.util.List;

public class Projecao {

    private final double rendaMensal;
    private final double gastoMensalPrevisto;
    private final List<Read> despesas;

    public Projecao(double rendaMensal, double gastoMensalPrevisto, List<Read> despesas) {
        this.rendaMensal = rendaMensal;
        this.gastoMensalPrevisto = gastoMensalPrevisto;
        this.despesas = despesas;
    }

    public double getRendaMensal() {
        return rendaMensal;
    }

    public double getGastoMensalPrevisto() {
        return gastoMensalPrevisto;
    }

    public List<Read> getDespesas() {
        return despesas;
    }

    public double getSaldoPrevisto() {
        return rendaMensal - gastoMensalPrevisto;
    }
}
