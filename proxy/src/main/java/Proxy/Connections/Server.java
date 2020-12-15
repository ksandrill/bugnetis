package Proxy.Connections;

import Proxy.MOD;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;

public class Server implements Handler {

    private MOD mod;
    private ServerSocketChannel serverChannel = ServerSocketChannel.open();
    private DNS dns;
    private HashMap<String,String> users;
    public Server(int port,Selector selector,HashMap<String,String> users,MOD mod) throws IOException {
        this.users = users;
        dns = new DNS(port, selector);
        serverChannel.bind( new InetSocketAddress(port)); /// айпишник+ порт
        serverChannel.configureBlocking(false);/// снимаем блокировку( неблокирующий ввод/вывод )
        serverChannel.register(selector, SelectionKey.OP_ACCEPT, this);///цепляем сервер к селектору(теперь селектор слушает этот канал)
        this.mod = mod;

    }

    public Server(int port, Selector selector) throws IOException {
        dns = new DNS(port, selector);
        serverChannel.bind( new InetSocketAddress(port)); /// айпишник+ порт
        serverChannel.configureBlocking(false);/// снимаем блокировку( неблокирующий ввод/вывод )
        serverChannel.register(selector, SelectionKey.OP_ACCEPT, this);///цепляем сервер к селектору(теперь селектор слушает этот канал)
        this.mod = MOD.NO_AUTH;

    }


    public void closeDNS() throws IOException {
        dns.close();
    }
    @Override
    public void close() throws IOException {
        serverChannel.close();

    }

    @Override
    public void accept(SelectionKey key) {
        try {
            if (!key.isValid()) {
                /// A key is valid upon creation and remains so until it is cancelled,
                // its channel is closed, or its
                // selector is closed.
                close();
                return;
            }
            new Connection(serverChannel.accept(), dns,key.selector(),users,mod);
            ///создаем новое подключение
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

    }



}
