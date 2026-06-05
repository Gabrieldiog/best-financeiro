package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    private static final String URL = "jdbc:sqlite:financeiro.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void criarTabela() {
        String sqlUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "senha TEXT NOT NULL)";

        String sqlMovimentacoes = "CREATE TABLE IF NOT EXISTS movimentacoes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_usuario INTEGER NOT NULL,"
                + "tipo TEXT NOT NULL,"
                + "valor REAL NOT NULL,"
                + "descricao TEXT,"
                + "data_movimentacao TEXT NOT NULL,"
                + "categoria TEXT NOT NULL,"
                + "frequencia INTEGER NOT NULL DEFAULT 1,"
                + "FOREIGN KEY (id_usuario) REFERENCES usuarios(id))";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlUsuarios);
            stmt.execute(sqlMovimentacoes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        migrarColunaFrequencia();
    }

    private static void migrarColunaFrequencia() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE movimentacoes ADD COLUMN frequencia INTEGER NOT NULL DEFAULT 1");
        } catch (SQLException e) {
        }
    }
}
