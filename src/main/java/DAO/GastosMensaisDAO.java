package DAO;

import utils.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GastosMensaisDAO {

    public double getDespesasDoMes(int idUsuario, String anoMes) {
        return getTotalDoMes(idUsuario, "DESPESA", anoMes);
    }

    public double getReceitasDoMes(int idUsuario, String anoMes) {
        return getTotalDoMes(idUsuario, "RECEITA", anoMes);
    }

    public int getDiasComMovimentacao(int idUsuario, String anoMes) {
        String sql = "SELECT COUNT(DISTINCT data_movimentacao) AS dias FROM movimentacoes "
                + "WHERE id_usuario = ? AND strftime('%Y-%m', data_movimentacao) = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, anoMes);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("dias");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private double getTotalDoMes(int idUsuario, String tipo, String anoMes) {
        String sql = "SELECT COALESCE(SUM(valor * frequencia), 0) AS total FROM movimentacoes "
                + "WHERE id_usuario = ? AND tipo = ? "
                + "AND strftime('%Y-%m', data_movimentacao) = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, tipo);
            ps.setString(3, anoMes);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
