package org.m2ci.msp.flaml

import de.undercouch.gradle.tasks.download.DownloadTaskPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin

class FlamlPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.pluginManager.apply(BasePlugin)
        project.pluginManager.apply(DownloadTaskPlugin)

        def flamlExtension = project.extensions.create('flaml', FlamlExtension, project)
        flamlExtension.flacFile = project.file("${project.name}.flac")
        flamlExtension.yamlFile = project.file("${project.name}.yaml")

        project.tasks.register 'extractWavFiles', ExtractWav, {
            group = 'FLAML'
            description = 'Extracts WAV files from FLAC+YAML'
            flacFile.set flamlExtension.flacFile
            yamlFile.set flamlExtension.yamlFile
            destDir.set project.layout.buildDirectory.dir('wav')
        }

        def extractLabFilesTask = project.tasks.register 'extractLabFiles', ExtractLab, {
            group = 'FLAML'
            description = 'Extracts XWaves lab files from YAML'
            yamlFile.set flamlExtension.yamlFile
            destDir.set project.layout.buildDirectory.dir('lab')
        }

        def extractTextGridTask = project.tasks.register 'extractTextGrid', ExtractTextGrid, {
            group = 'FLAML'
            description = 'Converts YAML to single Praat TextGrid file'
            flacFile.set flamlExtension.flacFile
            yamlFile.set flamlExtension.yamlFile
            textGridFile.set project.layout.buildDirectory.file("${project.name}.TextGrid")
        }

        def extractTextFilesTask = project.tasks.register 'extractTextFiles', ExtractText, {
            group = 'FLAML'
            description = 'Extracts text files from YAML'
            yamlFile.set flamlExtension.yamlFile
            destDir.set project.layout.buildDirectory.dir('text')
        }

        def generateCommentsTask = project.tasks.register('generateComments', WriteComments) {
            group = 'FLAML'
            description = 'Generates comments as properties file'
            destFile.set project.layout.buildDirectory.file('comments.properties')
        }

        project.tasks.register 'generateFlac', GenerateFlac, {
            group = 'FLAML'
            description = 'Generates FLAC from WAV file collection'
            srcFiles = project.layout.buildDirectory.dir('wav').get().asFileTree
            commentsFile.set generateCommentsTask.get().destFile
            flacFile.set project.layout.buildDirectory.file("${project.name}.flac")
        }

        project.tasks.register 'generateYaml', GenerateYaml, {
            group = 'FLAML'
            description = 'Generates YAML from WAV file collection'
            srcFiles = project.tasks.named('generateFlac').get().srcFiles
            commentsFile.set generateCommentsTask.get().destFile
            yamlFile.set project.layout.buildDirectory.file("${project.name}.yaml")
        }

        project.tasks.register 'generateYamlFromTextGrid', GenerateYamlFromTextGrid, {
            group = 'FLAML'
            description = 'Generates YAML from TextGrid'
            srcFile.set extractTextGridTask.get().textGridFile
            yamlFile.set project.layout.buildDirectory.file("${project.name}.yaml")
        }

        project.tasks.register 'injectText', InjectText, {
            group = 'FLAML'
            description = 'Injects text files into YAML'
            textDir.set extractTextFilesTask.get().destDir
            yamlSrcFile.set flamlExtension.yamlFile
            yamlDestFile.set project.layout.buildDirectory.file("${project.name}.yaml")
        }

        project.tasks.register 'injectSegments', InjectSegments, {
            group = 'FLAML'
            description = 'Injects lab files into YAML'
            labDir.set extractLabFilesTask.get().destDir
            yamlSrcFile.set flamlExtension.yamlFile
            yamlDestFile.set project.layout.buildDirectory.file("${project.name}.yaml")
        }
    }
}
