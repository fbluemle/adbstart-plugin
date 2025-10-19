package org.fbluemle;

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

public class AdbstartPluginPluginFunctionalTest {
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

        // If the plugin applies cleanly, the 'help' task should complete successfully
        assertTrue(result.getOutput().contains("BUILD SUCCESSFUL"));
    }

    private void writeString(File file, String string) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            writer.write(string);
        }
    }
}
