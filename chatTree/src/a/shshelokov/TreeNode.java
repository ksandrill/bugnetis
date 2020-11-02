package a.shshelokov;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TreeNode {
    private final String name;
    private final int lossPercentage;
    private final DatagramSocket socket;
    private boolean isRoot;
    private InetSocketAddress parent;
    private ConcurrentLinkedQueue<InetSocketAddress> children;


    public TreeNode(String _name, int _port, int _lossPercentage, InetSocketAddress _parent) throws SocketException {
        this.name = _name;
        this.socket = new DatagramSocket(_port);
        this.lossPercentage = _lossPercentage;
        this.parent = _parent;
        this.isRoot = false;
        this.children = new ConcurrentLinkedQueue<>();
    }

    public TreeNode(String _name, int _port, int _lossPercentage) throws SocketException {
        this.name = _name;
        this.socket = new DatagramSocket(_port);
        this.lossPercentage = _lossPercentage;
        this.parent = null;
        this.isRoot = true;
        this.children = new ConcurrentLinkedQueue<>();

    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public void setParent(InetSocketAddress parent) {
        this.parent = parent;
    }

    public void setChildren(ConcurrentLinkedQueue<InetSocketAddress> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public int getLossPercentage() {
        return lossPercentage;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public InetSocketAddress getParent() {
        return parent;
    }

    public ConcurrentLinkedQueue<InetSocketAddress> getChildren() {
        return children;
    }
}
