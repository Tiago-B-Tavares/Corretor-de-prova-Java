package com.mycompany.corretorprova;

import java.io.*;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) throws IOException {

        ResultadoViaThread res = new ResultadoViaThread();
        res.start();
    }

    public static String leitorRespostas(Socket cliente) throws IOException {

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

    public static String leitorGabarito(Socket Cliente) throws IOException {
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

    public static String corretor(String respostas, String gabarito) {

        if (respostas.length() != gabarito.length()) {
            return "Tamanho das respostas e do gabarito não coincidem";
        }
        /*      Verifica cada linha do texto de resposta e do gabarito desde que seja
         "V" ou "F" e compara os caracteres. A cada caractere igual ele adiciona
         +1 a variável contaAcerto e a cada caractere diferente ele contabiliza
        +1 a variavel contaErro.
         */
        int contaAcerto = 0;
        int contaErro = 0;

        for (int i = 0; i < respostas.length(); i++) {
            char caractereResposta = respostas.charAt(i);
            char caractereGabarito = gabarito.charAt(i);

            if (caractereResposta >= '0' && caractereResposta <= '9') {
                i++;
                continue;
            }

            if (caractereResposta == '-') {
                i++;
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
        return "Acertos: " + contaAcerto + "\nErros: " + contaErro;
    }
}
