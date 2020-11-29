package Network.RecvUtils;

import Network.protocol.MessageHandler;
import Network.protocol.Packet;
import Network.protocol.SnakesProto;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RecvMaster implements Runnable {
    private ConcurrentLinkedQueue<Packet> recvPackets;

    public RecvMaster(ConcurrentLinkedQueue<Packet> recvPackets) {
        this.recvPackets = recvPackets;
    }

    @Override
    public void run() {
        while (true) {
            Packet packet = recvPackets.poll();
            if (packet != null) {
                handle(packet.getMessage(), packet.getAddrPort());

            }


        }


    }

    private void handle(SnakesProto.GameMessage message, InetSocketAddress messageSrc) {
        switch (message.getTypeCase()) {
            case ACK: {
                break;
            }
            //новый игрок хочет присоединиться. Проверяем, есть ли свободное место. Если есть - добавляем, иначе ошибка операции
            case JOIN: {
                break;

            }
            //говорим, что мы живы ответом на это сообщение
            case PING: {
                break;

            }

            //ошибка операции. Нужно вывести строку с ошибкой
            case ERROR: {
                break;

            }
            //состояние игры от центр. Обновить свою инфу для правильного вывода
            case STATE: {
                break;

            }
            //поворот головы змеи от нецентр. Нужно обновить инфу для правильной отрисовки
            case STEER: {
                break;

            }
            //смена роли одного из игроков
            case ROLE_CHANGE: {
                break;

            }
            //уведомление об идущей игре. Просто вывести инфу об игре
            case ANNOUNCEMENT: {
                MessageHandler.handleAnnouncementMessage(message);
                break;
            }
            //без типа - вывести какую-нибудь ошибку
            case TYPE_NOT_SET: {
                break;

            }

        }

    }
}
