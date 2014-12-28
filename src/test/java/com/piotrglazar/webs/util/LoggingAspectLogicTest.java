package com.piotrglazar.webs.util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoggingAspectLogicTest {

    // the same as in LoggingAspect
    private static final Logger LOG = (Logger) LoggerFactory.getLogger("Operation");

    private LoggingAspectLogic aspectLogic = new LoggingAspectLogic();

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private MethodSignature signature;

    @Mock
    private Appender<ILoggingEvent> appender;

    @Test
    public void shouldLogOperation() throws Throwable {
        // given
        LOG.addAppender(appender);
        given(proceedingJoinPoint.getSignature()).willReturn(signature);
        given(signature.getMethod()).willReturn(Operation.class.getDeclaredMethod("someOperation"));

        // when
        aspectLogic.logOperation(proceedingJoinPoint);

        // then
        final ArgumentCaptor<ILoggingEvent> loggingEvent = ArgumentCaptor.forClass(ILoggingEvent.class);
        verify(appender).doAppend(loggingEvent.capture());
        assertThat(loggingEvent.getValue().getFormattedMessage()).isEqualTo("someOperation");
    }

    private static final class Operation {

        @OperationLogging(operation = "someOperation")
        @SuppressWarnings("unused")
        public String someOperation() {
            return "doing some stuff";
        }
    }
}
