package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.Yaml

class ExtractText extends DefaultTask {

    @InputFile
    RegularFileProperty yamlFile = newInputFile()

    @OutputDirectory
    def destDir = project.file("$project.buildDir/text")

    @TaskAction
    def extract() {
        new Yaml().load(yamlFile.get().asFile.newReader()).each { utterance ->
            project.file("$destDir/${utterance.prompt}.txt").text = utterance.text
        }
    }
}
