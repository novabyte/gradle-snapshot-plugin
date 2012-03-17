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

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SnapshotPluginExtensionTest {

    /** Test instance of a Gradle {@code Project}. */
    private Project project;

    /** Test instance of the {@code SnapshotPlugin}. */
    private SnapshotPlugin plugin;

    @Before
    public void setup() {
        project = ProjectBuilder.builder().build();
        plugin = new SnapshotPlugin();

        plugin.apply(project);
    }

    @Test
    public void default_configuration_is_applied() {
        SnapshotPluginExtension ext = project.getExtensions()
                .getByType(SnapshotPluginExtension.class);
        assertSame(ext.getFilename(), "snapshot.properties");
        assertSame(ext.getDateFormat(), "dd.MM.yyyy '@' HH:mm:ss z");
        assertFalse(ext.isVerbose());
    }

/* TODO: overwrite default extension properties from build script
    @Test
    public void custom_configuration_is_applied() {
        SnapshotPluginExtension ext = new SnapshotPluginExtension(project);
        ext.setFilename("git.properties");
        ext.setDateFormat("hh 'o''clock' a, zzzz");
        ext.setVerbose(true);
        project.getExtensions().add("snapshot", ext);

        SnapshotPluginExtension ext1 = project.getExtensions()
                .getByType(SnapshotPluginExtension.class);
        assertSame(ext1.getFilename(), "git.properties");
    }
*/

}
