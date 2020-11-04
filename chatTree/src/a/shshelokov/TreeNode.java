package a.shshelokov;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TreeNode {
    private final String name;
    private final int lossPercentage;
    private final DatagramSocket socket;
    private InetSocketAddress parent;
    private ConcurrentLinkedQueue<InetSocketAddress> children = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Packet> recvPackets = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Packet> packetsToSend = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Packet> savedPacketsToSend = new ConcurrentLinkedQueue<>();
    private ConcurrentHashMap<SocketAddress, LocalTime> relatives = new ConcurrentHashMap<>();


    public TreeNode(String name, int port, int lossPercentage, InetSocketAddress parent) throws SocketException {
        this.name = name;
        this.socket = new DatagramSocket(port);
        this.lossPercentage = lossPercentage;
        this.parent = parent;


    }

    public TreeNode(String name, int port, int lossPercentage) throws SocketException {
        this.name = name;
        this.socket = new DatagramSocket(port);
        this.lossPercentage = lossPercentage;
        this.parent = null;


    }

    public void addChild(InetSocketAddress child){
        children.add(child);
        relatives.put(child,LocalTime.now());

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


    public ConcurrentLinkedQueue<Packet> getSavedPacketsToSend() {
        return savedPacketsToSend;
    }

    public ConcurrentHashMap<SocketAddress, LocalTime> getRelatives() {
        return relatives;
    }

    public boolean hasParent() {
        return parent != null;
    }
}
