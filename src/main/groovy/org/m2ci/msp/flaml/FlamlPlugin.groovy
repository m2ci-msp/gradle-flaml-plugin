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

        project.task('extractWavFiles', type: ExtractWav, group: 'FLAML') {
            flacFile = flamlExtension.flacFile
            yamlFile = flamlExtension.yamlFile
        }

        project.task('extractLabFiles', type: ExtractLab, group: 'FLAML') {
            yamlFile = flamlExtension.yamlFile
        }

        project.task('extractTextFiles', type: ExtractText, group: 'FLAML') {
            yamlFile = flamlExtension.yamlFile
        }
    }
}
