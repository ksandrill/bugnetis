package Proxy;

import Proxy.Connections.Handler;
import Proxy.Connections.Server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Set;

public class Proxy implements AutoCloseable,Runnable {
    private Selector selector = Selector.open();
    private Server server;
    private final int TIMEOT = 10000;






    public Proxy(int port, HashMap<String,String> users,MOD mod) throws IOException {
        server = new Server(port,selector,users,mod);



    }

    public Proxy(int port) throws IOException {
        server = new Server(port,selector);
    }


    @Override
    public void close() throws Exception {
        selector.close();
        server.close();
        server.closeDNS();
    }

    @Override
    public void run() {

            while (!Thread.currentThread().isInterrupted()) {
                int count =0;
                try {
                   count = selector.select(TIMEOT);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (count == 0) {
                   continue;
                }; //// если ни на одном из каналов,прослушиваемом селектором не произошло - скип
                Set<SelectionKey> modified = selector.selectedKeys();
                for (SelectionKey selected : modified) {
                    Handler key  =  (Handler)selected.attachment();
                    ////interface Conectionhandler- implements by Server/Server/DNS
                    key.accept(selected);
                    ///проходимся по  ключам, там где произошли какие-то события и чекаем эти события

                }
                modified.clear();
            }
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
