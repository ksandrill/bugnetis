package Proxy.Connections;

import Proxy.MOD;
import Proxy.ToolsMessage.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashMap;

public class Connection implements SocketHandler {
    private MOD mod;
    private SocketChannel serverChannel = null;
    private HashMap<String,String> users;
    public SocketChannel getClientChannel() {
        return clientChannel;
    }

    private SocketChannel clientChannel;


    private DNS dns;
    private static final int BUFFER_SIZE = 4096;

    private State state = State.HELLO;


    public ByteBuffer getReadBuff() {
        return readBuff;
    }

    public void setReadBuff(ByteBuffer readBuff) {
        this.readBuff = readBuff;
    }

    private ByteBuffer readBuff = ByteBuffer.allocateDirect(BUFFER_SIZE);
    private ByteBuffer writeBuff = null;

    private Hello hello = null;
    private Request request = null;
    private ResponseOnRequest response = null;
    private Negotiaion negotiaion = null;

    public Connection(SocketChannel aux_client, DNS aux_dns,Selector selector,HashMap<String,String> users,MOD aux_mod) throws IOException {
        dns = aux_dns;
        this.users = users;
        clientChannel = aux_client;
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ, this);
        mod = aux_mod;


    }

    @Override
    public void accept(SelectionKey key) {
        try {
            if (!key.isValid()) {
                close();
                key.cancel();
                return;
            }
            if (key.isReadable()) {
                read(key);
            } else if (key.isWritable()) {
                write(key);
            } else if (key.isConnectable() && key.channel() == serverChannel) {
                serverConnect(key);
            }


        } catch (IOException ex) {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void close() throws IOException {
     if(clientChannel !=null)   clientChannel.close();
       if(serverChannel != null) serverChannel.close();
    }


    @Override
    public void read(SelectionKey key) throws IOException {
        if (key.channel() == clientChannel) {
            clientRead(key);

        } else if (key.channel() == serverChannel) {
            serverRead(key);

        }

    }


    private void clientRead(SelectionKey key) throws IOException {
        switch (state) {
            case HELLO: {
                System.out.println("Get hello");
                hello = MessageReader.readHelloMessage(this);
                if (hello == null) return;
                key.interestOps(SelectionKey.OP_WRITE);

                readBuff.clear();
                break;
            }
            case NEGOTIATION:{
                System.out.println("get Client negotiation");
                negotiaion = MessageReader.readSubNegotiation(this);
                if (negotiaion == null) return;
                System.out.println(Arrays.toString(negotiaion.getData()));
                key.interestOps(SelectionKey.OP_WRITE);
                readBuff.clear();
                break;



            }
            case REQUEST: {
                System.out.println("Get request");
                request = MessageReader.readRequestMessage(this);
                if (request == null) return;
                if (!connect()) {
                    serverChannel = null;
                    key.interestOps(SelectionKey.OP_WRITE);
                } else {
                    serverChannel.register(key.selector(), SelectionKey.OP_CONNECT, this);
                    key.interestOps(0);
                }
                readBuff.clear();
                break;
            }
            case MESSAGE: {
                if (this.readFrom(clientChannel, readBuff)) {
                    serverChannel.register(key.selector(), SelectionKey.OP_WRITE, this);
                    key.interestOps(0);
                }
                break;
            }
        }

    }

    private void serverRead(SelectionKey key) throws IOException {
        if (readFrom(serverChannel, readBuff)) {
            clientChannel.register(key.selector(), SelectionKey.OP_WRITE, this);
            key.interestOps(0);
        }


    }

    private void serverConnect(SelectionKey key) throws IOException {
        if (!serverChannel.isConnectionPending()) return;
        if (!serverChannel.finishConnect()) return;
        key.interestOps(0);
        clientChannel.register(key.selector(), SelectionKey.OP_WRITE, this);

    }

    @Override
    public void write(SelectionKey key) throws IOException {
        if (key.channel() == clientChannel) {
            clientWrite(key);

        } else if (key.channel() == serverChannel) {
            serverWrite(key);

        }

    }

    private void clientWrite(SelectionKey key) throws IOException {
        switch (state) {
            case HELLO: {
                if (writeBuff == null) {
                    writeBuff = ByteBuffer.wrap(MessageReader.getResponse(hello,mod));
                }
                if (writeTo(clientChannel, writeBuff)) {
                    writeBuff = null;
                    if (hello.hasMethod(mod)) {
                        key.interestOps(SelectionKey.OP_READ);
                            state = mod == MOD.AUTH?State.NEGOTIATION:State.REQUEST;

                    } else {
                        System.err.println("Not support ffs");
                        this.close();
                    }
                    hello = null;
                }
                break;
            }
            case NEGOTIATION:{
                if(writeBuff == null){
                    negotiaion.response(users);
                    writeBuff = ByteBuffer.wrap(negotiaion.getResponce());
                }
                if(writeTo(clientChannel, writeBuff)) {
                    writeBuff = null;
                    if (negotiaion.hasSuccess()) {
                        key.interestOps(SelectionKey.OP_READ);
                        state = State.REQUEST;
                    } else {
                        System.err.println("DENIED");
                        this.close();
                    }
                    negotiaion = null;
                }
                break;






            }
            case REQUEST: {
                if (writeBuff == null) {
                    response = new ResponseOnRequest(request);
                    writeBuff = ByteBuffer.wrap(response.create(serverChannel != null)); //
                }
                if (writeTo(clientChannel, writeBuff)) {
                    writeBuff = null;
                    if (!request.isCommand(Request.CONNECT_TCP) || serverChannel == null) {
                        this.close();
                        System.out.println("Not support, please conntect TCP or dafaq");
                    } else {
                        key.interestOps(SelectionKey.OP_READ);
                        serverChannel.register(key.selector(), SelectionKey.OP_READ, this);
                        state = State.MESSAGE;
                    }
                    request = null;
                }
                break;
            }
            case MESSAGE: {
                if (writeTo(clientChannel, readBuff)) {
                    key.interestOps(SelectionKey.OP_READ);
                    serverChannel.register(key.selector(), SelectionKey.OP_READ, this);
                }
                break;
            }
        }

    }

    private void serverWrite(SelectionKey key) throws IOException {
        if (writeTo(serverChannel, readBuff)) {
            key.interestOps(SelectionKey.OP_READ);
            clientChannel.register(key.selector(), SelectionKey.OP_READ, this);
        }


    }




    public boolean connectToServer(InetAddress address) {
        System.out.println("Connect with  " + address);
        try {
            serverChannel.connect(new InetSocketAddress(address, request.getDestPort()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean connect() throws IOException {
        serverChannel = SocketChannel.open();
        serverChannel.configureBlocking(false);
        switch (request.getAddressType()) {
            case Request.IPv4: {
                return connectToServer(InetAddress.getByAddress(request.getDestAddress()));
            }
            case Request.IPv6: {
                System.err.println("It's a IPv6");
                return false;
            }


            case Request.DOMAIN_NAME: {
                dns.sendToResolve(new String(request.getDestAddress()), this);
                break;
            }


        }
        return true;
    }

    private boolean readFrom(SocketChannel channel, ByteBuffer buffer) throws IOException {
        buffer.compact();
        int read_bytes = channel.read(buffer);
        if (read_bytes == -1) {
            this.close();
            return false;
        }
        if (read_bytes != 0) {
            buffer.flip();
        }
        return read_bytes != 0;
    }

    private boolean writeTo(SocketChannel channel, ByteBuffer buffer) throws IOException {
        channel.write(buffer);
        return !buffer.hasRemaining();
    }

    private enum State {
        HELLO,
        REQUEST,
        MESSAGE,
        NEGOTIATION;
    }
}
