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

import lombok.val;
import org.gradle.api.Task;
import org.gradle.api.internal.TaskInternal;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Test;

import java.io.File;

// FIXME:
//  This code is far from ideal, the git test doesn't exercise the git project
//  that's bundled in the resources folder. Neither of these tests run the
//  "doFirst" or "doLast" actions in their respective "build.gradle" scripts.
public class SnapshotProjectTest {

    @Test
    public void test_git_project_with_plugin() {
        val projectDir = new File("src/test/resources/testProject/gitProject");
        val project = ProjectBuilder.builder()
                .withProjectDir(projectDir).build();
        SnapshotPlugin plugin = new SnapshotPlugin();
        plugin.apply(project);

        Task task = project.getTasks()
                .getByName(SnapshotPlugin.SNAPSHOT_TASK_NAME);

        ((TaskInternal) task).execute(); // gnarly I know :(
    }

    @Test
    public void test_hg_project_with_plugin() {
        val projectDir = new File("src/test/resources/testProject/hgProject");
        val project = ProjectBuilder.builder()
                .withProjectDir(projectDir).build();
        SnapshotPlugin plugin = new SnapshotPlugin();
        plugin.apply(project);

        Task task = project.getTasks()
                .getByName(SnapshotPlugin.SNAPSHOT_TASK_NAME);

        ((TaskInternal) task).execute(); // gnarly I know :(
    }

}
