package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.m2ci.msp.jtgt.Annotation
import org.m2ci.msp.jtgt.TextGrid
import org.m2ci.msp.jtgt.Tier
import org.m2ci.msp.jtgt.annotation.IntervalAnnotation
import org.m2ci.msp.jtgt.io.TextGridSerializer
import org.m2ci.msp.jtgt.tier.IntervalTier
import org.yaml.snakeyaml.Yaml

class ExtractTextGrid extends DefaultTask {

    @InputFile
    RegularFileProperty flacFile = newInputFile()

    @InputFile
    RegularFileProperty yamlFile = newInputFile()

    @OutputFile
    RegularFileProperty textGridFile = newOutputFile()

    @TaskAction
    void extract() {
        def time = 0.0
        def promptIntervals = []
        def yaml = new Yaml()
        yaml.load(yamlFile.get().asFile.newReader()).each { utterance ->
            if (time < utterance.start) {
                promptIntervals << new IntervalAnnotation(time, utterance.start, '')
            }
            time = utterance.start
            promptIntervals << new IntervalAnnotation(time, utterance.end, utterance.prompt)
            time = utterance.end
        }
        // determine end time from FLAC via soxi
        def soxi = new ByteArrayOutputStream()
        project.exec {
            commandLine 'soxi', flacFile.get().asFile
            standardOutput = soxi
        }
        def flacInfo = yaml.load(soxi.toString())
        def samples = flacInfo.Duration.split(' = ')[1] - 'samples'
        def sampleRate = flacInfo.'Sample Rate' as float
        def flacEnd = Float.parseFloat(samples) / sampleRate
        if (time < flacEnd) {
            promptIntervals << new IntervalAnnotation(time, flacEnd, '')
        }
        // create TextGrid
        def promptTier = new IntervalTier('prompts', promptIntervals.first().start, promptIntervals.last().end, promptIntervals)
        def textGrid = new TextGrid(null, promptIntervals.first().start, promptIntervals.last().end, [promptTier])
        new TextGridSerializer().save(textGrid, textGridFile.get().asFile)
    }
}
