package com.mycompany.corretorprova;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ResultadoViaThread extends Thread {

    int porta = 12345;
    String host = "localhost";

    public void run() {
        ServerSocket servidor;
        try {
            servidor = new ServerSocket(12345);
            Socket cliente;
            while (true) {
                cliente = servidor.accept();
                System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());

                Servidor sv = new Servidor();
                String respostas = sv.leitorRespostas(cliente);
                String gabarito = sv.leitorGabarito(cliente);
                String resultado = sv.corretor(respostas, gabarito);
               JOptionPane.showMessageDialog(null, resultado, "Resultado da correção", JOptionPane.INFORMATION_MESSAGE);
                //System.out.println(resultado);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage() + "Erro ao na conexão!");
        }
    }

}
