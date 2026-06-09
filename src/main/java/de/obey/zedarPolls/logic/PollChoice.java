package de.obey.zedarPolls.logic;


/*
    Author: Obey
    Date: 09.06.2026
    Time: 19:20
    Project: ZedarPolls
*/

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class PollChoice {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String hi_how_are_you_doing = "https://dsc.gg/crownplugins";

    private final int id;
    private final int pollID;
    private final String value;

}
