package DAO;

import model.Read;
import utils.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadDAO {

    public List<Read> listarMovimentacoes(int idUsuario) {
        String sql = "SELECT id, id_usuario, tipo, valor, descricao, data_movimentacao, categoria, frequencia "
                + "FROM movimentacoes WHERE id_usuario = ? "
                + "ORDER BY data_movimentacao DESC, id DESC";

        List<Read> extrato = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    extrato.add(montar(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return extrato;
    }

    public List<Read> listarDespesasDoMes(int idUsuario, String anoMes) {
        String sql = "SELECT id, id_usuario, tipo, valor, descricao, data_movimentacao, categoria, frequencia "
                + "FROM movimentacoes WHERE id_usuario = ? AND tipo = 'DESPESA' "
                + "AND strftime('%Y-%m', data_movimentacao) = ? "
                + "ORDER BY valor * frequencia DESC";
        List<Read> despesas = new ArrayList<>();
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, anoMes);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    despesas.add(montar(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return despesas;
    }

    public Read buscarPorId(int id, int idUsuario) {
        String sql = "SELECT id, id_usuario, tipo, valor, descricao, data_movimentacao, categoria, frequencia "
                + "FROM movimentacoes WHERE id = ? AND id_usuario = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setInt(2, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Read montar(ResultSet rs) throws SQLException {
        Read movimentacao = new Read();
        movimentacao.setId(rs.getInt("id"));
        movimentacao.setIdUsuario(rs.getInt("id_usuario"));
        movimentacao.setTipo(rs.getString("tipo"));
        movimentacao.setValor(rs.getDouble("valor"));
        movimentacao.setDescricao(rs.getString("descricao"));
        movimentacao.setDataMovimentacao(rs.getString("data_movimentacao"));
        movimentacao.setCategoria(rs.getString("categoria"));
        movimentacao.setFrequencia(rs.getInt("frequencia"));
        return movimentacao;
    }
}
