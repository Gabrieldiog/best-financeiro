package DAO;

import model.Read;
import utils.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateDAO {

    public boolean registrar(Read movimentacao) {
        String sql = "INSERT INTO movimentacoes (id_usuario, tipo, valor, descricao, data_movimentacao, categoria, frequencia) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movimentacao.getIdUsuario());
            ps.setString(2, movimentacao.getTipo());
            ps.setDouble(3, movimentacao.getValor());
            ps.setString(4, movimentacao.getDescricao());
            ps.setString(5, movimentacao.getDataMovimentacao());
            ps.setString(6, movimentacao.getCategoria());
            ps.setInt(7, movimentacao.getFrequencia());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
