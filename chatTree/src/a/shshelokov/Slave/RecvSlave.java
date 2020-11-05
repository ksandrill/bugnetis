package a.shshelokov.Slave;

import a.shshelokov.Message.Message;
import a.shshelokov.Packet;
import a.shshelokov.TreeNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
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
                Packet packet = getPacket();
                recvPackets.add(packet);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    private Packet getPacket() throws IOException, ClassNotFoundException {
        DatagramPacket recvPacket = new DatagramPacket(new byte[Message.BUFF_SIZE], Message.BUFF_SIZE);
        socket.receive(recvPacket);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(recvPacket.getData()));
        Message recvMessage = (Message) ois.readObject();

        return new Packet((InetSocketAddress) recvPacket.getSocketAddress(), recvMessage,REC_TTL);

    }

}
