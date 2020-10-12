package de.kaibra.swaggercodegenintegration.utils.container;

import org.testcontainers.containers.SelinuxContext;

public class MountInfo {
    private final SelinuxContext context;
    private final String mvnSettingsLocation;
    private final String mvnRepositoryLocation;

    public MountInfo(SelinuxContext context, String mvnSettingsLocation, String mvnRepositoryLocation) {
        this.context = context;
        this.mvnSettingsLocation = mvnSettingsLocation;
        this.mvnRepositoryLocation = mvnRepositoryLocation;
    }

    public SelinuxContext getContext() {
        return context;
    }

    public String getMvnSettingsLocation() {
        return mvnSettingsLocation;
    }

    public String getMvnRepositoryLocation() {
        return mvnRepositoryLocation;
    }

    @Override
    public String toString() {
        return "MountInfo{" +
                "context=" + context +
                ", mvnSettingsLocation='" + mvnSettingsLocation + '\'' +
                ", mvnRepositoryLocation='" + mvnRepositoryLocation + '\'' +
                '}';
    }
}