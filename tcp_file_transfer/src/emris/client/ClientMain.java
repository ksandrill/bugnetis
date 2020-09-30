package emris.client;

import java.io.IOException;
import java.net.InetAddress;

public class ClientMain {
        public static void main(String[] args) throws IOException {
            Client client = new Client(InetAddress.getByName("localhost"), 8080);
            client.sendFile("ffs.jpg");


        }
    }

