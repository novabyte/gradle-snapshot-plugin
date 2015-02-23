package me.cmoz.gradle.snapshot

import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

class SnapshotTaskTest {

    @Test
    void "Should load stored properties"() {
        /* given: */
        def project = new ProjectBuilder().build()
        def plugin = new SnapshotPlugin()
        plugin.apply(project)

        def task = project.tasks[SnapshotPlugin.SNAPSHOT_TASK_NAME]
        task.outputFile.with {
            parentFile.mkdirs()
            withWriter { out ->
                out.writeLine "$Commit.ID_ABBREV=abcdf12"
            }
        }

        /* when: */
        task.execute()

        /* then: */
        assert project[Commit.ID_ABBREV] == 'abcdf12'
    }

}
