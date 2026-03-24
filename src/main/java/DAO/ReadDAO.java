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
                // Não adicionei senha nem ID aqui para focar só no nome que vai aparecer no extrato
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }
    public List<String> listarMovimentacoes(int idUsuario){
         String sql



    }
}

