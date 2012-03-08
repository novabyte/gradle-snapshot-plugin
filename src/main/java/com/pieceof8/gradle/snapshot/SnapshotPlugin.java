// vi: set softtabstop=4 shiftwidth=4 expandtab:
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
 * A Gradle plugin to generate build information from a Git repository.
 */
public class SnapshotPlugin implements Plugin<Project> {

    /** The name of the gitcommit task exposed to Gradle. */
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

        val task = tasks.add(SNAPSHOT_TASK_NAME, GitcommitTask.class);
        task.setDescription("Generates build info from a SCM repository.");

        plugins.withType(JavaPlugin.class, new Action<JavaPlugin>() {
            @Override
            public void execute(final @NonNull JavaPlugin plugin) {
                SourceSetContainer sourceSets = (SourceSetContainer)
                        project.getProperties().get("sourceSets");
                sourceSets.getByName("main").getResources()
                        .srcDir(task.getOutput().getParentFile());

                Copy resourcesTask = (Copy) tasks
                        .getByName(JavaPlugin.PROCESS_RESOURCES_TASK_NAME);
                resourcesTask.dependsOn(task);
            }
        });
    }

}
