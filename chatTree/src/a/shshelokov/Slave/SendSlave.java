package a.shshelokov.Slave;

import a.shshelokov.Message.Message;
import a.shshelokov.Packet;
import a.shshelokov.TreeNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SendSlave implements Runnable {
    DatagramSocket socket;
    TreeNode node;

    final int BUFF_SIZE = 2048;


    public SendSlave(TreeNode node) {
        this.socket = node.getSocket();
        this.node = node;
    }

    @Override
    public void run() {
        while (true) {
            ConcurrentLinkedQueue<Packet> packetsToSend = node.getPacketsToSend();
            try {
                Packet packet = packetsToSend.poll();
                if(packet !=null) {
                    System.out.println("send: " + packet.getMessage().getMessageText() +" to " +packet.getSocketAddress().toString());
                    sendMessage(packet.getMessage(), packet.getSocketAddress());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    void sendMessage(Message message, SocketAddress addPort) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFF_SIZE);
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(message);
        DatagramPacket packet = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, addPort);
        socket.send(packet);

    }
}


