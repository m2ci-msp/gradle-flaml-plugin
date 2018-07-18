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
    final DirectoryProperty textDir = newInputDirectory()

    @InputFile
    final RegularFileProperty yamlSrcFile = newInputFile()

    @OutputFile
    final RegularFileProperty yamlDestFile = newOutputFile()

    @TaskAction
    void inject() {
        def options = new DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        def yaml = new Yaml(options)
        def prompts = yaml.load(yamlSrcFile.get().asFile.newReader('UTF-8'))
        prompts.each { prompt ->
            File textFile = textDir.file("${prompt.prompt}.txt").get().asFile
            if (textFile.canRead()) {
                prompt.text = textFile.getText('UTF-8')
            }
        }
        yaml.dump(prompts, yamlDestFile.get().asFile.newWriter('UTF-8'))
    }
}
