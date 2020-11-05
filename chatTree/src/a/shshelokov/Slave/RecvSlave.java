package a.shshelokov.Slave;

import a.shshelokov.Packet;
import a.shshelokov.TreeNode;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecvSlave implements Runnable {
    private DatagramSocket socket;
    private TreeNode node;
    private final int REC_TTL = 1;


    public RecvSlave(TreeNode node) {
        this.socket = node.getSocket();
        this.node = node;
    }

    @Override
    public void run() {
        while (true) {
            ConcurrentLinkedQueue<Packet> recvPackets = node.getRecvPackets();
            try {
                Packet packet = Packet.getPacket(socket,REC_TTL);
                recvPackets.add(packet);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }



}
