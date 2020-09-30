package emris.server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Server server = new Server(8080);
        server.start();


    }
}
