package emris.client;

import java.io.IOException;
import java.net.InetAddress;

public final class App {
    private App() {
    }

    public static void main(final String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("localhost and 8080 - default debug");
            final FileSender FileSender = new FileSender(InetAddress.getByName("25.36.178.143"), 8080);
            FileSender.sendFile("MoxIHK-ta_E.mp3");
        } else if (args.length == 3) {
            final String serverIp = args[0];
            final int port = Integer.parseInt(args[1]);
            final String filePath = args[2];
            final FileSender FileSender = new FileSender(InetAddress.getByName(serverIp), port);
            FileSender.sendFile(filePath);
        } else {
            System.out.println("wrong param. Shoud be  3: server ip, port, file  or zero(localhost, 8080, test.pdf");
        }
    }
}

