package de.kaibra.swaggercodegenintegration.test1;

import de.kaibra.swaggercodegenintegration.config.MyCodegenConfig;
import io.swagger.codegen.v3.CodegenConfig;
import io.swagger.codegen.v3.CodegenConfigLoader;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static de.kaibra.swaggercodegenintegration.config.MyCodegenConfig.MY_CUSTOM_TEMPLATE;

public class Test1 {

    private MyCodegenConfig custom;

    @BeforeEach
    public void setup() {
        custom = new MyCodegenConfig();
    }


    @Test
    void shouldFindCodegenConfigByTemplateName() {
        CodegenConfig config = CodegenConfigLoader.forName(MY_CUSTOM_TEMPLATE);
        Assert.assertNotNull(config);
    }

    @Test
    void shouldHaveTemplateDir() {
        Assertions.assertEquals(custom.getDefaultTemplateDir(), MY_CUSTOM_TEMPLATE);
    }

    @Test
    void shouldHaveTemplateName() {
        Assertions.assertEquals(custom.getName(), MY_CUSTOM_TEMPLATE);
    }

}
