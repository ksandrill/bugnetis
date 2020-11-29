package Network.protocol;

public class MessageFactory {
   public static SnakesProto.GameMessage createAnnouncementMsg(SnakesProto.GameConfig config, SnakesProto.GamePlayers gamePlayers) {
        SnakesProto.GameMessage.AnnouncementMsg announcementMsgBuilder = SnakesProto.GameMessage.AnnouncementMsg.newBuilder()
                .setCanJoin(true)
                .setPlayers(gamePlayers)
                .setConfig(config)
                .build();

        return SnakesProto.GameMessage.newBuilder()
                .setMsgSeq(0)
                .setSenderId(0)
                .setReceiverId(0)
                .setAnnouncement(announcementMsgBuilder)
                .build();
    }

    public static SnakesProto.GameMessage.SteerMsg createSteerMsg() {

        return null;
    }

    public static SnakesProto.GameMessage.StateMsg createStateMsg() {

        return null;
    }


    public static SnakesProto.GameMessage.ErrorMsg createErrorMsg() {

        return null;
    }

    public static SnakesProto.GameMessage.AckMsg createStateAckMsg() {

        return null;
    }


    public  static SnakesProto.GameMessage.PingMsg createStatePingMsg() {
//        SnakesProto.GameMessage message = SnakesProto.GameMessage.newBuilder()
//                .setMsgSeq(incState())
//                .setPing(GameMessage.PingMsg.newBuilder().build())
//                .setSenderId(uuid.hashCode())
//                .setReceiverId(to.getId())
//                .build();

        return null;
    }


    public static SnakesProto.GameMessage.JoinMsg createStateJoinMsg() {

        return null;
    }

    public static SnakesProto.GameMessage.RoleChangeMsg createStateRoleChangeMsg() {

        return null;
    }


}
