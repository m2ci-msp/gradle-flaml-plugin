package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.Yaml

class ExtractWav extends DefaultTask {

    @InputFile
    RegularFileProperty flacFile = newInputFile()

    @InputFile
    RegularFileProperty yamlFile = newInputFile()

    @OutputDirectory
    def destDir = project.file("$project.buildDir/wav")

    @TaskAction
    def extract() {
        new Yaml().load(yamlFile.get().asFile.newReader()).each { utterance ->
            project.exec {
                commandLine 'sox',
                        flacFile.get().asFile,
                        project.file("$destDir/${utterance.prompt}.wav"),
                        'trim',
                        utterance.start,
                        '=' + utterance.end
            }
        }
    }
}
