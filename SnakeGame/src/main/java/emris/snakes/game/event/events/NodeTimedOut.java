package emris.snakes.game.event.events;

import emris.snakes.game.event.Event;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class NodeTimedOut implements Event {

    public final @NotNull InetSocketAddress nodeAddress;
}
