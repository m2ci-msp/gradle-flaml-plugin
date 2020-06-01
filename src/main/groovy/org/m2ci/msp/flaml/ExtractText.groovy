package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.Yaml

class ExtractText extends DefaultTask {

    @InputFile
    final RegularFileProperty yamlFile = project.objects.fileProperty()

    @OutputDirectory
    final DirectoryProperty destDir = project.objects.directoryProperty()

    @TaskAction
    def extract() {
        new Yaml().load(yamlFile.get().asFile.newReader()).each { utterance ->
            if (utterance.text) {
                destDir.file("${utterance.prompt}.txt").get().asFile.text = utterance.text
            }
        }
    }
}
