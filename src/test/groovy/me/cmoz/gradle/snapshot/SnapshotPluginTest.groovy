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
package me.cmoz.gradle.snapshot

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.hamcrest.Matchers.*
import static org.junit.Assert.*

class SnapshotPluginTest {

    private Project project

    private SnapshotPlugin plugin

    @Before
    void setUp() {
        project = new ProjectBuilder().build()
        plugin = new SnapshotPlugin()

        plugin.apply(project)
    }

    @Test
    void "Extension is applied to project"() {
        def clazz = SnapshotPluginExtension.class;
        assertThat(project.extensions.getByType(clazz), instanceOf(clazz))
    }

    @Test
    void "Task is added to project"() {
        def task = project.tasks.getByName(SnapshotPlugin.SNAPSHOT_TASK_NAME)
        assertNotNull(task)
        assertThat(task, instanceOf(SnapshotTask.class))
    }

}
