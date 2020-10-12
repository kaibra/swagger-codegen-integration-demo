package de.kaibra.swaggercodegenintegration.test3;

import de.kaibra.swaggercodegenintegration.handlebars.ClassDescriptionHelper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class Test3 {

    private ClassDescriptionHelper myHandlebarsFn;

    @BeforeEach
    void setUp() {
        myHandlebarsFn = new ClassDescriptionHelper();
    }

    @Test
    void shouldRenderAShortName() throws IOException {
        Object result = myHandlebarsFn.apply("Pet", null);
        Assert.assertEquals(result, "\"Pet\" is a class with a short name");
    }

    @Test
    void shouldRenderALongName() throws IOException {
        Object result = myHandlebarsFn.apply("Order", null);
        Assert.assertEquals(result, "\"Order\" is a class with a long name");
    }

}
