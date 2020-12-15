package Proxy.ToolsMessage;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class Negotiaion extends ToolsMessage {

    public byte[] getResponce() {
        return responce;
    }

    private byte[] responce;

    Negotiaion(ByteBuffer negotiationRequest) {
        super(new byte[negotiationRequest.limit()]);
        negotiationRequest.get(data);
        if ((data[0]) != X) {
            throw new IllegalArgumentException();
        }

    }

    public boolean hasSuccess() {
        return responce[0] == X && responce[1] == SUCCESS;
    }

    public void response(HashMap<String, String> map) {
        byte[] login = new byte[data[1]];

        responce = new byte[2];
        responce[0] = X;
        responce[1] = SUCCESS;
        for (int i = 0; i < data[1]; ++i) {
            login[i] = data[i + 2];

        }
        String loginString = new String(login);


        int plen = data[data[1] + 2];
        int pstart = data[1] + 2;

        byte[] password = new byte[plen];
        for (int i = 0; i < plen; ++i) {
            password[i] = data[pstart + 1 + i];

        }
        String passwordString = new String(password);
        System.out.println("Password: " + passwordString);

        if (!map.containsKey(loginString)) {
            responce[1] = DENIED;

        }
        if (responce[1] != DENIED) {
            if (!map.get(loginString).equals(passwordString)) {
                responce[1] = DENIED;
            }


        }


    }


}
