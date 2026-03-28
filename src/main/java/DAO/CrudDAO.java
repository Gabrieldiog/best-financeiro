package DAO;

import model.Crud;
import utils.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudDAO {

    public boolean inserir(Crud transacao) {
        String sql = "INSERT INTO transacoes (usuario_id, tipo, valor, data, descricao, categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, transacao.getUsuarioId());
                ps.setString(2, transacao.getTipo());
                ps.setDouble(3, transacao.getValor());
                ps.setString(4, transacao.getData());
                ps.setString(5, transacao.getDescricao());
                ps.setString(6, transacao.getCategoria());
                ps.executeUpdate();
                return true;
            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }

    }

    public boolean atualizar(Crud transacao) {
        String sql = "UPDATE transacoes SET tipo = ?, valor = ?, data = ?, descricao = ?, categoria = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, transacao.getTipo());
                ps.setDouble(2, transacao.getValor());
                ps.setString(3, transacao.getData());
                ps.setString(4, transacao.getDescricao());
                ps.setString(5, transacao.getCategoria());
                ps.setInt(6, transacao.getId());
                ps.executeUpdate();
                return true;
            }catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }
}
