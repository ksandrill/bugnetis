import java.io.IOException;
import java.net.*;

public class UdpCloneDetector {
    private MulticastSocket _socket;
    private SocketAddress _multicastAddr;
    private int _timeout;
    private final int _port;
    private long lastSendtime;
    private long lastRecvtime;
    public UdpCloneDetector(String multicastAddr, int port, int timeout) throws IOException {
        _multicastAddr = new InetSocketAddress(multicastAddr,port);
        _port = port;
        _timeout = timeout;
        _socket = new MulticastSocket();
        NetworkInterface IFC = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        _socket.setSoTimeout(_timeout);
        _socket.joinGroup(_multicastAddr,IFC);


    }

    public void send(String message) throws IOException {
        byte[] messByte = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(messByte,messByte.length,_multicastAddr);
        _socket.send(sendPacket);
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
        while (true){ ;
            if(System.currentTimeMillis() - lastSendtime > _timeout){
                send("hello");

            }
            var s = recieve();
            System.out.println("got message from " + s);


        }


    }

}
