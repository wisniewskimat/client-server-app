package serverclient.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Server {
    private PrintWriter out;
    private BufferedReader in;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private long serverStartTime = System.nanoTime();
    private Gson gson;
    private GsonBuilder builder;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();

    }
    public Server() throws IOException {
        SimpleDateFormat dateAfterFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateAfterFormat.format(new Date());
        float uptime = java.util.Calendar.getInstance().getTime().getSeconds();
        serverSocket = new ServerSocket(4444);
        clientSocket = serverSocket.accept();
    }
    public void start() throws IOException {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            builder = new GsonBuilder();
            gson = builder.create();

            printMenu();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clientSocket.close();
        }

    }
    public void stop() throws IOException{
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
    public void printMenu() throws IOException {
        boolean isTrue = true;
        out.println("Witaj. Wybierz jedną z poniższych opcji: ");
        String clientMessage = in.readLine();
        while (isTrue) {
            switch (clientMessage) {
                case "uptime":
                    long serverCurrentTime = System.nanoTime();
                    long serverTotalTime = (serverCurrentTime - serverStartTime) / 1_000_000_000;
                    String uptime = gson.toJson("uptime: " + serverTotalTime + " seconds.");
                    out.println(uptime);
                    break;
                case "info":
                    Date currentDate = new Date();
                    SimpleDateFormat dateAfterFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateAfterFormat.format(currentDate);
                    double serverVersion = 1.01;
                    String info = gson.toJson("Info: " + serverVersion + ", " + date);
                    out.println(info);
                    break;
                case "help":
                    String helpText = "Do wyboru masz:" + "\nuptime" + "\ninfo" + "\nhelp" + "\nstop";
                    String help = gson.toJson(helpText);
                    out.println(help);
                    break;
                case "stop":
                    isTrue = false;
                    String stop = gson.toJson("STOP");
                    out.println(stop);
                    this.stop();
                    break;
                default:
                    String uncorrectAnswer = gson.toJson("Błąd. Wybierz opcję jeszcze raz.");
                    break;
            }
        }
    }

}
