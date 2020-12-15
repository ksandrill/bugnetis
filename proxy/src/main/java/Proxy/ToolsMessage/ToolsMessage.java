package Proxy.ToolsMessage;

import Proxy.MOD;

public abstract class ToolsMessage {
    public byte[] getData() {
        return data;
    }

    byte[] data;
      ToolsMessage(byte[] buff){
          data = buff;

      }

      static byte getCurrentMethod(MOD mod){
          byte currentMethod =0x00;
          if(mod == MOD.NO_AUTH){
              currentMethod = NO_AUTHENTICATION;
          }else if(mod == MOD.AUTH){
              currentMethod = AUTH;
          }
          return currentMethod;

      }

    public static final byte SOCKS_5 = 0x05;

    public static final byte IPv4 = (byte) 0x01;
    public static final byte IPv6 = (byte) 0x04;

    public static final byte DOMAIN_NAME = (byte) 0x03;
    public static final byte AUTH = (byte)0x02;
    public final byte X = 0x01;
    public final byte SUCCESS = 0x00;
    public final byte DENIED = 0x05;


    public static final byte COMMAND_NOT_SUPPORTED = 0x07;
    public static final byte ADDRESS_TYPE_NOT_SUPPORTED = 0x08;
    public static final byte SUCCEEDED = 0x00;
    public static final byte HOST_NOT_AVAILABLE = 0x04;
    public static final byte NO_AUTHENTICATION = 0x00;
    public static final byte NO_ACCEPTABLE_METHODS = (byte) 0xFF;
    public static final byte CONNECT_TCP = (byte) 0x01;
}
