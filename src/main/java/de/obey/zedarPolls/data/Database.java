package de.obey.zedarPolls.data;


/*
    Author: Obey
    Date: 09.06.2026
    Time: 18:54
    Project: ZedarPolls
*/

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.obey.zedarPolls.ZedarPolls;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@RequiredArgsConstructor
public class Database {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final String hi_how_are_you_doing = "https://dsc.gg/crownplugins";

    private final ZedarPolls zedarPolls;

    private HikariDataSource hikariDataSource;

    public void initiializeConnection() {
        final File file = new File(zedarPolls.getDataFolder(), "database.db");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (final IOException ignored) {}
        }

        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.h2.Driver");
        hikariConfig.setJdbcUrl("jdbc:h2:file:" + file.getAbsolutePath());
        hikariConfig.setUsername("sa");
        hikariConfig.setPassword("");

        hikariConfig.setMaximumPoolSize(4);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setConnectionTimeout(10000);
        hikariConfig.setIdleTimeout(60000);
        hikariConfig.setMaxLifetime(300000);

        hikariConfig.setPoolName("LOL");

        hikariDataSource = new HikariDataSource(hikariConfig);

        createTables();
    }

    public void disconnectConnection() {
        hikariDataSource.close();
    }

    public Connection getConnection() throws SQLException {
        if (hikariDataSource == null || hikariDataSource.isClosed())
            throw new IllegalStateException("datassource closed");

        return hikariDataSource.getConnection();
    }

    private void createTables() {
        /*

            polls table

            id int | title varchar | question varchar | miltiple_choice boolean | state varchat | created_millis bigint | end_millis big int | author varchar

         */

        final String pollsQuery = """
                                create table if not exists polls (
                                    id int auto_increment primary key,
                                    title varchar(80),
                                    question text,
                                    multiple_choice boolean,
                                    state varchar(10),
                                    created_millis bigint,
                                    end_millis bigint,
                                    author varchar(36),
                                )
                                """;

        /*
            chouces table

            id int | poll_id int | value varchar

         */

        final String choicesQuery = """
                                create table if not exists poll_choices (
                                    id int auto_increment primary key,
                                    poll_id int,
                                    value text,
                                    constraint poll_choices_fk foreign key (poll_id) references polls(id) on delete cascade,
                                )
                                """;

        /*
            votes table

            id int | poll_id int | choice_id int | player_uuid varchar

         */

        final String votesQuery = """
                                create table if not exists poll_votes (
                                    id int auto_increment primary key,
                                    poll_id int,
                                    choice_id int,
                                    player_uuid varchar(36),
                                    constraint vote_poll_fk foreign key (poll_id) references polls(id) on delete cascade,
                                    constraint vote_choices_fk foreign key (choice_id) references poll_choices(id) on delete cascade,
                                )
                                """;

        try (final Connection connection = getConnection()) {

            final Statement statement = connection.createStatement();

            statement.execute(pollsQuery);
            statement.execute(choicesQuery);
            statement.execute(votesQuery);

        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
