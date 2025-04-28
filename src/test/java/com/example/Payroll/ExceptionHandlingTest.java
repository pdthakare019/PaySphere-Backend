package com.example.Payroll;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.Payroll.Exceptions.EmployeeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

//@SpringBootTest
//@ExtendWith(MockitoExtension.class)

public class ExceptionHandlingTest {
    private ListAppender<ILoggingEvent> listAppender;
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testEmployeeNotFoundExceptionLogging() {
        // Arrange
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(employeeId));

        // Assert
        List<ILoggingEvent> logEvents = listAppender.list;
        assertTrue(logEvents.stream().anyMatch(event -> event.getLevel() == Level.ERROR
                && event.getMessage().contains("Employee not found with id: " + employeeId)));
    }
}


/*
    @BeforeEach
    public void setup() {
        Logger logger = (Logger) LoggerFactory.getLogger(EmployeeService.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }
 */
