package de.kaibra.swaggercodegenintegration.test2;

import com.github.jknack.handlebars.Handlebars;
import de.kaibra.swaggercodegenintegration.config.MyCodegenConfig;
import de.kaibra.swaggercodegenintegration.handlebars.ClassDescriptionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Test2 {
    private MyCodegenConfig custom;

    @BeforeEach
    public void setup() {
        custom = new MyCodegenConfig();
    }

    @Test
    void shouldRegisterAHandleBarsHelper() {
        Handlebars handleBarsMock = mock(Handlebars.class);

        // WHEN
        custom.addHandlebarHelpers(handleBarsMock);

        // THEN
        verify(handleBarsMock).registerHelper(
                eq("classDescription"),
                isA(ClassDescriptionHelper.class)
        );
    }
}
