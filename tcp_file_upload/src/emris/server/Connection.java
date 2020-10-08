package emris.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Connection implements Runnable {
    private static final int BUFFER_SIZE = 1024 * 1024;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final int TIMEOUT = 3000;

    private static final int MB = 1024 * 1024;

    private final Socket clientSocket;

    Connection(final Socket clientSocket) throws SocketException {
        this.clientSocket = clientSocket;
        this.clientSocket.setSoTimeout(TIMEOUT * 10);
    }

    private boolean checkDirectory(final File dir) {
        if (!dir.exists()) {
            return dir.mkdir();
        }
        return true;
    }

    @Override
    public void run() {
        try (this.clientSocket) {
            final InputStream socketInputStream = this.clientSocket.getInputStream();
            final DataInputStream inputFromClient = new DataInputStream(socketInputStream);
            final BufferedWriter writerToClient = new BufferedWriter(new OutputStreamWriter(
                    this.clientSocket.getOutputStream(), CHARSET));
            final File dir = new File("./upload/");
            if (!this.checkDirectory(dir)) {
                writerToClient.write("can't create directory. Server error");
                writerToClient.flush();
                return;
            }

            final int nameLength = inputFromClient.readInt();

            final byte[] nameBuffer = new byte[nameLength];
            final int actualNameLength = inputFromClient.readNBytes(nameBuffer, 0, nameLength);
            final String fileName = new String(nameBuffer, 0, actualNameLength, CHARSET);

            if (fileName.contains(File.separator) || fileName.contains("/") || fileName.contains("\\")) {
                writerToClient.write("incorrect file name ( should be without /)");
                writerToClient.flush();
                return;
            }
            final long fileSize = inputFromClient.readLong();
            System.out.println("got file_name: " + fileName + "\n" + "got file_Size: " + fileSize);
            final File file = new File(dir.getAbsoluteFile() + File.separator + fileName);
            final boolean read = this.readFile(file, fileSize, socketInputStream);
            final String answer = this.checkUploadedFile(file, fileSize, read);
            if (answer == null) {
                System.out.println("ha-ha, gay");
                return;
            }
            writerToClient.write("server: " + fileName + answer + "\n");
            writerToClient.flush();
            System.out.println("done");
        } catch (final IOException e) {
            System.err.println("connection: " + e.getMessage());
        }
    }

    private boolean readFile(final File file, final long fileSize, final InputStream socketInputStream) throws
            IOException {
        try (final FileOutputStream writerToFile = new FileOutputStream(file)) {
            long readySize = 0;
            final byte[] buffer = new byte[BUFFER_SIZE];
            long period_read = 0;
            final long startTime = System.currentTimeMillis();
            long time = startTime;
            long receiveTime = startTime;

            while (readySize < fileSize) {
                System.out.println("ffs");
                final int actuallyRead = socketInputStream.readNBytes(buffer, 0, buffer.length);
                if (actuallyRead == 0) {
                    break;
                }
                period_read += actuallyRead;
                readySize += actuallyRead;

                writerToFile.write(buffer, 0, actuallyRead);
                receiveTime = System.currentTimeMillis();
                if (receiveTime - time > TIMEOUT) {
                    this.printSpeedInfo(file.getName(), readySize, period_read, startTime, time, receiveTime);
                    period_read = 0;
                    time = System.currentTimeMillis();
                }


            }
            this.printSpeedInfo(file.getName(), readySize, period_read, startTime, time, receiveTime);

            return true;
        }
    }

    private void printSpeedInfo(final String fileName, final long readySize, final double period_read, final long startTime, final long time, final long receiveTime) {
        System.out.println(fileName);
        System.out.println("    current speed: " + String.format("%.2f",
                period_read / MB / ((double) (receiveTime - time) / 1000)) + " mb/s");
        System.out.println("    average speed: " + String.format("%.2f",
                (double) (readySize) / MB / ((double) (receiveTime - startTime) / 1000)) + " mb/s");
    }


    private boolean checkFileSize(final File file, final long size) {
        return file.length() == size;
    }

    private String checkUploadedFile(final File file, final long fileSize, final boolean read) {
        if (this.checkFileSize(file, fileSize)) {
            return " loaded";
        } else if (!read) {
            return null;
        } else {
            return " incorrect size ";
        }
    }

}
