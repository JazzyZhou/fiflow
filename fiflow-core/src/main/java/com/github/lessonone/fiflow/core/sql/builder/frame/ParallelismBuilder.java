package com.github.lessonone.fiflow.core.sql.builder.frame;

import com.github.lessonone.fiflow.core.flink.BuildLevel;
import com.github.lessonone.fiflow.core.flink.FlinkBuildInfo;
import com.github.lessonone.fiflow.core.sql.Cmd;
import com.github.lessonone.fiflow.core.sql.CmdBuilder;
import com.github.lessonone.fiflow.core.sql.SqlSessionContext;
import com.github.lessonone.fiflow.core.sql.builder.CmdBaseBuilder;
import org.apache.flink.table.api.config.ExecutionConfigOptions;

/**
 * -p n
 * 设置任务的并发度
 */
public class ParallelismBuilder extends CmdBaseBuilder implements CmdBuilder {
    public static final String pattern = "-*\\s?-p\\s+(\\d)";

    public ParallelismBuilder() {
        super(pattern);
    }

    @Override
    public String help() {
        return "-p n; set job parallelism";
    }

    @Override
    public BuildLevel buildLevel() {
        return BuildLevel.Set;
    }

    @Override
    public FlinkBuildInfo build(FlinkBuildInfo result, Cmd cmd, SqlSessionContext session) {
        String p = cmd.args[0];
        result.addMsg("set parallelism " + p);
        Integer pp = Integer.parseInt(p);

        session.tEnv.getConfig().getConfiguration()
                .set(ExecutionConfigOptions.TABLE_EXEC_RESOURCE_DEFAULT_PARALLELISM, pp);

        return result;
    }
}
