// vi: set softtabstop=4 shiftwidth=4 expandtab:
/*
 * Copyright 2012 the original author or authors.
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
package com.pieceof8.gradle.snapshot;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.Copy;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskContainer;

import lombok.*;

/**
 * A Gradle plugin to generate build-time metadata from Source Control
 * Management (SCM) tools.
 */
public class SnapshotPlugin implements Plugin<Project> {

    /** The name of the snapshot task exposed to Gradle. */
    public static final String SNAPSHOT_TASK_NAME = "snapshot";

    /** {@inheritDoc} */
    @Override
    public void apply(final @NonNull Project project) {
        project.getExtensions().add("snapshot",
                new SnapshotPluginExtension(project));

        configureSnapshot(project);
    }

    /**
     * Configures the Snapshot task with the Gradle project and adds the task
     * to the project. If the {@code JavaPlugin} is available, the snapshot
     * task is configured to run just before it's <i>processResources</i> task.
     *
     * @param project The project POJO for this plugin.
     */
    private void configureSnapshot(final @NonNull Project project) {
        final TaskContainer tasks = project.getTasks();
        final PluginContainer plugins = project.getPlugins();

        val task = tasks.add(SNAPSHOT_TASK_NAME, SnapshotTask.class);
        task.setDescription("Generates build information from an SCM repository.");

        plugins.withType(JavaPlugin.class, new Action<JavaPlugin>() {
            @Override
            public void execute(final @NonNull JavaPlugin plugin) {
                SourceSetContainer sourceSets = (SourceSetContainer)
                        project.getProperties().get("sourceSets");
                sourceSets.getByName("main").getResources()
                        .srcDir(task.getSnapshotFile().getParentFile());

                Copy resourcesTask = (Copy) tasks
                        .getByName(JavaPlugin.PROCESS_RESOURCES_TASK_NAME);
                resourcesTask.dependsOn(task);
            }
        });
    }

}
