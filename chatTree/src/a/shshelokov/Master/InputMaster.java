package a.shshelokov.Master;

import a.shshelokov.Message.Message;
import a.shshelokov.Message.MessageType;
import a.shshelokov.Packet;
import a.shshelokov.TreeNode;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class InputMaster implements Runnable {
    Scanner inputReader;
    TreeNode node;
    final int TTL = 4;

    public InputMaster(TreeNode node) {
        this.node = node;
        inputReader = new Scanner(System.in);
    }

    @Override
    public void run() {
        ConcurrentLinkedQueue<InetSocketAddress> children = node.getChildren();
        ConcurrentLinkedQueue<Packet> packetsToSend = node.getPacketsToSend();
        while (true) {
            Message msg = new Message(MessageType.CHAT_MESSAGE, node.getName(), inputReader.nextLine(), UUID.randomUUID());
           //// System.out.println("(from this node)" + msg.getGUID() + "///" + msg.getMessageType() + "///" + msg.getName() +": " +  msg.getMessageText());
            for (InetSocketAddress sendAddr : children) {
                packetsToSend.add(new Packet(sendAddr, msg,TTL));
            }
            if(node.hasParent()){
                packetsToSend.add(new Packet(node.getParent(),msg,TTL));
            }
        }


    }
}
