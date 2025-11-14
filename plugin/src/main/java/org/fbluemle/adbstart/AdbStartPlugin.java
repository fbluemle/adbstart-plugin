package org.fbluemle.adbstart;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdbStartPlugin implements Plugin<@NotNull Project> {
    @Override
    public void apply(Project project) {
        AdbStartExtension ext = project.getExtensions().create("adbStart", AdbStartExtension.class);

        project.getPlugins().withId("com.android.application", p -> {
            try {
                Object android = project.getExtensions().getByName("android");
                Method getApplicationVariants = android.getClass().getMethod("getApplicationVariants");
                Object variants = getApplicationVariants.invoke(android);
                Method all = variants.getClass().getMethod("all", Action.class);

                all.invoke(variants, (Action<@NotNull Object>) variant -> {
                    try {
                        Method getName = variant.getClass().getMethod("getName");
                        String variantName = String.valueOf(getName.invoke(variant));
                        String cap = Character.toUpperCase(variantName.charAt(0)) + variantName.substring(1);
                        String installTaskName = "install" + cap;
                        String startTaskName = "start" + cap;

                        project.getTasks().register(startTaskName, task -> {
                            task.setGroup("run");
                            task.setDescription("Installs and starts the " + variantName + " app.");
                            task.dependsOn(installTaskName);

                            task.doLast(t -> {
                                try {
                                    Method getApplicationId = variant.getClass().getMethod("getApplicationId");
                                    String applicationId = String.valueOf(getApplicationId.invoke(variant));

                                    Method getNamespace = variant.getClass().getMethod("getNamespace");
                                    String namespace = String.valueOf(getNamespace.invoke(variant));

                                    String activity = ext.getActivity();
                                    String activityClassName = (activity != null && activity.startsWith("."))
                                            ? namespace + activity : activity;

                                    List<String> cmd = new ArrayList<>();
                                    String adb = (ext.getAdbPath() == null || ext.getAdbPath().isBlank()) ? "adb" : ext.getAdbPath();
                                    cmd.add(adb);
                                    if (ext.getDeviceSerial() != null && !ext.getDeviceSerial().isBlank()) {
                                        cmd.addAll(Arrays.asList("-s", ext.getDeviceSerial()));
                                    }
                                    cmd.addAll(Arrays.asList("shell", "am", "start"));
                                    if (ext.getExtraAmArgs() != null && !ext.getExtraAmArgs().isBlank()) {
                                        cmd.addAll(Arrays.asList(ext.getExtraAmArgs().trim().split("\\s+")));
                                    }
                                    cmd.addAll(Arrays.asList("-n", applicationId + "/" + activityClassName));

                                    project.getLogger().lifecycle("Starting " + applicationId + "/" + activityClassName + " ...");
                                    try {
                                        Process process = new ProcessBuilder(cmd).inheritIO().start();
                                        int exit = process.waitFor();
                                        if (exit != 0) {
                                            throw new RuntimeException("adb command failed with exit code " + exit + ": " + cmd);
                                        }
                                    } catch (Exception e) {
                                        throw new RuntimeException("Failed to execute adb command: " + cmd, e);
                                    }
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException("Failed to configure Android application variants", e);
            }
        });
    }
}
