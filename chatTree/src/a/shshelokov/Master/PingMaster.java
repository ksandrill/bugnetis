package a.shshelokov.Master;

import a.shshelokov.Message.Message;
import a.shshelokov.Message.MessageType;
import a.shshelokov.Packet;
import a.shshelokov.TreeNode;

import java.net.InetSocketAddress;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PingMaster extends TimerTask {
    TreeNode node;
    final long DEAD_TIME = 10_000_000_000L;

    public PingMaster(TreeNode node) {
        this.node = node;
    }

    @Override
    public void run() {
        System.out.println("PING ACTIVATED");
        pingNodes();
        checkPing();
        checkAlterNode();


    }


    void pingNodes() {
        var children = node.getChildren();
        var packetsToSend = node.getPacketsToSend();
        Packet pingPacket;
        Message message;
        for (InetSocketAddress child : children) {
            message = new Message(MessageType.PING_MESSAGE, node.getName(), "I'm stil alive", UUID.randomUUID());
            pingPacket = new Packet(child, message, Packet.PING_TTL);
            packetsToSend.add(pingPacket);
        }
        if (node.hasParent()) {
            message = new Message(MessageType.PING_MESSAGE, node.getName(), "I'm stil alive", UUID.randomUUID());
            pingPacket = new Packet(node.getParent(), message, Packet.PING_TTL);
            packetsToSend.add(pingPacket);

        }

    }

    void checkPing() {
        boolean parentIsDead = false;
        for (InetSocketAddress item : node.getRelatives().keySet()) {
            long delta =System.nanoTime() - node.getRelatives().get(item);
            var aux  = TimeUnit.SECONDS.convert(delta,TimeUnit.NANOSECONDS);
            System.out.println(item.toString() + " was seen " + aux + "s  ago");
            if (delta > DEAD_TIME) {
                parentIsDead = node.forgetRelative(item);
                System.out.println(item.toString() + " is dead");

            }
        }
        if (parentIsDead) {
            System.out.println("parent is dead");
            node.setParent(node.getFosterParent());
            node.setFosterParent(null);
            InetSocketAddress nodeAddr = new InetSocketAddress(node.getSocket().getInetAddress(), node.getSocket().getLocalPort());
            Message adoptMessage = new Message(MessageType.ADOPT_CHILD_MESSAGE, node.getName(), nodeAddr.toString(), UUID.randomUUID());
            Packet adoptPacket = new Packet(node.getParent(), adoptMessage, Packet.ADOPT_CHILD_TTL);
            node.getPacketsToSend().add(adoptPacket);
        }
    }

    void checkAlterNode() {
        var children = node.getChildren();
        var alterNode = node.getAlterNode();
        var packetsToSend = node.getPacketsToSend();
        if (!children.contains(alterNode)) {
            node.setAlterNode(children.peek());
            if (alterNode != null) {
                Message message = new Message(MessageType.SEND_FOSTER_MESSAGE, node.getName(), alterNode.toString(), UUID.randomUUID());
                Packet packet = new Packet(alterNode, message, Packet.SEND_FOSTER_TTL);
                Packet.spreadPacket(packet, packetsToSend, Packet.SEND_FOSTER_TTL, node, true);
            }
        }
    }


}





