package Proxy.ToolsMessage;

import Proxy.Connections.Connection;
import Proxy.MOD;

import java.io.IOException;

public class MessageReader extends ToolsMessage {
    MessageReader(byte[] buff) {
        super(buff);
    }

    static public Hello readHelloMessage(Connection session) throws IOException { //// читает приветствие от клиента
        /// проверяя на корректность  - вслучае успеха возвращает прочитанное сообщение
        int read_bytes = session.getClientChannel().read(session.getReadBuff());
        if (read_bytes == -1 ) {
            session.close();
            return null;
        }
        if (Hello.isCorrectSizeOfMessage(session.getReadBuff())) {
            session.setReadBuff(session.getReadBuff().flip());
            return new Hello(session.getReadBuff());
        }
        return null;
    }

    static public Negotiaion readSubNegotiation(Connection session) throws IOException {
        int read_bytes = session.getClientChannel().read(session.getReadBuff());
        if (read_bytes == -1 ) {
            System.out.println("get null");
            session.close();
            return null;
        }
        session.setReadBuff(session.getReadBuff().flip());

        return new Negotiaion(session.getReadBuff());

    }


    static public Request readRequestMessage(Connection session) throws IOException {
        int read_bytes = session.getClientChannel().read(session.getReadBuff());
        if (read_bytes == -1) {
            session.close();
            return null;
        }
        if (Request.isCorrectSizeOfMessage(session.getReadBuff())) {
            session.setReadBuff(session.getReadBuff().flip());
            return new Request(session.getReadBuff());
        }
        return null;
    }

    static public byte[] getResponse(Hello hello, MOD mod) {
        byte[] data = new byte[2];
        data[0] = SOCKS_5;
        if (!hello.hasMethod(mod)) {
            data[1] = NO_ACCEPTABLE_METHODS;
        }
        else {
            data[1] = ToolsMessage.getCurrentMethod(mod);
        }
        return data;
    }
}
