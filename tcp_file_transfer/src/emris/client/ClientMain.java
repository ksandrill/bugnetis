package emris.client;

import java.io.IOException;
import java.net.InetAddress;

public class ClientMain {
        public static void main(String[] args) throws IOException {
            if(args.length == 0){
                System.out.println("localhost and 8080 - default debug");
                Client client = new Client(InetAddress.getByName("localhost"), 8080);
                client.sendFile("test.pdf");
            }
            else if(args.length == 3) {
                String  serverIp = args[0];
                int port = Integer.parseInt(args[1]);
                String filePath = args[2];
                Client client = new Client(InetAddress.getByName(serverIp), port);
                client.sendFile(filePath);
            }
            else {
                System.out.println("wrong param. Shoud be  3: server ip, port, file  or zero(localhost, 8080, test.pdf");
            }


        }
    }

