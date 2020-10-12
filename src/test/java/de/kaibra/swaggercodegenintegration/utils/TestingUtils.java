package de.kaibra.swaggercodegenintegration.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.swagger.codegen.v3.service.GenerationRequest;
import io.swagger.codegen.v3.service.Options;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static java.nio.charset.StandardCharsets.UTF_8;

final public class TestingUtils {

    private final static String TARGET_DIR = "target";
    private final static String GENERATED_CODE_DIR = TARGET_DIR + "/generated-code";

    private TestingUtils() {
    }

    public static GenerationRequest aTestGenerationRequest(File testDir) throws IOException {
        GenerationRequest request = new GenerationRequest();
        request.codegenVersion(GenerationRequest.CodegenVersion.V3)
                .lang("my-custom-template")
                .spec(loadYamlOrJson("specs/petstore_v3.json"))
                .options(new Options().outputDir(testDir.getAbsolutePath())
                        .invokerPackage("de.kaibra.client")
                        .library("resttemplate")
                        .apiPackage("de.kaibra.client.api")
                        .modelPackage("de.kaibra.client.model")
                        .addAdditionalProperty("dateLibrary", "java8")
                        .addAdditionalProperty("java8", true)
                );
        return request;
    }

    public static CompilationUnit loadCompilationUnit(String filename) throws FileNotFoundException {
        File javaFile = new File(filename);
        return StaticJavaParser.parse(javaFile);
    }

    public static String slurp(String filename) throws IOException {
        File f = new File(filename);
        byte[] bytes = Files.readAllBytes(f.toPath());
        return new String(bytes, UTF_8);
    }

    public static void deleteRecursivly(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                deleteRecursivly(file);
            }
        }
        dir.delete();
    }

    public static JsonNode loadYamlOrJson(final String file) throws IOException {
        try (InputStream in = TestingUtils.class.getClassLoader().getResourceAsStream(file)) {
            if (file.endsWith("yml")) {
                return Yaml.mapper().readTree(in);
            } else {
                return Json.mapper().readTree(in);
            }
        }
    }

    public static void createGeneratedCodeDir() {
        File targetDir = new File(TARGET_DIR);
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
        File generatedCodeDir = new File(GENERATED_CODE_DIR);
        if (!generatedCodeDir.exists()) {
            generatedCodeDir.mkdir();
        }
    }

    public static File createNewCodegenOutputDir() {
        try {
            createGeneratedCodeDir();
            File outputFolder = File.createTempFile("codegentest-", "-tmp", new File(GENERATED_CODE_DIR));
            outputFolder.delete();
            outputFolder.mkdir();
            return outputFolder;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
