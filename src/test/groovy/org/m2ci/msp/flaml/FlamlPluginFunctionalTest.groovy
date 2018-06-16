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
    }

    @Test
    void canRun() {
        def result = gradle.build()
        println result.output
        assert result.task(':help').outcome == TaskOutcome.SUCCESS
    }
}
