package com.mycompany.corretorprova;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Cliente2 {

    public static void main(String[] args) throws IOException {
        int porta = 12345;
        String host = "localhost";
        try (
                // Conecta ao servidor usando um socket
                Socket clienteSocket = new Socket(host, porta)) {
            System.out.println("Cliente conectado com sucesso!");

            // Lê o arquivo e envia em formato de array de bytes o conteúdo lido
            enviaResposta(clienteSocket);

            //Recebe o resultado com a correção do servidor
            clienteSocket.close();
        } catch (IOException e) {
            System.out.println("Falha ao conectar com o servidor \n" + e.getMessage());
        }
    }

    public static String escolheArquivoResposta() {
        // Configura o seletor de arquivo
        JFileChooser seletorArquivo = new JFileChooser();
        FileNameExtensionFilter filtroTipoArquivo = new FileNameExtensionFilter("Arquivos de texto", "txt");
        seletorArquivo.setFileFilter(filtroTipoArquivo);

        // Mostra a janela de seleção de arquivo
        int resultadoSelecao = seletorArquivo.showOpenDialog(null);

        if (resultadoSelecao == JFileChooser.APPROVE_OPTION) {
            java.io.File arquivoSelecionado = seletorArquivo.getSelectedFile();
            System.out.println("Arquivo selecionado: " + arquivoSelecionado.getAbsolutePath());

            return arquivoSelecionado.getAbsolutePath();
        } else {
            return "Nenhum arquivo selecionado.";
        }
    }

    public static void enviaResposta(Socket clienteSocket) throws IOException {
        byte[] buffer = new byte[1024];
        //Recebe o caminho do arquivo que foi selecionado
        String caminhoArquivoSelecionado = escolheArquivoResposta();
        // Cria um objeto File pra representar o arquivo
        File arquivo = new File(caminhoArquivoSelecionado);
        FileInputStream fileInputStream = new FileInputStream(arquivo);
        // Lê o conteúdo do arquivo para o buffer
        fileInputStream.read(buffer);
     
        OutputStream saida = clienteSocket.getOutputStream();

        // Envia o conteúdo do arquivo para o servidor
        saida.write(buffer, 0, buffer.length);
        saida.flush();
        System.out.println("Arquivo enviado ao servidor com sucesso!");
    }
}
