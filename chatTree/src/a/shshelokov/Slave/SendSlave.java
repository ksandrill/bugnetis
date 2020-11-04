package a.shshelokov.Slave;

import a.shshelokov.Message.Message;
import a.shshelokov.Message.MessageType;
import a.shshelokov.Packet;
import a.shshelokov.TreeNode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.UUID;
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
        ConcurrentLinkedQueue<Packet> packetsToSend = node.getPacketsToSend();
        ConcurrentLinkedQueue<Packet>SavedPacketsToSend = node.getSavedPacketsToSend();
        if(node.hasParent()){
            Message adoptMessage = new Message(MessageType.ADOPT_CHILD_MESSAGE,node.getName(),"Adopt me, my lord", UUID.randomUUID());
            Packet  adoptPacket = new Packet(node.getParent(),adoptMessage);
            packetsToSend.add(adoptPacket);
        }
        while (true) {
            try {
                Packet packet = packetsToSend.poll();
                if(packet !=null) {
                    SavedPacketsToSend.add(packet);
                    System.out.println("send: " + packet.getMessage().getMessageText() +" to " +packet.getInetSocketAddress().toString());
                    sendMessage(packet.getMessage(), packet.getInetSocketAddress());
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


