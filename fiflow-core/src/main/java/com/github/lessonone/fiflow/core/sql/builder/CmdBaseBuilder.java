package com.github.lessonone.fiflow.core.sql.builder;

import com.github.lessonone.fiflow.core.flink.FlinkBuildInfo;
import com.github.lessonone.fiflow.core.sql.Cmd;
import com.github.lessonone.fiflow.core.sql.CmdBuilder;
import com.github.lessonone.fiflow.core.sql.SqlSessionContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class CmdBaseBuilder implements CmdBuilder {
    public final Pattern regPattern;

    public CmdBaseBuilder(String p) {
        this.regPattern = Pattern.compile(p, DEFAULT_PATTERN_FLAGS);
    }

    public static String readText(String fileName) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = CmdBaseBuilder.class.getClassLoader().getResourceAsStream(fileName);
            List<String> lines = IOUtils.readLines(inputStream);
            return StringUtils.join(lines, "\n");
        } catch (IOException e) {
            throw new IOException("file " + fileName + " not found");
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public FlinkBuildInfo build(Cmd cmd, SqlSessionContext sessionContext) {
        return build(new FlinkBuildInfo(buildLevel()), cmd, sessionContext);
    }

    public abstract FlinkBuildInfo build(FlinkBuildInfo result, Cmd cmd, SqlSessionContext sessionContext);

    @Override
    public Optional<String[]> accept(String sql) {
        if (StringUtils.isBlank(sql) || regPattern == null) return Optional.empty();
        final Matcher matcher = regPattern.matcher(sql);
        if (matcher.matches()) {
            final String[] groups = new String[matcher.groupCount()];
            for (int i = 0; i < groups.length; i++) {
                groups[i] = matcher.group(i + 1);
            }
            return Optional.of(groups);
        }
        return Optional.empty();
    }

}
