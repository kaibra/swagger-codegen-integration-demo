package de.kaibra.swaggercodegenintegration.test7;

import com.github.javaparser.ast.CompilationUnit;
import de.kaibra.swaggercodegenintegration.utils.java.AbstractJavaCodegenITest;
import io.swagger.codegen.v3.service.GenerationRequest;
import io.swagger.codegen.v3.service.GeneratorService;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static de.kaibra.swaggercodegenintegration.utils.TestingUtils.aTestGenerationRequest;
import static de.kaibra.swaggercodegenintegration.utils.TestingUtils.loadCompilationUnit;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class Test7 extends AbstractJavaCodegenITest {

    @Test
    void shouldLoadPetAndOrderCompilationUnits() throws IOException {
        GenerationRequest request = aTestGenerationRequest(CODEGEN_OUTPUT_DIR);
        List<String> paths = new GeneratorService().generationRequest(request).generate().stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());

        String petPath = CODEGEN_OUTPUT_DIR.getAbsolutePath() + "/src/main/java/de/kaibra/client/model/Pet.java";
        String orderPath = CODEGEN_OUTPUT_DIR.getAbsolutePath() + "/src/main/java/de/kaibra/client/model/Order.java";

        assertThat(paths).contains(
                petPath,
                orderPath
        );

        CompilationUnit petCompilationUnit = loadCompilationUnit(petPath);
        CompilationUnit orderCompilationUnit = loadCompilationUnit(orderPath);

        AssertionsForClassTypes.assertThat(petCompilationUnit.toString()).contains("\"Pet\" is a class with a short name");
        AssertionsForClassTypes.assertThat(orderCompilationUnit.toString()).contains("\"Order\" is a class with a long name");
    }

}
