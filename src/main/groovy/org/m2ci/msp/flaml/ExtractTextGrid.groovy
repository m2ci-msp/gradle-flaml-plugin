package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.m2ci.msp.jtgt.TextGrid
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
        def segmentIntervals = []
        def yaml = new Yaml()
        yaml.load(yamlFile.get().asFile.newReader()).each { utterance ->
            def start = utterance.start as BigDecimal
            def end = utterance.end as BigDecimal
            if (time < start) {
                promptIntervals << new IntervalAnnotation(time, start, '')
                segmentIntervals << new IntervalAnnotation(time, start, '')
            }
            time = start
            promptIntervals << new IntervalAnnotation(time, end, utterance.prompt)
            if (utterance.segments) {
                utterance.segments.each { segment ->
                    def dur = segment.dur as BigDecimal
                    end = time + dur
                    segmentIntervals << new IntervalAnnotation(time, end, segment.lab)
                    time = end
                }
                if (time < end) {
                    segmentIntervals << new IntervalAnnotation(time, end, '')
                }
            }
            time = end
        }
        // determine end time from FLAC via soxi
        def soxi = new ByteArrayOutputStream()
        project.exec {
            commandLine 'soxi', flacFile.get().asFile
            standardOutput = soxi
        }
        def flacInfo = yaml.load(soxi.toString())
        def samples = flacInfo.Duration.split(' = ')[1] - 'samples' as BigDecimal
        def sampleRate = flacInfo.'Sample Rate' as BigInteger
        def flacEnd = samples / sampleRate
        if (time < flacEnd) {
            promptIntervals << new IntervalAnnotation(time, flacEnd, '')
            segmentIntervals << new IntervalAnnotation(time, flacEnd, '')
        }
        // create TextGrid
        def promptTier = new IntervalTier('prompts', promptIntervals.first().start, promptIntervals.last().end, promptIntervals)
        def segmentTier = new IntervalTier('segments', segmentIntervals.first().start, segmentIntervals.last().end, segmentIntervals)
        def textGrid = new TextGrid(null, promptIntervals.first().start, promptIntervals.last().end, [promptTier, segmentTier])
        new TextGridSerializer().save(textGrid, textGridFile.get().asFile)
    }
}
