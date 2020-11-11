package com.example.demo.logging;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@RestController
@Getter
public class LoggingController {
 
    private boolean errorEnabled = log.isErrorEnabled();
    private boolean warnEnabled = log.isWarnEnabled();
    private boolean infoEnabled = log.isInfoEnabled();
    private boolean debugEnabled = log.isDebugEnabled();
    private boolean traceEnabled = log.isTraceEnabled();

	@GetMapping(path = "/log")
    public String log() {
    	this.errorEnabled = log.isErrorEnabled();
    	this.warnEnabled = log.isWarnEnabled();
    	this.infoEnabled = log.isInfoEnabled();
    	this.debugEnabled = log.isDebugEnabled();
    	this.traceEnabled = log.isTraceEnabled();
    	log.error("This is an ERROR level message");
    	log.warn("This is a WARN level message");
    	log.info("This is an INFO level message");
    	log.debug("This is a DEBUG level message");
        log.trace("This is a TRACE level message");
        return "See the log for details";
    }
}
