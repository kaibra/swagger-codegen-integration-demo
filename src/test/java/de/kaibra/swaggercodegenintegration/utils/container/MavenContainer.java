package de.kaibra.swaggercodegenintegration.utils.container;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.SelinuxContext;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.images.builder.dockerfile.DockerfileBuilder;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Stream;

public class MavenContainer extends GenericContainer<MavenContainer> {

    private final static Logger log = LoggerFactory.getLogger(MavenContainer.class);

    private static final String MAVEN_CONTAINER_IMAGE_NAME = "maven:3-openjdk-11";
    private static final String MAVEN_CONTAINER_SRC_DIR = "/usr/src/mymaven";
    private static final String MAVEN_CONTAINER_USER_HOME = "/var/maven";
    private static final String MAVEN_CONTAINER_MVN_CACHE = MAVEN_CONTAINER_USER_HOME + "/.m2";
    private static final String MAVEN_CONTAINER_REPOSITORY = MAVEN_CONTAINER_MVN_CACHE + "/repository";
    private static final String MAVEN_CONTAINER_SETTINGS = MAVEN_CONTAINER_MVN_CACHE + "/settings.xml";
    private static final String HOST_MAVEN_CACHE = System.getProperty("user.home") + "/.m2";

    public MavenContainer(String absHostSrcPath, String... mvnCommand) {
        super(withCustomImage());
        MountInfo mountInfo = getMountInfo();

        log.info("Copying {} to container", mountInfo.getMvnSettingsLocation());
        withCopyFileToContainer(MountableFile.forHostPath(mountInfo.getMvnSettingsLocation()),
                MAVEN_CONTAINER_SETTINGS);

        log.info("Mounting {} to container", mountInfo.getMvnRepositoryLocation());
        addFileSystemBind(mountInfo.getMvnRepositoryLocation(), MAVEN_CONTAINER_REPOSITORY,
                BindMode.READ_WRITE, mountInfo.getContext());

        log.info("Mounting {} to container", absHostSrcPath);
        addFileSystemBind(absHostSrcPath, MAVEN_CONTAINER_SRC_DIR,
                BindMode.READ_WRITE, mountInfo.getContext());

        withCommand(Stream.concat(
                Stream.of("mvn", "-Duser.home=" + MAVEN_CONTAINER_USER_HOME),
                Arrays.stream(mvnCommand)
        ).toArray(String[]::new));
    }


    private static ImageFromDockerfile withCustomImage() {
        return new ImageFromDockerfile()
                .withDockerfileFromBuilder(builder -> {
                    DockerfileBuilder b = builder.from(MAVEN_CONTAINER_IMAGE_NAME)
                            .env("MAVEN_CONFIG", MAVEN_CONTAINER_MVN_CACHE)
                            .workDir(MAVEN_CONTAINER_SRC_DIR);

                    IdInfo idInfo = getIdInfo();
                    log.info("Setting Image idInfo to {}", idInfo);
                    b = b
                            .run(String.format("groupadd -g %s %s", idInfo.getGroupId(), idInfo.getGroupName()))
                            .run(String.format("adduser --home /var/maven --gid %s --uid %s %s", idInfo.getGroupId(), idInfo.getUserId(), idInfo.getUserName()))
                            .user(idInfo.getUserId() + ":" + idInfo.getGroupId());
                    b.build();
                });
    }

    private static MountInfo getMountInfo() {
        return new MountInfo(
                SelinuxContext.NONE,
                HOST_MAVEN_CACHE + "/settings.xml",
                HOST_MAVEN_CACHE + "/repository"
        );
    }

    private void activateLogging() {
        Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
        this.followOutput(logConsumer);
    }

    private static IdInfo getIdInfo() {
        try {
            String userName = System.getProperty("user.name");
            return new IdInfo(
                    userId(userName),
                    userName,
                    groupId(userName),
                    userName
            );
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private static String userId(String userName) throws IOException {
        String command = "id -u " + userName;
        return executeCommand(command);
    }

    private static String groupId(String userName) throws IOException {
        String command = "id -g " + userName;
        return executeCommand(command);
    }

    private static String executeCommand(String command) throws IOException {
        Process child = Runtime.getRuntime().exec(command);
        try (InputStream i = child.getInputStream()) {
            return new String(i.readAllBytes()).trim();
        }
    }

    public void assertMvnCommandIsSuccessWithoutErrors() throws Throwable {
        try {
            this.start();
            this.activateLogging();
            log.info("Container started");

            long secondsTaken = waitForContainerToBeFinished(this, 3 * 60);
            log.info("Container stopped after {} seconds", secondsTaken);

            Assert.assertThat(this.getLogs(),
                    CoreMatchers.containsString("[INFO] BUILD SUCCESS"));

            Assert.assertThat(this.getLogs(),
                    CoreMatchers.not(CoreMatchers.containsString("[ERROR]")));
        } finally {
            this.stop();
        }
    }


    private static long waitForContainerToBeFinished(GenericContainer container, int timeLimitInSeconds)
            throws InterruptedException, WaitForContainerTimeoutReachedException {
        long start = System.currentTimeMillis();
        long secondsTaken = 0;
        while (container.isRunning()) {
            secondsTaken = (System.currentTimeMillis() - start) / 1000;
            if (secondsTaken > timeLimitInSeconds) {
                throw new WaitForContainerTimeoutReachedException(timeLimitInSeconds);
            }
            Thread.sleep(200);
        }
        return secondsTaken;
    }


    private static class WaitForContainerTimeoutReachedException extends Exception {
        public WaitForContainerTimeoutReachedException(int timeLimitInSeconds) {
            super(String.format("The testcontainer is still running after %d seconds. The test gets aborted.", timeLimitInSeconds));
        }
    }


}