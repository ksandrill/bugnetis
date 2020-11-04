package a.shshelokov.Slave;

import a.shshelokov.Message.Message;
import a.shshelokov.Packet;
import a.shshelokov.TreeNode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecvSlave implements Runnable {
    DatagramSocket socket;
    TreeNode node;
    final int BUFF_SIZE = 2048;

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
                System.out.println("from " + packet.getSocketAddress().toString() + " : " + packet.getMessage().getGUID() + "///" + packet.getMessage().getMessageType() + "///" + packet.getMessage().getName() + ": " + packet.getMessage().getMessageText());
                recvPackets.add(getPacket());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    private Packet getPacket() throws IOException, ClassNotFoundException {
        DatagramPacket recvPacket = new DatagramPacket(new byte[BUFF_SIZE], BUFF_SIZE);
        socket.receive(recvPacket);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(recvPacket.getData()));
        Message recvMessage = (Message) ois.readObject();
        return new Packet(recvPacket.getSocketAddress(), recvMessage);

    }

}
