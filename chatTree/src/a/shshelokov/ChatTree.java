package a.shshelokov;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ChatTree {

    public static void main(String[] args) throws SocketException {
        int argsLength = args.length;
        if (argsLength != 3 && argsLength != 5) {
            System.err.println("usage: parent_name, loss_percentage, port [, parent_address, parent_port]");
            System.exit(-1);
        }
        String name = args[0];
        int loss_percentage = Integer.parseInt(args[1]);
        int port = Integer.parseInt(args[2]);
        TreeNode node = null;
        if (argsLength == 3) {
            node = new TreeNode(name, port, loss_percentage);
        } else {
            InetSocketAddress parent = new InetSocketAddress(args[3], Integer.parseInt(args[4]));
            node = new TreeNode(name, port, loss_percentage, parent);

        }
        new Thread(new Sender(node)).start();
        new Thread(new Receiver(node)).start();



    }
}
