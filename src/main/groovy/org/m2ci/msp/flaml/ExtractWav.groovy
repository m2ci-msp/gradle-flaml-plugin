package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.Yaml

class ExtractWav extends DefaultTask {

    @InputFile
    final RegularFileProperty flacFile = project.objects.fileProperty()

    @InputFile
    final RegularFileProperty yamlFile = project.objects.fileProperty()

    @OutputDirectory
    final DirectoryProperty destDir = project.objects.directoryProperty()

    @TaskAction
    def extract() {
        new Yaml().load(yamlFile.get().asFile.newReader()).each { utterance ->
            project.exec {
                commandLine 'sox',
                        flacFile.get().asFile,
                        destDir.file("${utterance.prompt}.wav").get().asFile,
                        'trim',
                        utterance.start,
                        '=' + utterance.end
            }
        }
    }
}
