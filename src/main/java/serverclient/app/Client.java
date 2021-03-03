package serverclient.app;

import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Client {
    private static BufferedReader in;
    private static PrintWriter out;
    private Socket clientSocket;
    private static String ip = "127.0.0.1";
    private static int port = 4444;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.startConnection(ip, port);
        client.sendMessage();
        client.stopConnection();
    }

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);

    }
    public void sendMessage() throws IOException {
        System.out.println("Witaj. Wybierz jedną z dostępnych opcji: " + "\nuptime" +
                "\ninfo" + "\nhelp" + "\nstop");
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        boolean isTrue = true;

        while(isTrue) {
            String line = bufferedReader.readLine();
            out.println(line);
            String response = in.readLine();
            if(line.equals("stop")) {
                isTrue = false;
                out.println("stop");
                System.out.println(response);
                this.stopConnection();
            }
        }
    }
    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
