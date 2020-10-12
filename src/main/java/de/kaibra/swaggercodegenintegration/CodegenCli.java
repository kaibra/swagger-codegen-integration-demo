package de.kaibra.swaggercodegenintegration;

import io.swagger.codegen.v3.service.GenerationRequest;
import io.swagger.codegen.v3.service.GeneratorService;
import io.swagger.codegen.v3.service.Options;

public class CodegenCli {

    public static void main(String[] args) {
        assert args.length == 2;
        assert args[0] != null;
        assert args[1] != null;

        GenerationRequest request = new GenerationRequest();
        request.codegenVersion(GenerationRequest.CodegenVersion.V3)
                .lang("my-custom-template")
                .specURL(args[0])
                .options(new Options().outputDir(args[1])
                        .invokerPackage("de.kaibra.client")
                        .library("resttemplate")
                        .apiPackage("de.kaibra.client.api")
                        .modelPackage("de.kaibra.client.model")
                        .addAdditionalProperty("dateLibrary", "java8")
                        .addAdditionalProperty("java8", true)
                );
        new GeneratorService().generationRequest(request).generate();
    }
}
