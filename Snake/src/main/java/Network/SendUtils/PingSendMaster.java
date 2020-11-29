package Network.SendUtils;

import Network.protocol.Packet;

import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PingSendMaster implements Runnable {
    DatagramSocket socket;
    ConcurrentLinkedQueue<Packet> packetsToSend;

    public PingSendMaster(DatagramSocket socket, ConcurrentLinkedQueue<Packet> packetsToSend) {
        this.socket = socket;
        this.packetsToSend = packetsToSend;
    }

    @Override
    public void run() {
            System.err.println("here should be ping");
        }

    }

