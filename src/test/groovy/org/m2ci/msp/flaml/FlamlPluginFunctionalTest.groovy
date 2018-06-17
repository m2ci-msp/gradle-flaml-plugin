package org.m2ci.msp.flaml

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.testng.annotations.BeforeClass
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class FlamlPluginFunctionalTest {

    GradleRunner gradle

    @BeforeClass
    void setup() {
        def projectDir = File.createTempDir()
        gradle = GradleRunner.create().withPluginClasspath().withProjectDir(projectDir)
        ['build.gradle', 'foobar.flac', 'foobar.yaml', 'foobar.wav', 'foobar.TextGrid', 'foo.wav', 'bar.wav', 'foo.lab', 'bar.lab'].each { resourceName ->
            new File(projectDir, resourceName).withOutputStream { stream ->
                stream << this.getClass().getResourceAsStream(resourceName)
            }
        }
    }

    @DataProvider
    Object[][] tasks() {
        [
                ['help'],
                ['hasPlugin'],
                ['hasTestResources'],
                ['testExtractWav']
        ]
    }

    @Test(dataProvider = 'tasks')
    void testTasks(String taskName) {
        def result = gradle.withArguments(taskName).build()
        println result.output
        assert result.task(":$taskName").outcome in [TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE]
    }
}
