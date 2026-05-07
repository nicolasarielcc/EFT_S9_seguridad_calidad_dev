package com.duoc.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;

import java.lang.reflect.Field;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordMigrationRunnerTest {

    @Mock
    private UserService userService;

    @Test
    void runShouldDoNothingWhenMigrationDisabled() throws Exception {
        PasswordMigrationRunner runner = new PasswordMigrationRunner();
        setField(runner, "migratePlainPasswords", false);
        setField(runner, "userService", userService);

        ApplicationArguments args = new DefaultApplicationArguments(new String[]{});
        runner.run(args);

        verify(userService, never()).migratePlainTextPasswords();
    }

    @Test
    void runShouldMigrateWhenEnabled() throws Exception {
        PasswordMigrationRunner runner = new PasswordMigrationRunner();
        setField(runner, "migratePlainPasswords", true);
        setField(runner, "userService", userService);
        when(userService.migratePlainTextPasswords()).thenReturn(3);

        ApplicationArguments args = new DefaultApplicationArguments(new String[]{});
        runner.run(args);

        verify(userService).migratePlainTextPasswords();
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
