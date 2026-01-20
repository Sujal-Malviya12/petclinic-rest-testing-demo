package org.springframework.samples.petclinic.util;

import org.aspectj.lang.ProceedingJoinPoint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

class CallMonitoringAspectTest {

    @Test
    void isEnabled_shouldReturnTrueByDefault() {
        CallMonitoringAspect aspect = new CallMonitoringAspect();
        assertTrue(aspect.isEnabled());
    }

    @Test
    void setEnabled_shouldDisableAspect() {
        CallMonitoringAspect aspect = new CallMonitoringAspect();
        aspect.setEnabled(false);
        assertFalse(aspect.isEnabled());
    }

    @Test
    void reset_shouldSetCallCountAndCallTimeToZero() {
        CallMonitoringAspect aspect = new CallMonitoringAspect();

        // simulate some state
        aspect.setEnabled(true);
        aspect.reset();

        assertEquals(0, aspect.getCallCount());
        assertEquals(0, aspect.getCallTime());
    }

    @Test
    void getCallTime_shouldReturnZeroWhenNoCalls() {
        CallMonitoringAspect aspect = new CallMonitoringAspect();
        aspect.reset();

        assertEquals(0, aspect.getCallCount());
        assertEquals(0, aspect.getCallTime());
    }

    @Test
    void invoke_whenEnabled_shouldProceedAndIncreaseCallCount() throws Throwable {
        CallMonitoringAspect aspect = new CallMonitoringAspect();
        aspect.setEnabled(true);
        aspect.reset();

        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        when(joinPoint.toShortString()).thenReturn("DummyRepo.save(..)");
        when(joinPoint.proceed()).thenReturn("OK");

        Object result = aspect.invoke(joinPoint);

        assertEquals("OK", result);
        assertEquals(1, aspect.getCallCount());

        // getCallTime can be 0 sometimes (very fast execution), but should not be negative
        assertTrue(aspect.getCallTime() >= 0);
    }

    @Test
    void invoke_whenEnabledAndProceedThrows_shouldStillIncreaseCallCount() throws Throwable {
        CallMonitoringAspect aspect = new CallMonitoringAspect();
        aspect.setEnabled(true);
        aspect.reset();

        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        when(joinPoint.toShortString()).thenReturn("DummyRepo.find(..)");
        when(joinPoint.proceed()).thenThrow(new RuntimeException("boom"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> aspect.invoke(joinPoint));
        assertEquals("boom", ex.getMessage());

        // Even if proceed fails, finally block should count the call
        assertEquals(1, aspect.getCallCount());
        assertTrue(aspect.getCallTime() >= 0);
    }

    @Test
    void invoke_whenDisabled_shouldProceedButNotUpdateCounters() throws Throwable {
        CallMonitoringAspect aspect = new CallMonitoringAspect();
        aspect.setEnabled(false);
        aspect.reset();

        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenReturn(123);

        Object result = aspect.invoke(joinPoint);

        assertEquals(123, result);

        // disabled branch should not track calls
        assertEquals(0, aspect.getCallCount());
        assertEquals(0, aspect.getCallTime());
    }
}
