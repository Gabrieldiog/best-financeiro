package DAO;

import utils.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RelatorioDAO {

    public double getTotalReceitas(int idUsuario) {
        return somarPorTipo(idUsuario, "RECEITA");
    }

    public double getTotalDespesas(int idUsuario) {
        return somarPorTipo(idUsuario, "DESPESA");
    }

    public double getSaldo(int idUsuario) {
        return getTotalReceitas(idUsuario) - getTotalDespesas(idUsuario);
    }

    private double somarPorTipo(int idUsuario, String tipo) {
        String sql = "SELECT COALESCE(SUM(valor), 0) AS total FROM movimentacoes "
                + "WHERE id_usuario = ? AND tipo = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, tipo);
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

    public Map<String, Double> getGastosPorCategoria(int idUsuario) {
        return gastosPorCategoria(idUsuario, null);
    }

    public Map<String, Double> getGastosPorCategoriaMes(int idUsuario, String anoMes) {
        return gastosPorCategoria(idUsuario, anoMes);
    }

    private Map<String, Double> gastosPorCategoria(int idUsuario, String anoMes) {
        String soma = anoMes != null ? "SUM(valor * frequencia)" : "SUM(valor)";
        String sql = "SELECT categoria, " + soma + " AS total FROM movimentacoes "
                + "WHERE id_usuario = ? AND tipo = 'DESPESA' "
                + (anoMes != null ? "AND strftime('%Y-%m', data_movimentacao) = ? " : "")
                + "GROUP BY categoria ORDER BY total DESC";
        Map<String, Double> gastos = new LinkedHashMap<>();
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            if (anoMes != null) {
                ps.setString(2, anoMes);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    gastos.put(rs.getString("categoria"), rs.getDouble("total"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gastos;
    }

    public List<String> getCategoriasUsadas(int idUsuario, String tipo) {
        String sql = "SELECT DISTINCT categoria FROM movimentacoes "
                + "WHERE id_usuario = ? AND tipo = ? ORDER BY categoria";
        List<String> categorias = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categorias.add(rs.getString("categoria"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }
}
