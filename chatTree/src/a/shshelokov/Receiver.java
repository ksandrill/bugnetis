package a.shshelokov;

import java.net.DatagramSocket;

public class Receiver implements Runnable {
    DatagramSocket socket;
    TreeNode node;

    public Receiver(TreeNode node) {
        this.socket = node.getSocket();
        this.node = node;
    }

    @Override
    public void run() {
        System.out.println("Receiver body is ready");
        while (true){

        }

    }
}
