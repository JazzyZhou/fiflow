package com.github.lessonone.fiflow.core.sql.builder.system;

import com.github.lessonone.fiflow.core.flink.BuildLevel;
import com.github.lessonone.fiflow.core.flink.FlinkBuildInfo;
import com.github.lessonone.fiflow.core.sql.Cmd;
import com.github.lessonone.fiflow.core.sql.CmdBuilder;
import com.github.lessonone.fiflow.core.sql.SqlSessionContext;
import com.github.lessonone.fiflow.core.sql.builder.CmdBaseBuilder;

/**
 * use xx
 */
public class UseBuilder extends CmdBaseBuilder implements CmdBuilder {
    public static final String pattern = "USE\\s+(?!CATALOG)(.*)";

    public UseBuilder() {
        super(pattern);
    }

    @Override
    public String help() {
        return "use xx; use database ";
    }

    @Override
    public BuildLevel buildLevel() {
        return BuildLevel.Set;
    }

    @Override
    public FlinkBuildInfo build(FlinkBuildInfo result, Cmd cmd, SqlSessionContext session) {
        String database = cmd.args[0];

        boolean has = false;
        for (String t : session.tEnv.listDatabases()) {
            if (database.equalsIgnoreCase(t)) {
                has = true;
            }
        }

        if (has == false)
            throw new IllegalArgumentException("database not exist " + database);

        result.addMsg("use database " + database);

        return result;
    }
}
