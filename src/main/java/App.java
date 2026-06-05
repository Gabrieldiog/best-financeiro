import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import DAO.CreateDAO;
import DAO.DeleteDAO;
import DAO.ReadDAO;
import DAO.RelatorioDAO;
import DAO.UpdateDAO;
import DAO.UsuarioDAO;
import model.AnaliseFinanceira;
import model.Projecao;
import model.Read;
import model.Sugestao;
import model.Usuario;
import service.Financas;
import utils.CancelamentoException;
import utils.Conexao;
import utils.DataUtil;
import utils.ExportadorCSV;
import utils.Moeda;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final CreateDAO createDAO = new CreateDAO();
    private static final ReadDAO readDAO = new ReadDAO();
    private static final UpdateDAO updateDAO = new UpdateDAO();
    private static final DeleteDAO deleteDAO = new DeleteDAO();
    private static final RelatorioDAO relatorioDAO = new RelatorioDAO();
    private static final Financas financas = new Financas();

    private static final String[] CATEGORIAS_DESPESA = {
            "Alimentacao", "Transporte", "Moradia", "Saude", "Educacao", "Lazer", "Contas", "Outros"
    };
    private static final String[] CATEGORIAS_RECEITA = {
            "Salario", "Investimento", "Freelance", "Presente", "Outros"
    };

    public static void main(String[] args) {
        Conexao.criarTabela();

        int opcao = 0;
        while (opcao != 3) {
            System.out.println("\n--------- SISTEMA FINANCEIRO ---------");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Login");
            System.out.println("3 - Sair");
            opcao = lerOpcao("Escolha uma opcao: ");

            switch (opcao) {
                case 1:
                    cadastrar();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Saindo do sistema, ate mais!");
                    break;
                default:
                    System.out.println("Opcao invalida");
            }
        }
        scanner.close();
    }

    private static void cadastrar() {
        System.out.println("\n---------- CADASTRO ----------");

        String username;
        while (true) {
            System.out.print("Digite o username: ");
            username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("Username nao pode ser vazio!");
                continue;
            }
            if (usuarioDAO.usernameExiste(username)) {
                System.out.println("Esse username ja esta em uso, pode tentar outro ai...");
                continue;
            }
            break;
        }

        String senha;
        while (true) {
            System.out.print("Digite a senha: ");
            senha = scanner.nextLine();
            String erroSenha = validarSenha(senha);
            if (erroSenha != null) {
                System.out.println(erroSenha);
                continue;
            }
            System.out.print("Confirme a senha: ");
            String confirmacao = scanner.nextLine();
            if (!senha.equals(confirmacao)) {
                System.out.println("As senhas nao coincidem, confirma ai direito...");
                continue;
            }
            break;
        }

        Usuario usuario = new Usuario(username, senha);
        if (usuarioDAO.cadastrar(usuario)) {
            System.out.println("Cadastro realizado com sucesso, pode logar!");
        } else {
            System.out.println("Erro ao cadastrar, verifique os dados");
        }
    }

    private static void login() {
        System.out.println("\n---------- LOGIN ----------");
        System.out.print("Digite o username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        Usuario usuario = usuarioDAO.login(username, senha);
        if (usuario != null) {
            System.out.println("Login realizado com sucesso! Bem vindo, " + usuario.getUsername());
            menuUsuario(usuario);
        } else {
            System.out.println("Username ou senha incorretos!");
        }
    }

    private static void menuUsuario(Usuario usuario) {
        int opcao = 0;
        while (opcao != 10) {
            System.out.println("\n===== MENU (" + usuario.getUsername() + ") =====");
            System.out.println("1 - Registrar movimentacao");
            System.out.println("2 - Listar movimentacoes");
            System.out.println("3 - Editar movimentacao");
            System.out.println("4 - Excluir movimentacao");
            System.out.println("5 - Ver saldo");
            System.out.println("6 - Relatorios");
            System.out.println("7 - Projecao de gastos do mes");
            System.out.println("8 - Sugestao de economia");
            System.out.println("9 - Exportar CSV");
            System.out.println("10 - Logout");
            opcao = lerOpcao("Escolha uma opcao: ");

            switch (opcao) {
                case 1:
                    registrarMovimentacao(usuario);
                    break;
                case 2:
                    listarMovimentacoes(usuario);
                    break;
                case 3:
                    editarMovimentacao(usuario);
                    break;
                case 4:
                    excluirMovimentacao(usuario);
                    break;
                case 5:
                    verSaldo(usuario);
                    break;
                case 6:
                    verRelatorios(usuario);
                    break;
                case 7:
                    verProjecao(usuario);
                    break;
                case 8:
                    verSugestao(usuario);
                    break;
                case 9:
                    exportarCSV(usuario);
                    break;
                case 10:
                    System.out.println("Deslogando...");
                    break;
                default:
                    System.out.println("Opcao invalida");
            }
        }
    }

    private static void registrarMovimentacao(Usuario usuario) {
        System.out.println("\n---------- NOVA MOVIMENTACAO ----------");
        System.out.println("(digite 0 em qualquer campo pra cancelar)");
        try {
            String tipo = escolherTipo();
            double valor = lerValorPositivo("Valor de uma vez (R$): ");
            int frequencia = lerFrequencia("Quantas vezes por mes isso acontece? (Enter = 1): ");
            String categoria = escolherCategoria(tipo, usuario.getId());
            String descricao = lerTextoCancelavel("Descricao: ");
            String data = lerData();

            Read movimentacao = new Read();
            movimentacao.setIdUsuario(usuario.getId());
            movimentacao.setTipo(tipo);
            movimentacao.setValor(valor);
            movimentacao.setFrequencia(frequencia);
            movimentacao.setDescricao(descricao);
            movimentacao.setCategoria(categoria);
            movimentacao.setDataMovimentacao(data);

            if (createDAO.registrar(movimentacao)) {
                System.out.println("Movimentacao registrada com sucesso!");
            } else {
                System.out.println("Erro ao registrar movimentacao");
            }
        } catch (CancelamentoException e) {
            System.out.println("Operacao cancelada, voltando pro menu.");
        }
    }

    private static void listarMovimentacoes(Usuario usuario) {
        System.out.println("\n---------- MINHAS MOVIMENTACOES ----------");
        List<Read> lista = readDAO.listarMovimentacoes(usuario.getId());
        if (lista.isEmpty()) {
            System.out.println("Nenhuma movimentacao registrada ainda.");
            return;
        }
        for (Read m : lista) {
            imprimirMovimentacao(m);
        }
    }

    private static void editarMovimentacao(Usuario usuario) {
        System.out.println("\n---------- EDITAR MOVIMENTACAO ----------");
        List<Read> lista = readDAO.listarMovimentacoes(usuario.getId());
        if (lista.isEmpty()) {
            System.out.println("Voce nao tem movimentacoes pra editar.");
            return;
        }
        for (Read m : lista) {
            imprimirMovimentacao(m);
        }

        try {
            int id = lerNumeroCancelavel("Digite o id da movimentacao que quer editar (0 pra cancelar): ");
            Read m = readDAO.buscarPorId(id, usuario.getId());
            if (m == null) {
                System.out.println("Movimentacao nao encontrada.");
                return;
            }

            while (true) {
                System.out.println("\nEditando a movimentacao #" + m.getId() + ":");
                System.out.println("  Tipo:       " + m.getTipo());
                System.out.println("  Valor:      " + Moeda.formatar(m.getValor()));
                System.out.println("  Vezes/mes:  " + m.getFrequencia() + " (total " + Moeda.formatar(m.getValorMensal()) + "/mes)");
                System.out.println("  Categoria:  " + m.getCategoria());
                System.out.println("  Descricao:  " + descricaoOuVazio(m.getDescricao()));
                System.out.println("  Data:       " + DataUtil.paraExibicao(m.getDataMovimentacao()));
                System.out.println("\nO que voce quer mudar?");
                System.out.println("1 - Valor");
                System.out.println("2 - Quantas vezes por mes");
                System.out.println("3 - Tipo (vai pedir a categoria de novo)");
                System.out.println("4 - Categoria");
                System.out.println("5 - Descricao");
                System.out.println("6 - Data");
                System.out.println("7 - Salvar e voltar");
                System.out.println("0 - Cancelar sem salvar");
                int op = lerOpcao("Escolha: ");

                if (op == 1) {
                    System.out.println("Valor atual: " + Moeda.formatar(m.getValor()));
                    m.setValor(lerValorPositivo("Novo valor (R$): "));
                } else if (op == 2) {
                    System.out.println("Vezes por mes atual: " + m.getFrequencia());
                    m.setFrequencia(lerFrequencia("Novo quantas vezes por mes (Enter = 1): "));
                } else if (op == 3) {
                    System.out.println("Tipo atual: " + m.getTipo());
                    String novoTipo = escolherTipo();
                    m.setTipo(novoTipo);
                    m.setCategoria(escolherCategoria(novoTipo, usuario.getId()));
                } else if (op == 4) {
                    System.out.println("Categoria atual: " + m.getCategoria());
                    m.setCategoria(escolherCategoria(m.getTipo(), usuario.getId()));
                } else if (op == 5) {
                    System.out.println("Descricao atual: " + descricaoOuVazio(m.getDescricao()));
                    m.setDescricao(lerTextoCancelavel("Nova descricao: "));
                } else if (op == 6) {
                    System.out.println("Data atual: " + DataUtil.paraExibicao(m.getDataMovimentacao()));
                    m.setDataMovimentacao(lerData());
                } else if (op == 7) {
                    if (updateDAO.atualizar(m)) {
                        System.out.println("Movimentacao atualizada com sucesso!");
                    } else {
                        System.out.println("Erro ao atualizar movimentacao");
                    }
                    return;
                } else if (op == 0) {
                    System.out.println("Edicao cancelada, nada foi salvo.");
                    return;
                } else {
                    System.out.println("Opcao invalida");
                }
            }
        } catch (CancelamentoException e) {
            System.out.println("Edicao cancelada, voltando pro menu.");
        }
    }

    private static String descricaoOuVazio(String descricao) {
        return (descricao == null || descricao.isEmpty()) ? "(sem descricao)" : descricao;
    }

    private static void excluirMovimentacao(Usuario usuario) {
        System.out.println("\n---------- EXCLUIR MOVIMENTACAO ----------");
        List<Read> lista = readDAO.listarMovimentacoes(usuario.getId());
        if (lista.isEmpty()) {
            System.out.println("Voce nao tem movimentacoes pra excluir.");
            return;
        }
        for (Read m : lista) {
            imprimirMovimentacao(m);
        }

        try {
            int id = lerNumeroCancelavel("Digite o id da movimentacao que quer excluir (0 pra cancelar): ");
            Read atual = readDAO.buscarPorId(id, usuario.getId());
            if (atual == null) {
                System.out.println("Movimentacao nao encontrada.");
                return;
            }

            System.out.print("Tem certeza que quer excluir? (s/n): ");
            String confirma = scanner.nextLine().trim().toLowerCase();
            if (!confirma.equals("s")) {
                System.out.println("Exclusao cancelada.");
                return;
            }

            if (deleteDAO.excluir(id, usuario.getId())) {
                System.out.println("Movimentacao excluida com sucesso!");
            } else {
                System.out.println("Erro ao excluir movimentacao");
            }
        } catch (CancelamentoException e) {
            System.out.println("Exclusao cancelada, voltando pro menu.");
        }
    }

    private static void verSaldo(Usuario usuario) {
        System.out.println("\n---------- SALDO ----------");
        double receitas = relatorioDAO.getTotalReceitas(usuario.getId());
        double despesas = relatorioDAO.getTotalDespesas(usuario.getId());
        double saldo = receitas - despesas;
        System.out.println("Total de receitas: " + Moeda.formatar(receitas));
        System.out.println("Total de despesas: " + Moeda.formatar(despesas));
        System.out.println("Saldo atual: " + Moeda.formatar(saldo));
    }

    private static void verRelatorios(Usuario usuario) {
        System.out.println("\n---------- RELATORIOS ----------");
        System.out.println("(valores que voce realmente registrou)");
        double receitas = relatorioDAO.getTotalReceitas(usuario.getId());
        double despesas = relatorioDAO.getTotalDespesas(usuario.getId());
        System.out.println("Total de receitas: " + Moeda.formatar(receitas));
        System.out.println("Total de despesas: " + Moeda.formatar(despesas));
        System.out.println("Saldo: " + Moeda.formatar(receitas - despesas));

        System.out.println("\nGastos por categoria:");
        Map<String, Double> gastos = relatorioDAO.getGastosPorCategoria(usuario.getId());
        if (gastos.isEmpty()) {
            System.out.println("Nenhuma despesa registrada ainda.");
            return;
        }
        for (Map.Entry<String, Double> entrada : gastos.entrySet()) {
            double percentual = despesas > 0 ? (entrada.getValue() / despesas) * 100 : 0;
            System.out.println("- " + entrada.getKey() + ": " + Moeda.formatar(entrada.getValue())
                    + " (" + String.format("%.1f", percentual) + "%)");
        }
    }

    private static void verProjecao(Usuario usuario) {
        System.out.println("\n---------- PROJECAO DE GASTOS DO MES ----------");
        Projecao p = financas.projetar(usuario.getId());

        if (p.getDespesas().isEmpty()) {
            System.out.println("Voce ainda nao registrou despesas esse mes, entao nao da pra projetar nada.");
            return;
        }

        System.out.println("Suas despesas do mes (valor x quantas vezes acontece no mes):");
        for (Read d : p.getDespesas()) {
            String desc = d.getDescricao();
            String rotulo = (desc == null || desc.isEmpty()) ? d.getCategoria() : desc + " (" + d.getCategoria() + ")";
            System.out.println("- " + rotulo + ": " + Moeda.formatar(d.getValor()) + " x " + d.getFrequencia()
                    + " = " + Moeda.formatar(d.getValorMensal()) + "/mes");
        }

        System.out.println();
        System.out.println(">> Gasto mensal previsto: " + Moeda.formatar(p.getGastoMensalPrevisto()));
        if (p.getRendaMensal() > 0) {
            System.out.println(">> Sua renda mensal: " + Moeda.formatar(p.getRendaMensal()));
            double saldo = p.getSaldoPrevisto();
            if (saldo >= 0) {
                System.out.println(">> No fim do mes deve sobrar " + Moeda.formatar(saldo) + ". Mandou bem!");
            } else {
                System.out.println(">> No fim do mes vai faltar " + Moeda.formatar(-saldo) + ". Cuidado!");
            }
        } else {
            System.out.println("(cadastre suas receitas pra eu comparar com a sua renda)");
        }
    }

    private static void verSugestao(Usuario usuario) {
        System.out.println("\n---------- ANALISE FINANCEIRA E SUGESTOES ----------");
        System.out.println("(considera suas despesas vezes quantas vezes acontecem no mes)");
        AnaliseFinanceira a = financas.analisar(usuario.getId());

        System.out.println("Renda do mes:   " + Moeda.formatar(a.getReceitaMes()));
        System.out.println("Gastos do mes:  " + Moeda.formatar(a.getDespesaMes()));
        System.out.println("Sobra do mes:   " + Moeda.formatar(a.getSaldoMes()));
        if (a.getReceitaMes() > 0) {
            System.out.println("Voce gasta " + String.format("%.1f", a.getTaxaComprometimento())
                    + "% da sua renda e poupa " + String.format("%.1f", a.getTaxaPoupanca()) + "%.");
        }
        System.out.println("\nDiagnostico: " + a.getDiagnostico());

        if (a.getSugestoes().isEmpty()) {
            System.out.println("\nNao tenho gastos suficientes esse mes pra sugerir cortes. Registre mais movimentacoes.");
            return;
        }

        System.out.println("\nOnde da pra economizar:");
        int i = 1;
        for (Sugestao s : a.getSugestoes()) {
            System.out.println(i + ") " + s.getCategoria() + " (" + s.getClassificacao() + ") - hoje "
                    + Moeda.formatar(s.getValorAtual()));
            System.out.println("   Cortando " + s.getPercentualCorte() + "% voce economiza "
                    + Moeda.formatar(s.getEconomiaMensal()) + "/mes ("
                    + Moeda.formatar(s.getEconomiaAnual()) + "/ano)");
            i++;
        }

        System.out.println("\n>> Potencial total de economia: " + Moeda.formatar(a.getPotencialMensal())
                + " por mes, ou " + Moeda.formatar(a.getPotencialAnual()) + " no ano!");
    }

    private static void exportarCSV(Usuario usuario) {
        System.out.println("\n---------- EXPORTAR CSV ----------");
        List<Read> lista = readDAO.listarMovimentacoes(usuario.getId());
        if (lista.isEmpty()) {
            System.out.println("Nao tem nada pra exportar ainda.");
            return;
        }
        String arquivo = "excel/extrato_" + usuario.getUsername() + "_" + DataUtil.hojeParaArquivo() + ".csv";
        if (ExportadorCSV.exportar(lista, arquivo)) {
            System.out.println("Exportado com sucesso pro arquivo: " + arquivo);
        } else {
            System.out.println("Erro ao exportar CSV");
        }
    }

    private static void imprimirMovimentacao(Read m) {
        String valor = m.getFrequencia() > 1
                ? Moeda.formatar(m.getValor()) + " x" + m.getFrequencia() + " = " + Moeda.formatar(m.getValorMensal()) + "/mes"
                : Moeda.formatar(m.getValor());
        System.out.println("#" + m.getId() + " | " + DataUtil.paraExibicao(m.getDataMovimentacao())
                + " | " + m.getTipo() + " | " + m.getCategoria()
                + " | " + valor
                + (m.getDescricao() == null || m.getDescricao().isEmpty() ? "" : " | " + m.getDescricao()));
    }

    private static int lerFrequencia(String prompt) {
        while (true) {
            System.out.print(prompt);
            String entrada = scanner.nextLine().trim();
            if (entrada.equals("0")) {
                throw new CancelamentoException();
            }
            if (entrada.isEmpty()) {
                return 1;
            }
            try {
                int freq = Integer.parseInt(entrada);
                if (freq < 1) {
                    System.out.println("Tem que ser pelo menos 1 vez");
                    continue;
                }
                return freq;
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro");
            }
        }
    }

    private static String escolherTipo() {
        while (true) {
            System.out.println("Tipo: 1 - Receita | 2 - Despesa  (0 pra cancelar)");
            int opcao = lerNumeroCancelavel("Escolha: ");
            if (opcao == 1) {
                return "RECEITA";
            }
            if (opcao == 2) {
                return "DESPESA";
            }
            System.out.println("Tipo invalido, escolhe 1 ou 2");
        }
    }

    private static String escolherCategoria(String tipo, int idUsuario) {
        String[] fixas = tipo.equals("RECEITA") ? CATEGORIAS_RECEITA : CATEGORIAS_DESPESA;
        List<String> categorias = new ArrayList<>(Arrays.asList(fixas));
        for (String usada : relatorioDAO.getCategoriasUsadas(idUsuario, tipo)) {
            if (!categorias.contains(usada)) {
                categorias.add(usada);
            }
        }
        while (true) {
            System.out.println("Categorias:  (0 pra cancelar)");
            for (int i = 0; i < categorias.size(); i++) {
                System.out.println((i + 1) + " - " + categorias.get(i));
            }
            int novaOpcao = categorias.size() + 1;
            System.out.println(novaOpcao + " - Criar nova categoria");
            int opcao = lerNumeroCancelavel("Escolha a categoria: ");
            if (opcao >= 1 && opcao <= categorias.size()) {
                return categorias.get(opcao - 1);
            }
            if (opcao == novaOpcao) {
                String nova = lerTextoCancelavel("Nome da nova categoria: ");
                if (!nova.isEmpty()) {
                    return nova;
                }
                System.out.println("Nome vazio, tenta de novo");
                continue;
            }
            System.out.println("Categoria invalida, tenta de novo");
        }
    }

    private static double lerValorPositivo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String entrada = scanner.nextLine().trim();
            if (entrada.equals("0")) {
                throw new CancelamentoException();
            }
            try {
                double valor = Double.parseDouble(entrada.replace(",", "."));
                if (valor <= 0) {
                    System.out.println("O valor tem que ser maior que zero");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido, digite um numero");
            }
        }
    }

    private static String lerData() {
        while (true) {
            System.out.print("Data (dd/mm/aaaa), Enter pra hoje ou 0 pra cancelar: ");
            String entrada = scanner.nextLine().trim();
            if (entrada.equals("0")) {
                throw new CancelamentoException();
            }
            if (entrada.isEmpty()) {
                return DataUtil.hoje();
            }
            String convertida = DataUtil.paraBanco(entrada);
            if (convertida != null) {
                return convertida;
            }
            System.out.println("Data invalida, usa o formato dd/mm/aaaa");
        }
    }

    private static int lerNumeroCancelavel(String prompt) {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        if (entrada.equals("0")) {
            throw new CancelamentoException();
        }
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String lerTextoCancelavel(String prompt) {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        if (entrada.equals("0")) {
            throw new CancelamentoException();
        }
        return entrada;
    }

    private static int lerOpcao(String prompt) {
        System.out.print(prompt);
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String validarSenha(String senha) {
        if (senha.length() < 6) {
            return "A senha deve ter no minimo 6 caracteres!";
        }

        if (senha.equals("123456")) {
            return "A senha nao pode ser 123456!";
        }

        boolean temMaiuscula = false;
        for (char c : senha.toCharArray()) {
            if (Character.isUpperCase(c)) {
                temMaiuscula = true;
                break;
            }
        }
        if (!temMaiuscula) {
            return "A senha deve conter pelo menos uma letra maiuscula!";
        }

        String especiais = ")(/&%$";
        boolean temEspecial = false;
        for (char c : senha.toCharArray()) {
            if (especiais.indexOf(c) != -1) {
                temEspecial = true;
                break;
            }
        }
        if (!temEspecial) {
            return "A senha deve conter pelo menos um caractere especial: ) ( / & % $";
        }

        return null;
    }
}
