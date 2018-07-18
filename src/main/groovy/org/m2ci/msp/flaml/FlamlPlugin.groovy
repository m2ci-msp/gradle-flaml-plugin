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
            flacFile = flamlExtension.flacFile
            yamlFile = flamlExtension.yamlFile
            destDir = project.layout.buildDirectory.dir('wav')
        }

        project.tasks.register 'extractLabFiles', ExtractLab, {
            group = 'FLAML'
            description = 'Extracts XWaves lab files from YAML'
            yamlFile = flamlExtension.yamlFile
            destDir = project.layout.buildDirectory.dir('lab')
        }

        project.tasks.register 'extractTextGrid', ExtractTextGrid, {
            group = 'FLAML'
            description = 'Converts YAML to single Praat TextGrid file'
            flacFile = flamlExtension.flacFile
            yamlFile = flamlExtension.yamlFile
            textGridFile = project.layout.buildDirectory.file("${project.name}.TextGrid")
        }

        project.tasks.register 'extractTextFiles', ExtractText, {
            group = 'FLAML'
            description = 'Extracts text files from YAML'
            yamlFile = flamlExtension.yamlFile
            destDir = project.layout.buildDirectory.dir('text')
        }

        project.tasks.register 'injectSegments', InjectSegments, {
            group = 'FLAML'
            description = 'Injects lab files into YAML'
            labDir = project.layout.buildDirectory.dir('lab')
            yamlSrcFile = flamlExtension.yamlFile
            yamlDestFile = project.layout.buildDirectory.file("${project.name}.yaml")
        }
    }
}
