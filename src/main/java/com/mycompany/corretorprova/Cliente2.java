package com.mycompany.corretorprova;

import java.io.*;

import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Cliente2 {

    public static void main(String[] args) throws IOException {
        int porta = 12345;  // Porta do servidor
        String host = "localhost";  // Endereço do servidor
        try ( // Conecta ao servidor usando um socket
                Socket clienteSocket = new Socket(host, porta)) {
            System.out.println("Cliente conectado com sucesso!");
            //Escolhe o arquivo e pega seu caminho
            String caminhoArquivoSelecionado = escolheArquivoResposta();
            //Lê o arquivo e envia em formato de array de bytes o conteúdo lido
            enviaResposta(clienteSocket, caminhoArquivoSelecionado);
            //Mostra a correção feita pelo servidor
            resultadoCorrecaoServidor(clienteSocket);
            // Feche o socket
            clienteSocket.close();
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

    public static void enviaResposta(Socket clienteSocket, String caminhoArquivoSelecionado) throws IOException {

        byte[] buffer = new byte[1024];

        // Cria um objeto File para representar o arquivo
        File arquivo = new File(caminhoArquivoSelecionado);
        FileInputStream fileInputStream = new FileInputStream(arquivo);
        // Le o conteúdo do arquivo para o buffer
        fileInputStream.read(buffer);
        // Obtem o OutputStream para enviar dados para o servidor
        OutputStream saida = clienteSocket.getOutputStream();

        // Envia o conteúdo do arquivo para o servidor
        saida.write(buffer, 0, buffer.length);
        saida.flush();
        System.out.println("Arquivo enviado ao servidor com sucesso!");
    }

    public static void resultadoCorrecaoServidor(Socket cliente) throws IOException {

        byte[] buffer = new byte[1024];
        int bytesLidos;

        //Lê a resposta do servidor
        while ((bytesLidos = cliente.getInputStream().read(buffer)) != -1) {
            System.out.print(new String(buffer, 0, bytesLidos));
        }
    }

}
