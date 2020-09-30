package emris.server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        int port;
        if(args.length == 1){
            port = Integer.parseInt(args[0]);

        }else if (args.length == 0){
            port = 8080;
        }
        else {
            System.out.println("wrong param");
            return;
        }


        Server server = new Server(port);
        server.start();


    }
}
