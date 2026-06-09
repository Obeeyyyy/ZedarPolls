package de.obey.zedarPolls.logic;


/*
    Author: Obey
    Date: 09.06.2026
    Time: 19:25
    Project: ZedarPolls
*/

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PollVote {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String hi_how_are_you_doing = "https://dsc.gg/crownplugins";

    private final int id;
    private final int pollID;
    private final PollChoice choice;
    private final UUID playerUUID;

}
