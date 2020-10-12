package de.kaibra.swaggercodegenintegration.utils.java;

import de.kaibra.swaggercodegenintegration.utils.container.MavenContainer;
import de.kaibra.swaggercodegenintegration.utils.junit.LastTest;
import de.kaibra.swaggercodegenintegration.utils.junit.LastTestMethodOrder;
import org.junit.jupiter.api.*;

import java.io.File;

import static de.kaibra.swaggercodegenintegration.utils.TestingUtils.*;

@TestMethodOrder(LastTestMethodOrder.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract public class AbstractJavaCodegenITest {

    private final String MVN_COMMAND;

    public final File CODEGEN_OUTPUT_DIR;

    public AbstractJavaCodegenITest(String mvnCommand) {
        this(createNewCodegenOutputDir(), mvnCommand);
    }

    public AbstractJavaCodegenITest() {
        this(createNewCodegenOutputDir(), "compile");
    }

    private AbstractJavaCodegenITest(File outputDir, String mvnCommand) {
        this.CODEGEN_OUTPUT_DIR = outputDir;
        this.MVN_COMMAND = mvnCommand;
    }

    @Test
    @LastTest
    void shouldCompileAllGeneratedSources() throws Throwable {
        new MavenContainer(CODEGEN_OUTPUT_DIR.getAbsolutePath(), MVN_COMMAND)
                .assertMvnCommandIsSuccessWithoutErrors();
    }

    @BeforeAll
    public void setup() {
        createGeneratedCodeDir();
    }

    @AfterAll
    public void cleanup() {
        deleteRecursivly(CODEGEN_OUTPUT_DIR);
    }
}
