import java.util.Scanner;
import DAO.UsuarioDAO;
import model.Usuario;
import utils.Conexao;

public class App {

    private static Scanner scanner = new Scanner(System.in);
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();

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
            if (usuarioDAO.login(username, senha)) {
                System.out.println("Login realizado com sucesso!");
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
}
