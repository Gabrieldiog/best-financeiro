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
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "senha TEXT NOT NULL)";
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            String sqlTransacoes = "CREATE TABLE IF NOT EXISTS transacoes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "usuario_id INTEGER NOT NULL, "
                + "tipo TEXT NOT NULL, "
                + "valor REAL NOT NULL, "
                + "data TEXT NOT NULL, "
                + "descricao TEXT NOT NULL, "
                + "categoria TEXT NOT NULL, "
                + "FOREIGN KEY (usuario_id) REFERENCES usuarios(id))";
            stmt.execute(sqlTransacoes);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
