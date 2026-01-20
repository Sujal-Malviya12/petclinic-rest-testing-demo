package org.springframework.samples.petclinic.rest.advice;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;

class ExceptionControllerAdviceTest {

    @Test
    void handleGeneralException_shouldReturn500WithProblemDetail() {
        ExceptionControllerAdvice advice = new ExceptionControllerAdvice();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/test"));

        Exception ex = new RuntimeException("Something went wrong");

        ResponseEntity<ProblemDetail> response = advice.handleGeneralException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("RuntimeException", response.getBody().getTitle());
        assertEquals("Something went wrong", response.getBody().getDetail());
        assertNotNull(response.getBody().getProperties().get("timestamp"));
    }

    @Test
    void handleDataIntegrityViolationException_shouldReturn404WithProblemDetail() {
        ExceptionControllerAdvice advice = new ExceptionControllerAdvice();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/owners/99"));

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException("Owner does not exist");

        ResponseEntity<ProblemDetail> response =
                advice.handleDataIntegrityViolationException(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("DataIntegrityViolationException", response.getBody().getTitle());
        assertEquals("Owner does not exist", response.getBody().getDetail());
        assertNotNull(response.getBody().getProperties().get("timestamp"));
    }

    @Test
    void handleMethodArgumentNotValidException_shouldReturn400WithProblemDetail_whenBindingHasErrors() throws Exception {
        ExceptionControllerAdvice advice = new ExceptionControllerAdvice();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/owners"));

        // ✅ IMPORTANT: target must have property "firstName"
        FakeOwnerDto target = new FakeOwnerDto();

        BindingResult bindingResult = new BeanPropertyBindingResult(target, "ownerDto");
        bindingResult.rejectValue("firstName", "NotBlank", "must not be blank");

        Method method = DummyController.class.getDeclaredMethod("dummyMethod", String.class);
        var methodParameter = new org.springframework.core.MethodParameter(method, 0);

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ProblemDetail> response =
                advice.handleMethodArgumentNotValidException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("MethodArgumentNotValidException", response.getBody().getTitle());
        assertNotNull(response.getBody().getProperties().get("timestamp"));
    }

    @Test
    void handleMethodArgumentNotValidException_shouldReturn400WithoutBody_whenNoErrors() throws Exception {
        ExceptionControllerAdvice advice = new ExceptionControllerAdvice();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost/api/owners"));

        // BindingResult with NO errors
        FakeOwnerDto target = new FakeOwnerDto();
        BindingResult bindingResult = new BeanPropertyBindingResult(target, "ownerDto");

        Method method = DummyController.class.getDeclaredMethod("dummyMethod", String.class);
        var methodParameter = new org.springframework.core.MethodParameter(method, 0);

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<ProblemDetail> response =
                advice.handleMethodArgumentNotValidException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ✅ Needed only to create MethodParameter for MethodArgumentNotValidException
    static class DummyController {
        @SuppressWarnings("unused")
        void dummyMethod(String input) {
        }
    }

    // ✅ Needed so bindingResult.rejectValue("firstName", ...) works
    static class FakeOwnerDto {
        private String firstName;

        public String getFirstName() {
            return firstName;
        }

        @SuppressWarnings("unused")
        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }
}
