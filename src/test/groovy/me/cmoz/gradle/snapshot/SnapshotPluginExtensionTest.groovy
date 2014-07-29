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

import static org.junit.Assert.*
import static me.cmoz.gradle.snapshot.SnapshotPluginExtension.*

class SnapshotPluginExtensionTest {

    private Project project

    private SnapshotPlugin plugin

    @Before
    void setUp() {
        project = new ProjectBuilder().build()
        plugin = new SnapshotPlugin()

        plugin.apply(project)
    }

    @Test
    void "Default configuration is applied"() {
        def ext = project.extensions.getByType(SnapshotPluginExtension.class)
        assertEquals(DEFAULT_FILENAME, ext.filename)
        assertEquals(DEFAULT_DATEFORMAT, ext.dateFormat)
        assertEquals(DEFAULT_VERBOSE, ext.verbose)
    }

    @Test
    @org.junit.Ignore
    void "Custom configuration is applied"() {
        def final customFilename = "myfile.properties"
        def final customDateformat = "dd.MM.yyyy"
        def final customVerbose = Boolean.TRUE

        // FIXME
        project.beforeEvaluate {
            snapshot {
                filename = customFilename
                dateFormat = customDateformat
                verbose = customVerbose
            }
        }

        def ext = project.extensions.getByType(SnapshotPluginExtension.class)
        assertEquals(customFilename, ext.filename)
        assertEquals(customDateformat, ext.dateFormat)
        assertEquals(customVerbose, ext.verbose)
    }

}
