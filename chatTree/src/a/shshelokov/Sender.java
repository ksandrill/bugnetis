package a.shshelokov;

import java.net.DatagramSocket;

public class Sender implements Runnable {
    DatagramSocket socket;
    TreeNode node;

    public Sender(TreeNode node) {
        this.socket = node.getSocket();
        this.node = node;
    }

    @Override
    public void run() {
        System.out.println("Sender body is ready");
        while (true){

        }

    }

    private void send() {

    }
}


