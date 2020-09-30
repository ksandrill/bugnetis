package emris.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final Socket clientSocket;
    private final BufferedWriter writerToServer;
    private final BufferedReader readerFromServer;

    public Client(InetAddress ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        readerFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writerToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));



    }

    public void sendFile(String fileName) {
        try {
            File file = new File(fileName);

            writerToServer.write(fileName + "\n"); // отправляем сообщение на сервер
            writerToServer.write(file.length()+"\n");
            writerToServer.flush();
            String retFileName = readerFromServer.readLine(); // ждём, что скажет сервер
            String retFileSize = readerFromServer.readLine();
            System.out.println(retFileName); // получив - выводим на экран
            System.out.println(retFileSize);
            OutputStream outputStream = clientSocket.getOutputStream();
            FileInputStream fileInputStream  = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int read = 0;
            while(fileInputStream.available() > 0 && read !=-1){
                read = fileInputStream.read(buffer);
                System.out.println(read);
                outputStream.write(buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
               close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    void close() throws IOException {
        readerFromServer.close();
        writerToServer.close();
        clientSocket.close();

    }


}
