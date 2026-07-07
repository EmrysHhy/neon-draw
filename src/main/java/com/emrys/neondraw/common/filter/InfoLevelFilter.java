package com.emrys.neondraw.common.filter;

/**
 *
 * @author Emrys
 * content:
 */


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class InfoLevelFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        int level = iLoggingEvent.getLevel().toInt();
        if (level == Level.INFO.toInt() || level == Level.WARN.toInt()){
            return FilterReply.ACCEPT;
        }
        return FilterReply.DENY;
    }
}