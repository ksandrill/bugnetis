package emris.server;

import java.io.IOException;

public final class App {
    private App() {
    }

    public static void main(final String[] args) throws IOException {
        final int port;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);

        } else if (args.length == 0) {
            port = 8080;
        } else {
            System.out.println("wrong param");
            return;
        }

        final Server server = new Server(port);
        server.start();
    }
}
