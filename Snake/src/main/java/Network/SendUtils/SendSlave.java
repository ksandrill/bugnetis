package Network.SendUtils;

import Network.protocol.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SendSlave implements Runnable {
    private DatagramSocket socket;
    private ConcurrentLinkedQueue<Packet> packetsToSend;

    public SendSlave(DatagramSocket socket, ConcurrentLinkedQueue<Packet> packetsToSend) {
        this.socket = socket;
        this.packetsToSend = packetsToSend;
    }

    @Override
    public void run() {
        while (true) {
            try {
                send();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void send() throws IOException {

        var packetToSend = packetsToSend.poll();
        if (packetToSend != null) {
            var dataToSend = packetToSend.getMessage().toByteArray();
            DatagramPacket packet = new DatagramPacket(dataToSend, dataToSend.length, packetToSend.getAddrPort());
            socket.send(packet);
            System.out.println(Thread.currentThread().getName() + ": " + "i send shit to " + packetToSend.getAddrPort().toString());
        }

    }
}

