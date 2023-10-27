package com.mycompany.corretorprova;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) throws IOException {
        try (ServerSocket servidor = new ServerSocket(12345)) {
            System.out.println("Porta 12345 aberta!");

            while (true) {
                // Aceita uma nova conexão
                try (Socket cliente = servidor.accept()) {
                    System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());

                    // Lê a resposta do cliente
                    String resposta = leitorRespostas(cliente);

                    // Lê o gabarito
                    String gabarito = leitorGabarito();

                    // Calcula a resposta do corretor
                    String resultado = corretor(resposta, gabarito);

                    // Envia a resposta para o cliente
                    OutputStream escritor = cliente.getOutputStream();
                        escritor.write(resultado.getBytes());
                        escritor.flush();
                    
                }
            }
        }
    }

    private static String leitorRespostas(Socket cliente) throws IOException {

        StringBuilder textoResposta;
        try (
                BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream(), "UTF-8"))) {
            textoResposta = new StringBuilder();
            String linha;
            while ((linha = leitor.readLine()) != null) {
                textoResposta.append(linha);
            }
        }
        return textoResposta.toString().trim();
    }

    private static String leitorGabarito() throws IOException {
        String gabarito = "gabarito.txt";

        try (BufferedReader leitor = new BufferedReader(new FileReader(gabarito))) {
            StringBuilder textoGabarito = new StringBuilder();
            String linha;
            while ((linha = leitor.readLine()) != null) {
                textoGabarito.append(linha);
            }

            return textoGabarito.toString().trim();
        }
    }

private static String corretor(String respostas, String gabarito) {
        //Verifica se o tamanho do texto recebido e do gabarito são iguais
        if (respostas.length() != gabarito.length()) {
            return "Tamanho das respostas e do gabarito não coincidem";
        }

        int contaAcerto = 0;
        int contaErro = 0;

        //Verifica cada linha do texto de resposta e do gabarito desde que seja
        // "V" ou "F" e compara os caracteres. A cada caractere igual ele adiciona
        // +1 a variável contaAcerto e a cada caractere diferente ele contabiliza
        // +1 a variavel contaErro.

        for (int i = 0; i < respostas.length(); i++) {
            char caractereResposta = respostas.charAt(i);
            char caractereGabarito = gabarito.charAt(i);

            //Verifica se o caractere é um número
            if (caractereResposta >= '0' && caractereResposta <= '9') {
                i++; //Pula o caractere do número da questão
                continue;
            }

            //Verifica se o caractere é um hífen
            if (caractereResposta == '-') {
                i++; //Pula o hífen
                continue;
            }

            switch (caractereResposta) {
                case 'V':
                    if (caractereGabarito == 'V') {
                        contaAcerto++;
                    } else {
                        contaErro++;
                    }
                    break;
                case 'F':
                    if (caractereGabarito == 'F') {
                        contaAcerto++;
                    } else {
                        contaErro++;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Resposta inválida: " + caractereResposta);
            }
        }

        //Retorna a resposta do corretor
        return "Acertos: " + contaAcerto + "\nErros: " + contaErro;
    }
        }
