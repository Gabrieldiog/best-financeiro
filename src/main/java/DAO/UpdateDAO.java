package DAO;

import model.Read;
import utils.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateDAO {

    public boolean atualizar(Read movimentacao) {
        String sql = "UPDATE movimentacoes SET tipo = ?, valor = ?, descricao = ?, "
                + "data_movimentacao = ?, categoria = ?, frequencia = ? WHERE id = ? AND id_usuario = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, movimentacao.getTipo());
            ps.setDouble(2, movimentacao.getValor());
            ps.setString(3, movimentacao.getDescricao());
            ps.setString(4, movimentacao.getDataMovimentacao());
            ps.setString(5, movimentacao.getCategoria());
            ps.setInt(6, movimentacao.getFrequencia());
            ps.setInt(7, movimentacao.getId());
            ps.setInt(8, movimentacao.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
