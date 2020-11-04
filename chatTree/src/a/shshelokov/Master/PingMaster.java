package a.shshelokov.Master;

import a.shshelokov.Message.Message;
import a.shshelokov.Message.MessageType;
import a.shshelokov.Packet;
import a.shshelokov.TreeNode;

import java.net.InetSocketAddress;
import java.util.TimerTask;
import java.util.UUID;

public class PingMaster extends TimerTask {
    TreeNode node;

    public PingMaster(TreeNode node) {
        this.node = node;
    }

    @Override
    public void run() {
        pingNodes();

    }


    void pingNodes(){
        var children = node.getChildren();
        var packetsToSend = node.getPacketsToSend();
        Packet pingPacket;
        Message message;
        for (InetSocketAddress child : children) {
            message = new Message(MessageType.PING_MESSAGE, node.getName(), "I'm stil alive", UUID.randomUUID());
            pingPacket = new Packet(child, message);
            packetsToSend.add(pingPacket);
        }
        if (node.hasParent()) {
            message = new Message(MessageType.PING_MESSAGE, node.getName(), "I'm stil alive", UUID.randomUUID());
            pingPacket = new Packet(node.getParent(), message);
            packetsToSend.add(pingPacket);

        }

    }

}





