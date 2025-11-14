package org.fbluemle.adbstart;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class AdbstartPluginTest {
    @Test
    public void pluginAppliesAndCreatesExtension() {
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("org.fbluemle.adbstart");
        assertNotNull(project.getExtensions().findByName("adbStart"));
    }
}
