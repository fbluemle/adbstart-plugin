package org.fbluemle.adbstart;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static org.junit.Assert.assertTrue;

public class AdbstartPluginFunctionalTest {
    @Rule
    public TemporaryFolder testProjectDir = new TemporaryFolder();

    private File getBuildFile() throws IOException {
        return testProjectDir.newFile("build.gradle");
    }

    private File getSettingsFile() throws IOException {
        return testProjectDir.newFile("settings.gradle");
    }

    @Test
    public void appliesPluginAndBuildsHelpTask() throws IOException {
        writeString(getSettingsFile(), "");
        writeString(getBuildFile(),
                "plugins {\n" +
                        "    id 'org.fbluemle.adbstart'\n" +
                        "}\n");

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("help")
                .withPluginClasspath()
                .forwardOutput()
                .build();

        assertTrue(result.getOutput().contains("BUILD SUCCESSFUL"));
    }

    @Test
    public void pluginConfiguresExtension() throws IOException {
        writeString(getSettingsFile(), "");
        writeString(getBuildFile(),
                "plugins {\n" +
                        "    id 'org.fbluemle.adbstart'\n" +
                        "}\n" +
                        "\n" +
                        "adbStart {\n" +
                        "    activity = '.CustomActivity'\n" +
                        "    deviceSerial = 'emulator-5554'\n" +
                        "    extraAmArgs = '-W'\n" +
                        "}\n" +
                        "\n" +
                        "task validateExtension {\n" +
                        "    doLast {\n" +
                        "        assert adbStart.activity == '.CustomActivity'\n" +
                        "        assert adbStart.deviceSerial == 'emulator-5554'\n" +
                        "        assert adbStart.extraAmArgs == '-W'\n" +
                        "        println 'Extension configured correctly'\n" +
                        "    }\n" +
                        "}\n");

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("validateExtension")
                .withPluginClasspath()
                .forwardOutput()
                .build();

        assertTrue(result.getOutput().contains("Extension configured correctly"));
        assertTrue(result.getOutput().contains("BUILD SUCCESSFUL"));
    }

    private void writeString(File file, String string) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            writer.write(string);
        }
    }
}
