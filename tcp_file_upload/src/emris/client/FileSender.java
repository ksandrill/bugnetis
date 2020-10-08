package emris.client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

class FileSender {
    private static final int BUFFER_SIZE = 1024 * 1024;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final Socket clientSocket;

    FileSender(final InetAddress ip, final int port) throws IOException {
        this.clientSocket = new Socket(ip, port);
    }

    void sendFile(final String fileName) throws IOException {
        final File file = new File(fileName);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            System.err.println("Bad file");
            return;
        }

        try (
                this.clientSocket;
                final FileInputStream fileInputStream = new FileInputStream(file)
        ) {
            final OutputStream socketOutputStream = this.clientSocket.getOutputStream();
            final DataOutputStream writeStream = new DataOutputStream(socketOutputStream);
            final BufferedReader readerFromServer = new BufferedReader(new InputStreamReader(
                    this.clientSocket.getInputStream(),CHARSET));

            final byte[] nameBytes = file.getName().getBytes(CHARSET);
            writeStream.writeInt(nameBytes.length);
            writeStream.write(nameBytes, 0, nameBytes.length);
            writeStream.writeLong(file.length());
            this.writeFile(fileInputStream, writeStream);
            this.clientSocket.shutdownOutput();
            final String answer = readerFromServer.readLine();
            System.out.println(answer);
        }
    }

    private void writeFile(final FileInputStream fileInputStream, final OutputStream socketOutStream) throws
            IOException {
        final byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while (true) {
            read = fileInputStream.read(buffer);
            if (read == -1) {
                break;
            }
            socketOutStream.write(buffer, 0, read);
        }
        socketOutStream.flush();
    }
}


