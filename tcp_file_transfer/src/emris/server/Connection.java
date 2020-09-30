package emris.server;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable {
    private final Socket clientSocket;
    private final long TIMEOUT = 3000;

    public Connection(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    private boolean checkDirectory(File dir) {
        if (!dir.exists()) {
            return dir.mkdir();
        }
        return true;
    }

    @Override
    public void run() {
        try (
                BufferedReader readerFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter writerToClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                InputStream socketInputStream = clientSocket.getInputStream()


        ) {
            File dir = new File("./upload/");
            if (!checkDirectory(dir)) {
                writerToClient.write("can't create directory. Server error");
                writerToClient.flush();
                return;

            }
            String fileName = readerFromClient.readLine();
            long fileSize = Integer.parseInt(readerFromClient.readLine());
            System.out.println("got file_name: " + fileName + "\n" + "got file_Size: " + fileSize);
            File file = new File(dir.getAbsoluteFile() + "/" + fileName);
            boolean read = readFile(file, fileSize, socketInputStream);
            String answer = checkUploadedFile(file, fileSize, read);
            if (answer == null) {
                return;
            }
            writerToClient.write("server: " + fileName + answer + "\n");

            writerToClient.flush();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private boolean readFile(File file, long fileSize, InputStream SocketInputStream) throws IOException {
        try (FileOutputStream writerToFile = new FileOutputStream(file)) {
            long readySize = 0;
            int bufSize = 2048;
            byte[] buffer = new byte[bufSize];
            long period_read = 0;
            long startTime = System.currentTimeMillis();
            long time = startTime;
            while (readySize < fileSize) {

                int read = fileSize - readySize > bufSize ? SocketInputStream.read(buffer) : SocketInputStream.read(buffer, 0, (int) (fileSize - readySize));
                period_read += read;
                if (read == -1) {
                    return false;
                }

                readySize += read;
                writerToFile.write(buffer, 0, read);
                if (System.currentTimeMillis() - time > TIMEOUT) {
                    time = System.currentTimeMillis();
                    System.out.println(file.getName());
                    System.out.println("    current speed: " + String.format("%.2f", (double) period_read / 1024 / 1024 /3) + " mb/s");
                    System.out.println("    average speed: " + String.format("%.2f", (double) (fileSize) / 1024 / 1024 / ((double) (time - startTime) / 1000)) + " mb/s");
                    period_read = 0;
                }


            }
            time = System.currentTimeMillis();
            System.out.println(file.getName());
            System.out.println("    current speed: " + String.format("%.2f", (double) period_read / 1024 / 1024/ 3) + " mb/s");
            System.out.println("    average speed: " + String.format("%.2f", (double) (fileSize) / 1024 / 1024 / ((double) (time - startTime) / 1000)) + " mb/s");

            return true;

        }
    }


    private boolean checkFileSize(File file, long size) {
        return file.length() == size;
    }

    private String checkUploadedFile(File file, long fileSize, boolean read) {
        String answer;
        if (checkFileSize(file, fileSize)) {
            answer = " loaded";
        } else if (!read) {
            answer = null;
        } else {
            answer = " incorrect size ";
        }
        return answer;

    }

}
