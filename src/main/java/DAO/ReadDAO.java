package DAO;

import model.Usuario;
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
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setUsername(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }

    public List<String> listarMovimentacoes(int idUsuario) {
        String sql = "SELECT tipo, valor, descricao, data_movimentacao FROM movimentacoes WHERE id_usuario = ?";
        List<String> extratoEmTexto = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
                    
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String tipo = rs.getString("tipo");
                double valor = rs.getDouble("valor");
                String descricao = rs.getString("descricao");
                String data = rs.getString("data_movimentacao");
                
                String linhaFormatada = String.format("[%s] | %s | R$ %.2f | %s", data, tipo, valor, descricao);

                extratoEmTexto.add(linhaFormatada);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        
        return extratoEmTexto;
        /*  teste */
    }
}