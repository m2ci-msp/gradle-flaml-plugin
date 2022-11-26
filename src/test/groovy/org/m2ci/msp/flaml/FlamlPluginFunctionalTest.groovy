package org.m2ci.msp.flaml

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.testng.annotations.BeforeGroups
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

class FlamlPluginFunctionalTest {

    GradleRunner gradle

    @BeforeGroups(groups = 'core')
    void setupCore() {
        def projectDir = File.createTempDir()
        new File(projectDir, 'settings.gradle').createNewFile()
        new File(projectDir, 'build.gradle').withWriter {
            it << this.class.getResourceAsStream('build-core.gradle')
        }
        gradle = GradleRunner.create().withPluginClasspath().withProjectDir(projectDir)
        ['foobar.flac', 'foobar.yaml'].each { resourceName ->
            new File(projectDir, resourceName).withOutputStream { stream ->
                stream << this.getClass().getResourceAsStream(resourceName)
            }
        }
    }

    @BeforeGroups(groups = 'extraction')
    void setupExtraction() {
        def projectDir = File.createTempDir()
        new File(projectDir, 'settings.gradle').createNewFile()
        new File(projectDir, 'build.gradle').withWriter {
            it << this.class.getResourceAsStream('build-extraction.gradle')
        }
        gradle = GradleRunner.create().withPluginClasspath().withProjectDir(projectDir)
        ['foobar.flac', 'foobar.yaml', 'foobar.TextGrid',
         'foo.wav', 'bar.wav', 'baz.wav', 'foo.lab', 'baz.lab', 'foo.txt', 'baz.txt'].each { resourceName ->
            new File(projectDir, resourceName).withOutputStream { stream ->
                stream << this.getClass().getResourceAsStream(resourceName)
            }
        }
    }

    @BeforeGroups(groups = 'generation')
    void setupGeneration() {
        def projectDir = File.createTempDir()
        new File(projectDir, 'settings.gradle').createNewFile()
        new File(projectDir, 'build.gradle').withWriter {
            it << this.class.getResourceAsStream('build-generation.gradle')
        }
        gradle = GradleRunner.create().withPluginClasspath().withProjectDir(projectDir)
        ['foobar.flac', 'foobar.yaml', 'foo_padded.wav', 'baz_padded.wav',
         'bar.wav'].each { resourceName ->
            new File(projectDir, resourceName).withOutputStream { stream ->
                stream << this.getClass().getResourceAsStream(resourceName)
            }
        }
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

    @Test(groups = 'core', dataProvider = 'coreTasks')
    void testCoreTasks(String taskName) {
        def result = gradle.withArguments(
                '--warning-mode', 'all',
                taskName).build()
        println result.output
        assert result.task(":$taskName").outcome in [TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE]
    }

    @Test(groups = 'extraction', dataProvider = 'extractionTasks')
    void testExtractionTasks(String taskName) {
        def result = gradle.withArguments(
                '--warning-mode', 'all',
                taskName).build()
        println result.output
        assert result.task(":$taskName").outcome in [TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE]
    }

    @Test(groups = 'generation', dataProvider = 'generationTasks')
    void testGenerationTasks(String taskName) {
        def result = gradle.withArguments(
                '--warning-mode', 'all',
                taskName).build()
        println result.output
        assert result.task(":$taskName").outcome in [TaskOutcome.SUCCESS, TaskOutcome.UP_TO_DATE]
    }
}
