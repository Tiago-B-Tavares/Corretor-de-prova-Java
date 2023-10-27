package com.mycompany.corretorprova;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Cliente1 {

    public static void main(String[] args) throws IOException {
        int porta = 12345;  // Porta do servidor
        String host = "localhost";  // Endereço do servidor
        
        // Configura o seletor de arquivo
        JFileChooser seletorArquivo = new JFileChooser();
        FileNameExtensionFilter filtroTipoArquivo = new FileNameExtensionFilter("Arquivos de texto", "txt");
        seletorArquivo.setFileFilter(filtroTipoArquivo);

        // Mostra a janela de seleção de arquivo
        int resultadoSelecao = seletorArquivo.showOpenDialog(null);
        
        if (resultadoSelecao == JFileChooser.APPROVE_OPTION) {
            java.io.File arquivoSelecionado = seletorArquivo.getSelectedFile();
            System.out.println("Arquivo selecionado: " + arquivoSelecionado.getAbsolutePath());

            String caminhoArquivoSelecionado = arquivoSelecionado.getAbsolutePath();

            // Cria um objeto File para representar o arquivo
            File arquivo = new File(caminhoArquivoSelecionado);
            byte[] buffer = new byte[1024];

            try (FileInputStream fileInputStream = new FileInputStream(arquivo)) {
                // Ler o conteúdo do arquivo para o buffer
                fileInputStream.read(buffer);
                
                // Conecta ao servidor usando um socket
                Socket clienteSocket = new Socket(host, porta);
                System.out.println("Cliente conectado com sucesso!");

                // Obtem o OutputStream para enviar dados para o servidor
                OutputStream saida = clienteSocket.getOutputStream();
                System.out.println("Enviado ao servidor com sucesso!");

                // Envia o conteúdo do arquivo para o servidor
                saida.write(buffer, 0, buffer.length);
                saida.flush();
                System.out.println("Arquivo enviado ao servidor com sucesso!");

                // Feche o socket
                clienteSocket.close();
            }
        } else {
            System.out.println("Nenhum arquivo selecionado.");
        }
        
    }
    
}
