package org.m2ci.msp.flaml

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class FlamlPluginFunctionalTest {

    GradleRunner gradle

    @BeforeClass
    void setup() {
        def projectDir = File.createTempDir()
        gradle = GradleRunner.create().withPluginClasspath().withProjectDir(projectDir)
        ['build.gradle'].each { resourceName ->
            new File(projectDir, resourceName).withOutputStream { stream ->
                stream << this.getClass().getResourceAsStream(resourceName)
            }
        }
    }

    @Test
    void canRun() {
        def result = gradle.withArguments().build()
        println result.output
        assert result.task(':help').outcome == TaskOutcome.SUCCESS
    }

    @Test
    void canApplyPlugin() {
        def result = gradle.withArguments(':hasPlugin').build()
        println result.output
        assert result.task(':hasPlugin').outcome == TaskOutcome.SUCCESS
    }
}
