import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class UdpCloneDetector {
    private  MulticastSocket socket;
    private final SocketAddress multicastAddr;
    private final int TIMEOUT;
    private final int UPDATE_TIMEOUT;
    private long lastSendtime;
    private long lastRecvtime;

    public UdpCloneDetector(String multicastAddr, int port, int timeout, int updateTimeout) throws IOException {
        this.multicastAddr = new InetSocketAddress(multicastAddr,port);
        TIMEOUT = timeout;
        UPDATE_TIMEOUT = updateTimeout;
        socket = new MulticastSocket(port);
        NetworkInterface IFC = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        socket.setSoTimeout(TIMEOUT);
        socket.joinGroup(this.multicastAddr,IFC);


    }

    public void send(String message) throws IOException {
        byte[] messByte = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(messByte,messByte.length, multicastAddr);
        socket.send(sendPacket);
        lastSendtime = System.currentTimeMillis();

    }

    private String recieve(){
        byte[] messByte = new byte[128];
        DatagramPacket recvPacket = new DatagramPacket(messByte,messByte.length);
        try{
            socket.receive(recvPacket);
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
            if(System.currentTimeMillis() - lastSendtime > TIMEOUT){
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
            System.out.println(knownCopies.size() + " alive!");
            for(Map.Entry<String, Long> entry : knownCopies.entrySet()) {
               System.out.println(entry.getKey() + " last seen at " + new Date(entry.getValue()));
            }





        }


    }

}
