package DAO;

import java.sql.*;

public class GastosMensaisDAO {

    private Connection conn;

    public GastosMensaisDAO() {}

    public double calcularGastosMensais(int clienteId, int mes, int ano) throws SQLException {
        String sql = "SELECT SUM(ABS(valor)) AS total " +
                     "FROM transacoes " +
                     "WHERE cliente_id = ? AND valor < 0 " +
                     "AND MONTH(data) = ? AND YEAR(data) = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            stmt.setInt(2, mes);
            stmt.setInt(3, ano);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        }
        return 0.0;
    }
}
    
