package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

class InjectText extends DefaultTask {

    @InputDirectory
    final DirectoryProperty textDir = project.objects.directoryProperty()

    @InputFile
    final RegularFileProperty yamlSrcFile = project.objects.fileProperty()

    @OutputFile
    final RegularFileProperty yamlDestFile = project.objects.fileProperty()

    @TaskAction
    void inject() {
        def options = new DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        def yaml = new Yaml(options)
        def utterances = yaml.load(yamlSrcFile.get().asFile.newReader('UTF-8'))
        utterances.each { utterance ->
            File textFile = textDir.file("${utterance.prompt}.txt").get().asFile
            if (textFile.canRead()) {
                utterance.text = textFile.getText('UTF-8')
            }
        }
        yaml.dump(utterances, yamlDestFile.get().asFile.newWriter('UTF-8'))
    }
}
