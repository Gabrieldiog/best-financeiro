package utils;

import model.Read;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportadorCSV {

    public static boolean exportar(List<Read> movimentacoes, String caminho) {
        File arquivo = new File(caminho);
        File pasta = arquivo.getParentFile();
        if (pasta != null) {
            pasta.mkdirs();
        }
        try (FileWriter writer = new FileWriter(arquivo)) {
            writer.write("id,tipo,valor,descricao,data,categoria\n");
            for (Read m : movimentacoes) {
                writer.write(m.getId() + ","
                        + m.getTipo() + ","
                        + String.format(java.util.Locale.US, "%.2f", m.getValor()) + ","
                        + escapar(m.getDescricao()) + ","
                        + m.getDataMovimentacao() + ","
                        + escapar(m.getCategoria()) + "\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        if (texto.contains(",") || texto.contains("\"") || texto.contains("\n")) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }
}
