package emris.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final Socket clientSocket;


    public Client(InetAddress ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);

    }

    public void sendFile(String fileName) throws IOException {
        try (clientSocket;
             BufferedWriter writerToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
             BufferedReader readerFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream socketOutputStream = clientSocket.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(fileName)

        ) {
            writerToServer.write(fileName + "\n");
            writerToServer.write(new File(fileName).length() + "\n");
            writerToServer.flush();
            writeFile(fileInputStream, socketOutputStream);
            String answer = readerFromServer.readLine();
            System.out.println(answer);

        }


    }

    private void writeFile(FileInputStream fileInputStream, OutputStream socketOutStream) throws IOException {
        byte[] buffer = new byte[1024];
        int read = 0;
        while (fileInputStream.available() > 0 && read != -1) {
            read = fileInputStream.read(buffer);
            socketOutStream.write(buffer);
        }
        socketOutStream.flush();
    }



}


