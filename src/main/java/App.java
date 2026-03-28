import java.util.Scanner;
import DAO.UsuarioDAO;
import model.Usuario;
import model.Crud;
import DAO.CrudDAO;
import utils.Conexao;

public class App {

    private static Scanner scanner = new Scanner(System.in);
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static CrudDAO crudDAO = new CrudDAO();

    public static void main(String[] args) {
        Conexao.criarTabela();

        int opcao = 0;
        while (opcao != 3) {
            System.out.println("\n--------- SISTEMA FINANCEIRO ---------");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Login");
            System.out.println("3 - Sair");
            System.out.print("Escolha uma opcao: ");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("Opcao invalida");
                continue;
            }

            switch (opcao) {
                case 1:
                    cadastrar();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Saindo do sistema");
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
                System.out.println("Esse username ja esta em uso, pode tentar outra ai em...");
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

        while (true) {
            System.out.print("Digite o username: ");
            String username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("Username nao pode ser vazio!");
                continue;
            }
            System.out.print("Digite a senha: ");
            String senha = scanner.nextLine();
            int userId = usuarioDAO.login(username, senha);
            if (userId != -1) {
                System.out.println("Login realizado com sucesso!");
                menuPrincipal(userId);
                break;
            } else {
                System.out.println("Username ou senha incorretos!");
            }
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

    public static void menuPrincipal(int userId) {
        int opcao = 0;

        while (opcao != 3){
            System.out.println("\n--------- MENU PRINCIPAL ---------");
            System.out.println("1 - Registar transacao");
            System.out.println("2 - Editar transacao");
            System.out.println("3 - Voltar");
            System.out.print("Escolha uma opcao: ");

            if(scanner.hasNextInt()){
                opcao = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.println("Opcao invalida!");
                continue;
            }

            switch (opcao) {
                case 1:
                    registrarTransacao(userId);
                    break;
                case 2:
                    editarTransacao();
                    break;
                case 3:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opcao invalida");
            }
        }
    }

    private static void registrarTransacao(int userId){
        System.out.println("\n-------- NOVA TRANSACAO --------");

        System.out.print("Tipo (receita/despesa): ");
        String tipo = scanner.nextLine().trim();

        System.out.print("Valor: ");
        double valor;
        try {
            valor = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Valor invalido!");
            return;
        }

        System.out.print("Data (DD/MM/YYYY): ");
        String data = scanner.nextLine().trim();

        System.out.print("Descricao: ");
        String descricao = scanner.nextLine().trim();

        System.out.print("Categoria: ");
        String categoria = scanner.nextLine().trim();

        Crud transacao = new Crud(userId, tipo, valor, data, descricao, categoria);
        if(crudDAO.inserir(transacao)){
            System.out.println("Transacao registrada com sucesso.");
        } else {
            System.out.println("Erro ao registrar transacoes.");
        }
    }

    private static void editarTransacao() {
        System.out.println("\n---------- EDITAR TRANSACAO ----------");

        System.out.print("ID da transacao: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Novo tipo (receita/despesa): ");
        String tipo = scanner.nextLine().trim();

        System.out.print("Novo valor: ");
        double valor;
        try {
            valor = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("Valor invalido!");
            return;
        }

        System.out.print("Nova data (dd/MM/yyyy): ");
        String data = scanner.nextLine().trim();

        System.out.print("Nova descricao: ");
        String descricao = scanner.nextLine().trim();

        System.out.print("Nova categoria: ");
        String categoria = scanner.nextLine().trim();

        Crud transacao = new Crud(0, tipo, valor, data, descricao, categoria);
        transacao.setId(id);
        if (crudDAO.atualizar(transacao)) {
            System.out.println("Transacao atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar transacao.");
        }
    }
}
