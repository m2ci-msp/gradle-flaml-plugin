package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.m2ci.msp.jtgt.TextGrid
import org.m2ci.msp.jtgt.annotation.IntervalAnnotation
import org.m2ci.msp.jtgt.io.XWaveLabelSerializer
import org.m2ci.msp.jtgt.tier.IntervalTier
import org.yaml.snakeyaml.Yaml

class ExtractLab extends DefaultTask {

    @InputFile
    final RegularFileProperty yamlFile = project.objects.fileProperty()

    @OutputDirectory
    final DirectoryProperty destDir = project.objects.directoryProperty()

    @TaskAction
    def extract() {
        new Yaml().load(yamlFile.get().asFile.newReader()).each { utterance ->
            if (utterance.segments) {
                def time = 0.0
                def intervals = utterance.segments.collect { segment ->
                    def start = time
                    time += segment.dur as BigDecimal
                    def end = time
                    new IntervalAnnotation(start, end, segment.lab)
                }
                def tierName = 'phones'
                def tier = new IntervalTier(tierName, intervals.first().start, intervals.last().end, intervals)
                def textGrid = new TextGrid(null, intervals.first().start, intervals.last().end, [tier])
                destDir.file("${utterance.prompt}.lab").get().asFile.text = new XWaveLabelSerializer().toString(textGrid, tierName)
            }
        }
    }
}
