package emris.snakes.game.event.events;

import emris.snakes.game.event.Event;
import emris.snakes.Network.message.AddressedMessage;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class OutgoingMessage implements Event {

    public final @NotNull AddressedMessage message;
}
