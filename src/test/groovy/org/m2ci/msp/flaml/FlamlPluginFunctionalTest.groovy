package org.m2ci.msp.flaml

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.testng.annotations.BeforeGroups
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class FlamlPluginFunctionalTest {

    GradleRunner gradle

    void setupGradleAndProjectDir(String buildScriptResourceName, String... resourceNames) {
        def projectDir = File.createTempDir()
        new File(projectDir, 'settings.gradle').createNewFile()
        new File(projectDir, 'build.gradle').withWriter { writer ->
            writer << this.class.getResourceAsStream(buildScriptResourceName)
        }
        resourceNames.each { resourceName ->
            new File(projectDir, resourceName).withOutputStream { stream ->
                stream << this.class.getResourceAsStream(resourceName)
            }
        }
        gradle = GradleRunner.create().withPluginClasspath().withProjectDir(projectDir)
    }

    @BeforeGroups(groups = 'core')
    void setupCore() {
        setupGradleAndProjectDir('build-core.gradle',
                'foobar.flac', 'foobar.yaml')
    }

    @BeforeGroups(groups = 'extraction')
    void setupExtraction() {
        setupGradleAndProjectDir('build-extraction.gradle',
                'foobar.flac', 'foobar.yaml',
                'foobar.TextGrid', 'foo.wav', 'bar.wav', 'baz.wav', 'foo.lab', 'baz.lab', 'foo.txt', 'baz.txt')
    }

    @BeforeGroups(groups = 'generation')
    void setupGeneration() {
        setupGradleAndProjectDir('build-generation.gradle',
                'foobar.flac', 'foobar.yaml',
                'foo_padded.wav', 'baz_padded.wav', 'bar.wav')
    }

    @DataProvider
    Object[][] coreTasks() {
        [
                ['help'],
                ['hasPlugin'],
                ['tasks'],
                ['hasTaskDescriptions'],
                ['hasExtension'],
                ['hasFlamlResources']
        ]
    }

    @DataProvider
    Object[][] extractionTasks() {
        [
                ['testExtractWavFiles'],
                ['testExtractLabFiles'],
                ['testExtractTextGrid'],
                ['testExtractTextFiles']
        ]
    }

    @DataProvider
    Object[][] generationTasks() {
        [
                ['testGenerateFlac'],
                ['testGenerateYaml'],
                ['testGenerateYamlFromTextGrid'],
                ['testInjectText'],
                ['testInjectSegments']
        ]
    }

    void runGradleTask(String taskName) {
        def result = gradle.withArguments('--warning-mode', 'all', taskName).build()
        println result.output
        assert result.task(":$taskName").outcome in [TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE]
    }

    @Test(groups = 'core', dataProvider = 'coreTasks')
    void testCoreTasks(String taskName) {
        runGradleTask(taskName)
    }

    @Test(groups = 'extraction', dataProvider = 'extractionTasks')
    void testExtractionTasks(String taskName) {
        runGradleTask(taskName)
    }

    @Test(groups = 'generation', dataProvider = 'generationTasks')
    void testGenerationTasks(String taskName) {
        runGradleTask(taskName)
    }
}
