package a.shshelokov.Master;

import a.shshelokov.Message.Message;
import a.shshelokov.Message.MessageType;
import a.shshelokov.Packet;
import a.shshelokov.TreeNode;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecvMaster implements Runnable {
    TreeNode node;


    public RecvMaster(TreeNode node) {
        this.node = node;

    }

    @Override
    public void run() {
        ConcurrentLinkedQueue<Packet> recvMessage = node.getRecvPackets();
        ConcurrentLinkedQueue<Packet> SavedPacketsToSend = node.getSavedPacketsToSend();
        ConcurrentLinkedQueue<Packet> packetsToSend = node.getPacketsToSend();
        while (true) {
            Packet packet = recvMessage.poll();
            if (packet != null) {
                if(packet.getMessage().getMessageType() != MessageType.PING_MESSAGE) {
                    System.out.println("recv::" + packet.getMessage().getMessageText() + "////" + packet.getInetSocketAddress().toString());
                }
                switch (packet.getMessage().getMessageType()) {
                    case ACCEPT_CHILD_MESSAGE -> {
                        node.setParent(packet.getInetSocketAddress());
                        SavedPacketsToSend.removeIf(item -> item.getMessage().getGUID().equals(packet.getMessage().getGUID()));
                        System.out.println("I'm got parent now! it's :" + node.getParent().toString());
                    }
                    case ADOPT_CHILD_MESSAGE -> {
                        node.addChild(packet.getInetSocketAddress());
                        answerToPacket(packet, MessageType.ACCEPT_CHILD_MESSAGE, "I (" + node.getName() + ") accept you", packetsToSend, Packet.ACCEPT_CHILD_TTL);
                    }
                    case CHAT_MESSAGE -> {
                        answerToPacket(packet, MessageType.ACCEPT_CHAT_MESSAGE, "I (" + node.getName() + ") accept message from you", packetsToSend, Packet.ACCEPT_CHAT_MESSAGE_TTL);
                        spreadPacket(packet, packetsToSend, Packet.CHAT_MESSAGE_TTL);
                    }
                    case ACCEPT_CHAT_MESSAGE -> {
                        SavedPacketsToSend.removeIf(item -> item.getMessage().getGUID().equals(packet.getMessage().getGUID()));
                        System.out.println(packet.getInetSocketAddress().toString() + "//" + packet.getMessage().getName() + " got my message");
                    }
                    case PING_MESSAGE -> node.updateRelatives(packet.getInetSocketAddress());
                }


            }
        }


    }

    private void answerToPacket(Packet packet, MessageType type, String text, ConcurrentLinkedQueue<Packet> packetsToSend, int ttl) {
        Message message = new Message(type, node.getName(), text, packet.getMessage().getGUID());
        Packet answerPacket = new Packet(packet.getInetSocketAddress(), message,ttl);
        packetsToSend.add(answerPacket);
    }

    private void spreadPacket(Packet packet, ConcurrentLinkedQueue<Packet> packetsToSend, int ttl) {
        InetSocketAddress src = packet.getInetSocketAddress();
        ConcurrentLinkedQueue<InetSocketAddress> children = node.getChildren();
        Packet packetToSpread;
        if (node.hasParent()) {
            if (!src.equals(node.getParent())) {
                packetToSpread = new Packet(node.getParent(), packet.getMessage(),ttl);
                packetsToSend.add(packetToSpread);
            }

        }
        for(InetSocketAddress sendAddr : children){
            if(!src.equals(sendAddr)){
                packetToSpread = new Packet(sendAddr,packet.getMessage(),ttl);
                packetsToSend.add(packetToSpread);
            }

        }


    }
}
