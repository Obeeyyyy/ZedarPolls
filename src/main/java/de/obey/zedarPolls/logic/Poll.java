package de.obey.zedarPolls.logic;


/*
    Author: Obey
    Date: 09.06.2026
    Time: 18:44
    Project: ZedarPolls
*/

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Poll {

    public enum State {
        CREATION,ACTIVE, ENDED;

        public static State from(final String value) {
            return switch (value.toUpperCase()) {
                case "CREATION"      -> CREATION;
                case "ACTIVE"    -> ACTIVE;
                case "ENDED" -> ENDED;
                default -> throw new IllegalArgumentException("invalid: " + value);
            };
        }
    }


    private int id;

    private String title;
    private String question;

    private boolean multipleChoice;
    private State state;

    private long createdMillis;
    private long endMillis;

    private UUID author;

    private final Map<Integer, PollChoice> choices = Maps.newConcurrentMap();
    private final Map<UUID, List<PollVote>> votes = Maps.newConcurrentMap();

    public boolean isExpired() {
        return System.currentTimeMillis() >= endMillis;
    }

    public boolean isOpen() {
        return state == State.ACTIVE && !isExpired();
    }

}
