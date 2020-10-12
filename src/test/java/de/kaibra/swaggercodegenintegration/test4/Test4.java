package de.kaibra.swaggercodegenintegration.test4;

import io.swagger.codegen.v3.service.GenerationRequest;
import io.swagger.codegen.v3.service.GeneratorService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static de.kaibra.swaggercodegenintegration.utils.TestingUtils.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class Test4 {
    private File TEST_DIR;

    @BeforeEach
    void setUp() throws IOException {
        createGeneratedCodeDir();
        TEST_DIR = createNewCodegenOutputDir();
    }

    @Test
    void shouldWritePetAndOrderFiles() throws IOException {
        GenerationRequest request = aTestGenerationRequest(TEST_DIR);
        List<String> paths = new GeneratorService().generationRequest(request).generate().stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());

        assertThat(paths).contains(
                TEST_DIR.getAbsolutePath() + "/src/main/java/de/kaibra/client/model/Pet.java",
                TEST_DIR.getAbsolutePath() + "/src/main/java/de/kaibra/client/model/Order.java"
        );
    }

    @AfterEach
    public void cleanup() {
        deleteRecursivly(TEST_DIR);
    }
}
