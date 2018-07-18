package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.m2ci.msp.jtgt.TextGrid
import org.m2ci.msp.jtgt.io.XWaveLabelSerializer
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

class InjectSegments extends DefaultTask {

    @InputDirectory
    final DirectoryProperty labDir = newInputDirectory()

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
            File labFile = labDir.file("${prompt.prompt}.lab").get().asFile
            if (labFile.canRead()) {
                TextGrid tg = new XWaveLabelSerializer().fromString(labFile.getText('UTF-8'))
                prompt.segments = tg.tiers[0].annotations.collect {
                    [lab: it.text,
                     dur: (it.end - it.start).round(6)]
                }
            }
        }
        yaml.dump(prompts, yamlDestFile.get().asFile.newWriter('UTF-8'))
    }
}
