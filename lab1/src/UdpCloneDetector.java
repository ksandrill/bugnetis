import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class UdpCloneDetector {
    private MulticastSocket _socket;
    private final SocketAddress _multicastAddr;
    private final int _timeout;
    private final int _port;
    private final int UPDATE_TIMEOUT = 3000;
    private long lastSendtime;
    private long lastRecvtime;

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
        Map<String, Long> knownCopies = new HashMap<>();
        LinkedList<String> deffered = new LinkedList<>();
        while (true){
            if(System.currentTimeMillis() - lastSendtime > _timeout){
                send("hello");

            }
            var localIp = recieve();
            if(localIp != null) {
                knownCopies.put(localIp, lastRecvtime);
            }
            for(Map.Entry<String, Long> entry : knownCopies.entrySet()) {
                if(System.currentTimeMillis() - entry.getValue() > UPDATE_TIMEOUT){
                    deffered.add(entry.getKey());
                }
            }
            for (var value: deffered) {
                knownCopies.remove(value);

            }
            deffered.clear();
            for(Map.Entry<String, Long> entry : knownCopies.entrySet()) {
               System.out.println(entry.getKey() + " last seen at " + new Date(entry.getValue()) + "s");
            }





        }


    }

}
