import java.io.IOException;
import java.net.SocketAddress;

public class App {
    public static void main(String[] args) throws IOException {

       UdpCloneDetector detector = new UdpCloneDetector("224.0.147.0",8081,2000,3000);
       detector.run();

    }
}
