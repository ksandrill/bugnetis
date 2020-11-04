package a.shshelokov;

import a.shshelokov.Message.Message;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TreeNode {
    private final String name;
    private final int lossPercentage;
    private final DatagramSocket socket;
    private InetSocketAddress parent;
    private ConcurrentLinkedQueue<InetSocketAddress> children;
    private ConcurrentLinkedQueue<Packet> recvPackets;
    private ConcurrentLinkedQueue<Packet> packetsToSend;




    public TreeNode(String name, int port, int lossPercentage, InetSocketAddress parent) throws SocketException {
        this.name = name;
        this.socket = new DatagramSocket(port);
        this.lossPercentage = lossPercentage;
        this.parent = parent;
        this.children = new ConcurrentLinkedQueue<>();
        this.recvPackets = new ConcurrentLinkedQueue<>();
        this.packetsToSend = new ConcurrentLinkedQueue<>();

    }

    public TreeNode(String name, int port, int lossPercentage) throws SocketException {
        this.name = name;
        this.socket = new DatagramSocket(port);
        this.lossPercentage = lossPercentage;
        this.parent = null;
        this.children = new ConcurrentLinkedQueue<>();
        this.recvPackets = new ConcurrentLinkedQueue<>();
        this.packetsToSend = new ConcurrentLinkedQueue<>();

    }

    public ConcurrentLinkedQueue<Packet> getRecvPackets() {
        return recvPackets;
    }

    public ConcurrentLinkedQueue<Packet> getPacketsToSend() {
        return packetsToSend;
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


    public InetSocketAddress getParent() {
        return parent;
    }

    public ConcurrentLinkedQueue<InetSocketAddress> getChildren() {
        return children;
    }
    public boolean hasParent(){
        return parent != null;
    }
}
