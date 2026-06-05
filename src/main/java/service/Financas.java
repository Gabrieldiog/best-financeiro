package service;

import DAO.GastosMensaisDAO;
import DAO.ReadDAO;
import DAO.RelatorioDAO;
import model.AnaliseFinanceira;
import model.Projecao;
import model.Read;
import model.Sugestao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Financas {

    private final RelatorioDAO relatorioDAO = new RelatorioDAO();
    private final GastosMensaisDAO gastosMensaisDAO = new GastosMensaisDAO();
    private final ReadDAO readDAO = new ReadDAO();

    private static final List<String> ESSENCIAIS = Arrays.asList("Moradia", "Saude", "Educacao", "Contas");
    private static final int CORTE_ESSENCIAL = 10;
    private static final int CORTE_FLEXIVEL = 20;

    private String anoMesAtual() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public Projecao projetar(int idUsuario) {
        String anoMes = anoMesAtual();
        double rendaMensal = gastosMensaisDAO.getReceitasDoMes(idUsuario, anoMes);
        double gastoMensalPrevisto = gastosMensaisDAO.getDespesasDoMes(idUsuario, anoMes);
        List<Read> despesas = readDAO.listarDespesasDoMes(idUsuario, anoMes);
        return new Projecao(rendaMensal, gastoMensalPrevisto, despesas);
    }

    public AnaliseFinanceira analisar(int idUsuario) {
        String anoMes = anoMesAtual();
        double receitaMes = gastosMensaisDAO.getReceitasDoMes(idUsuario, anoMes);
        double despesaMes = gastosMensaisDAO.getDespesasDoMes(idUsuario, anoMes);

        double taxaComprometimento = receitaMes > 0 ? (despesaMes / receitaMes) * 100 : 0;
        double taxaPoupanca = receitaMes > 0 ? ((receitaMes - despesaMes) / receitaMes) * 100 : 0;
        String diagnostico = montarDiagnostico(receitaMes, despesaMes, taxaComprometimento);

        Map<String, Double> gastos = relatorioDAO.getGastosPorCategoriaMes(idUsuario, anoMes);
        List<Sugestao> sugestoes = new ArrayList<>();
        double potencialMensal = 0;

        for (Map.Entry<String, Double> entrada : gastos.entrySet()) {
            String categoria = entrada.getKey();
            double valor = entrada.getValue();
            double peso = despesaMes > 0 ? (valor / despesaMes) * 100 : 0;
            if (peso < 5) {
                continue;
            }
            boolean essencial = ESSENCIAIS.contains(categoria);
            int corte = essencial ? CORTE_ESSENCIAL : CORTE_FLEXIVEL;
            double economiaMensal = valor * corte / 100.0;
            double economiaAnual = economiaMensal * 12;
            potencialMensal += economiaMensal;
            sugestoes.add(new Sugestao(categoria, valor, corte, economiaMensal, economiaAnual,
                    essencial ? "essencial" : "flexivel"));
        }

        return new AnaliseFinanceira(receitaMes, despesaMes, taxaComprometimento, taxaPoupanca,
                diagnostico, sugestoes, potencialMensal, potencialMensal * 12);
    }

    private String montarDiagnostico(double receita, double despesa, double taxa) {
        if (receita == 0 && despesa == 0) {
            return "Voce ainda nao registrou nada esse mes. Cadastre suas receitas e despesas pra eu te analisar.";
        }
        if (receita == 0) {
            return "Voce registrou despesas mas nenhuma receita esse mes. Cadastre suas entradas pra eu calcular sua saude financeira.";
        }
        if (despesa > receita) {
            return "ALERTA: voce gastou mais do que ganhou esse mes. Esta no vermelho, precisa cortar gastos com urgencia.";
        }
        if (taxa <= 50) {
            return "Otimo! Voce comprometeu so " + arredondar(taxa) + "% da sua renda. Esta poupando bem, continue assim.";
        }
        if (taxa <= 70) {
            return "Saudavel. Voce usou " + arredondar(taxa) + "% da sua renda. Da pra apertar um pouco e poupar mais.";
        }
        if (taxa <= 90) {
            return "Atencao: " + arredondar(taxa) + "% da sua renda ja foi em gastos. Cuidado pra nao estourar o orcamento.";
        }
        return "Risco alto: quase toda sua renda (" + arredondar(taxa) + "%) esta comprometida. Reveja seus gastos.";
    }

    private String arredondar(double valor) {
        return String.format("%.1f", valor);
    }
}
