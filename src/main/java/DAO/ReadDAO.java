package DAO;

import model.Usuario;
import model.Read;
import utils.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadDAO {
    
    public Usuario buscarUsuarioPorId(int id) {
        String sql = "SELECT username FROM usuarios WHERE id = ?";
        Usuario usuario = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setUsername(rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    // Método atualizado para o RF04: Retorna objetos e inclui categoria + ordenação
    public List<Read> listarMovimentacoes(int idUsuario) {
        // Adicionada a coluna 'categoria' e a ordenação decrescente por data (ORDER BY)
        String sql = "SELECT tipo, valor, descricao, data_movimentacao, categoria " +
                     "FROM movimentacoes WHERE id_usuario = ? " +
                     "ORDER BY data_movimentacao DESC";
                     
        List<Read> extrato = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                    
            ps.setInt(1, idUsuario);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Read movimentacao = new Read();
                    
                    movimentacao.setTipo(rs.getString("tipo"));
                    movimentacao.setValor(rs.getDouble("valor"));
                    movimentacao.setDescricao(rs.getString("descricao"));
                    movimentacao.setDataMovimentacao(rs.getString("data_movimentacao"));
                    movimentacao.setCategoria(rs.getString("categoria"));
                    
                    extrato.add(movimentacao);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return extrato;
    }
}