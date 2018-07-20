package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.m2ci.msp.jtgt.io.TextGridSerializer
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

class GenerateYamlFromTextGrid extends DefaultTask {

    @InputFiles
    final RegularFileProperty srcFile = newInputFile()

    @OutputFile
    final RegularFileProperty yamlFile = newOutputFile()

    @TaskAction
    void generate() {
        def utterances = []
        def textGrid = new TextGridSerializer().fromString(srcFile.get().asFile.getText('UTF-8'))
        def promptsTier = textGrid.tiers[0]
        def segmentsTier = textGrid.tiers[1]
        promptsTier.annotations.findAll { it.text.trim() }.each { prompt ->
            def segments = []
            segmentsTier?.annotations.findAll { it.start >= prompt.start && it.end <= prompt.end }.each { segment ->
                if (segment.text) {
                    def dur = new BigDecimal(segment.end) - new BigDecimal(segment.start)
                    segments << [
                            lab: segment.text,
                            dur: dur.floatValue()
                    ]
                }
            }
            def utterance = [
                    prompt: prompt.text,
                    start : prompt.start,
                    end   : prompt.end
            ]
            if (segments) {
                utterance.segments = segments
            }
            utterances << utterance
        }
        def options = new DumperOptions()
        options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        def yaml = new Yaml(options)
        yaml.dump(utterances, yamlFile.get().asFile.newWriter('UTF-8'))
    }
}
