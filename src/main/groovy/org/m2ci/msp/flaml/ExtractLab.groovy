package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.m2ci.msp.jtgt.TextGrid
import org.m2ci.msp.jtgt.annotation.IntervalAnnotation
import org.m2ci.msp.jtgt.io.XWaveLabelSerializer
import org.m2ci.msp.jtgt.tier.IntervalTier
import org.yaml.snakeyaml.Yaml

class ExtractLab extends DefaultTask {

    @OutputDirectory
    def destDir = project.file("$project.buildDir/lab")

    @TaskAction
    def extract() {
        def yamlFile = project.yamlFile
        new Yaml().load(yamlFile.newReader()).each { utterance ->
            if (utterance.segments) {
                def time = 0.0f
                def intervals = utterance.segments.collect { segment ->
                    def start = time
                    time += segment.dur
                    def end = time
                    new IntervalAnnotation(start, end, segment.lab)
                }
                def tierName = 'phones'
                def tier = new IntervalTier(tierName, intervals.first().start, intervals.last().end, intervals)
                def textGrid = new TextGrid(null, intervals.first().start, intervals.last().end, [tier])
                project.file("$destDir/${utterance.prompt}.lab").text = new XWaveLabelSerializer().toString(textGrid, tierName)
            }
        }
    }
}
