package de.obey.zedarPolls.logic;


/*
    Author: Obey
    Date: 09.06.2026
    Time: 19:29
    Project: ZedarPolls
*/

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.obey.zedarPolls.data.Database;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class PollHandler {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String hi_how_are_you_doing = "https://dsc.gg/crownplugins";

    public final Database database;

    private final Map<Integer, Poll> polls = Maps.newHashMap();

    public void loadPolls() {
        //polls.clear(); maybe ? depends on other factors

        try (final Connection conn = database.getConnection();) {
            final PreparedStatement statement = conn.prepareStatement("select * from polls");
            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                final Poll poll = new Poll();

                poll.setId(resultSet.getInt("id"));

                poll.setTitle(resultSet.getString("title"));
                poll.setQuestion(resultSet.getString("question"));

                poll.setMultipleChoice(resultSet.getBoolean("multiple_choice"));
                poll.setState(Poll.State.from(resultSet.getString("state")));

                poll.setCreatedMillis(resultSet.getLong("started_millis"));
                poll.setEndMillis(resultSet.getLong("end_millis"));

                poll.setAuthor(UUID.fromString(resultSet.getString("author")));

                try (final Connection ndConn = database.getConnection()) {
                    final PreparedStatement ndStatement = conn.prepareStatement("select * from poll_choices where poll_id = ?");
                    ndStatement.setInt(1, poll.getId());

                    final ResultSet ndResultSet = statement.executeQuery();
                    while (ndResultSet.next()) {
                        final int pollChoiceID = ndResultSet.getInt("id");
                        poll.getChoices().put(pollChoiceID,
                                new PollChoice(
                                        pollChoiceID,
                                        ndResultSet.getInt("poll_id"),
                                        ndResultSet.getString("value")
                                )
                        );
                    }
                }

                polls.put(poll.getId(), poll);
            }

        } catch (final SQLException e) {}
    }

    public void loadVotes() {
        try (final Connection conn = database.getConnection();) {
            final PreparedStatement statement = conn.prepareStatement("select * from poll_votes");
            final ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                final int pollID =  resultSet.getInt("poll_id");
                final Poll poll = polls.get(pollID);

                final PollVote pollVote = new PollVote(
                        resultSet.getInt("id"),
                        pollID,
                        poll.getChoices().get(resultSet.getInt("choice_id")),
                        UUID.fromString(resultSet.getString("player_uuid"))
                );

                final List<PollVote> pollVotes = poll.getVotes().containsKey(pollVote.getPlayerUUID()) ? poll.getVotes().get(pollVote.getPlayerUUID()) : Lists.newArrayList();
                pollVotes.add(pollVote);

                poll.getVotes().put(pollVote.getPlayerUUID(), pollVotes);
            }

        } catch (final SQLException e) {}
    }

}
