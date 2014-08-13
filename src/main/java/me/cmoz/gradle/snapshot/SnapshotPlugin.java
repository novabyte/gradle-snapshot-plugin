/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.cmoz.gradle.snapshot;

import lombok.NonNull;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;

import java.io.File;

public final class SnapshotPlugin implements Plugin<Project> {

    /** The name of the snapshot task exposed to Gradle. */
    public static final String SNAPSHOT_TASK_NAME = "snapshot";

    @Override
    public void apply(final Project project) {
        final SnapshotPluginExtension ext = project
                .getExtensions()
                .create("snapshot", SnapshotPluginExtension.class);
        configureSnapshotTask(project, ext);
    }

    private void configureSnapshotTask(
            @NonNull final Project project,
            @NonNull final SnapshotPluginExtension ext) {
        final TaskContainer tasks = project.getTasks();

        final SnapshotTask task = tasks.create(SNAPSHOT_TASK_NAME, SnapshotTask.class);
        task.setDescription("Generate build information from an SCM repository.");

        project.getPlugins().withType(JavaPlugin.class, new Action<JavaPlugin>() {
            @Override
            public void execute(final JavaPlugin plugin) {
                final File outputDir = new File(project.getBuildDir(), "snapshot");
                final SourceSetContainer sourceSets =
                        (SourceSetContainer) project.getProperties().get("sourceSets");
                sourceSets.getByName("main").getResources().srcDir(outputDir);

                final Task t = tasks.getByName(JavaPlugin.PROCESS_RESOURCES_TASK_NAME);
                t.dependsOn(task);
            }
        });
    }

}
