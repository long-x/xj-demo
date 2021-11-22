package com.ecdata.cmp.common.auditlog;

import com.github.structlog4j.IToLog;
import lombok.Builder;
import lombok.Data;

/**
 * @author honglei
 * @since 2019-08-19
 */
@Data
@Builder
public class LogEntry implements IToLog {
    /**
     * currentUserId
     */
    private String currentUserId;
    /**
     * companyId
     */
    private String companyId;
    /**
     * teamId
     */
    private String teamId;
    /**
     * authorization
     */
    private String authorization;
    /**
     * targetType
     */
    private String targetType;
    /**
     * targetId
     */
    private String targetId;
    /**
     * originalContents
     */
    private String originalContents;
    /**
     * updatedContents
     */
    private String updatedContents;

    /**
     * @return LogEntry
     */
    @Override
    public Object[] toLog() {
        return new Object[]{
                "auditlog", "true",
                "currentUserId", currentUserId,
                "companyId", companyId,
                "teamId", teamId,
                "authorization", authorization,
                "targetType", targetType,
                "targetId", targetId,
                "originalContents", originalContents,
                "updatedContents", updatedContents
        };
    }
}
