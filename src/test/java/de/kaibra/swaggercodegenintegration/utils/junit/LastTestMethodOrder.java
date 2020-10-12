package de.kaibra.swaggercodegenintegration.utils.junit;

import org.junit.jupiter.api.MethodDescriptor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.MethodOrdererContext;

public class LastTestMethodOrder implements MethodOrderer {
    @Override
    public void orderMethods(MethodOrdererContext context) {
        context.getMethodDescriptors().sort(
                (MethodDescriptor m1, MethodDescriptor m2) -> {
                    if (m1.isAnnotated(LastTest.class)) {
                        return 1;
                    } else if (m2.isAnnotated(LastTest.class)) {
                        return -1;
                    }
                    return m1.getMethod().getName().compareToIgnoreCase(m2.getMethod().getName());
                });
    }
}
