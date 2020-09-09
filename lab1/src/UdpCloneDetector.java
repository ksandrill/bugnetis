import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class UdpCloneDetector {
    private MulticastSocket _socket;
    private final SocketAddress _multicastAddr;
    private final int _timeout;
    private final int _port;
    private long lastSendtime;
    private long lastRecvtime;
    private Map<String, Long> knownCopies;
    public UdpCloneDetector(String multicastAddr, int port, int timeout) throws IOException {
        _multicastAddr = new InetSocketAddress(multicastAddr,port);
        _port = port;
        _timeout = timeout;
        _socket = new MulticastSocket(_port);
        NetworkInterface IFC = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        _socket.setSoTimeout(_timeout);
        _socket.joinGroup(_multicastAddr,IFC);


    }

    public void send(String message) throws IOException {
        byte[] messByte = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(messByte,messByte.length,_multicastAddr);
        _socket.send(sendPacket);
        System.out.println("send message to"+sendPacket.getAddress());
        lastSendtime = System.currentTimeMillis();

    }

    private String recieve(){
        byte[] messByte = new byte[128];
        DatagramPacket recvPacket = new DatagramPacket(messByte,messByte.length);
        try{
            _socket.receive(recvPacket);
            lastRecvtime = System.currentTimeMillis();

        } catch (IOException e) {
           return null;
        }
        return recvPacket.getAddress().toString();


    }

    public void run() throws IOException {

        lastSendtime = System.currentTimeMillis();
        knownCopies = new HashMap<>();
        while (true){
            if(System.currentTimeMillis() - lastSendtime > _timeout){
                send("hello");

            }
            var localIp = recieve();
            lastRecvtime = System.currentTimeMillis();
            knownCopies.put(localIp,lastRecvtime);
            System.out.println(knownCopies);

            System.out.println("got message from " + localIp);


        }


    }

}
