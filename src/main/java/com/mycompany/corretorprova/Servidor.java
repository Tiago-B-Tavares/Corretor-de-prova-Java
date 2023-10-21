package com.mycompany.corretorprova;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args) {
        try {
            Socket cliente;
            try (ServerSocket servidor = new ServerSocket(12345)) {
                System.out.println("Porta 12345 aberta!");
                cliente = servidor.accept();
                System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());
                // Cria leitores e escritores para texto
                BufferedReader leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream(), "UTF-8"));
                PrintWriter escritor = new PrintWriter(cliente.getOutputStream(), true);
                String respostas = leitor.readLine().trim();
                System.out.println("Texto das respostas: " + respostas);
                String textoGabarito = leitorGabarito();
                System.out.println("Texto do gabarito: " + textoGabarito);
                String resultado = corretor(respostas, textoGabarito);
                System.out.println(resultado);
                // Fecha recursos
                leitor.close();
                escritor.close();
            }
            cliente.close();
        } catch (IOException e) {
        }
    }

    public static String leitorGabarito() {
        try {
            String gabarito = "Gabarito.txt";
            BufferedReader leitor = new BufferedReader(new FileReader(gabarito));
            StringBuilder textoCompleto = new StringBuilder();
            String linha;

            while ((linha = leitor.readLine()) != null) {
                textoCompleto.append(linha).append("");
            }

            leitor.close();
            return textoCompleto.toString().trim(); // Remova espaços em branco no início e no final
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String corretor(String respostas, String gabarito) {
        if (respostas.length() != gabarito.length()) {
            return "Tamanho das respostas e do gabarito não coincidem"+respostas.length()+"  "+gabarito.length();
        }

        int contaAcerto = 0;
        int contaErro = 0;

        for (int i = 0; i < respostas.length(); i++) {
            char caractereResposta = respostas.charAt(i);
            char caractereGabarito = gabarito.charAt(i);

            if (caractereResposta == caractereGabarito) {
                contaAcerto++;
            } else {
                contaErro++;
            }
        }

        return "Acertos: " + contaAcerto + "\nErros: " + contaErro;
    }
}
