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

    @Test
    public void extensionHasDefaultValues() {
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("org.fbluemle.adbstart");
        AdbStartExtension ext = project.getExtensions().getByType(AdbStartExtension.class);

        assertEquals(".MainActivity", ext.getActivity());
        assertEquals("", ext.getExtraAmArgs());
        assertEquals("adb", ext.getAdbPath());
        assertNull(ext.getDeviceSerial());
    }

    @Test
    public void extensionCanBeConfigured() {
        Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply("org.fbluemle.adbstart");
        AdbStartExtension ext = project.getExtensions().getByType(AdbStartExtension.class);

        ext.setActivity(".CustomActivity");
        ext.setDeviceSerial("emulator-5554");
        ext.setExtraAmArgs("-W");
        ext.setAdbPath("/custom/path/to/adb");

        assertEquals(".CustomActivity", ext.getActivity());
        assertEquals("emulator-5554", ext.getDeviceSerial());
        assertEquals("-W", ext.getExtraAmArgs());
        assertEquals("/custom/path/to/adb", ext.getAdbPath());
    }
}
