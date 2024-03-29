package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

class GenerateYaml extends DefaultTask {

    @InputFiles
    FileCollection srcFiles = project.files()

    @InputFile
    final RegularFileProperty commentsFile = project.objects.fileProperty()

    @OutputFile
    final RegularFileProperty yamlFile = project.objects.fileProperty()

    @TaskAction
    void generate() {
        def utterances = []
        def start = 0.0
        srcFiles.each { srcFile ->
            // determine end time from WAV via soxi
            def soxi = new ByteArrayOutputStream()
            project.exec {
                commandLine 'soxi', '-r', srcFile
                standardOutput = soxi
            }
            def sampleRate = soxi.toString().readLines().first() as BigDecimal
            soxi = new ByteArrayOutputStream()
            project.exec {
                commandLine 'soxi', '-s', srcFile
                standardOutput = soxi
            }
            def samples = soxi.toString().readLines().first() as BigDecimal
            def end = start + samples / sampleRate

            // add prompt
            utterances << [
                    prompt: srcFile.name - '.wav',
                    start : start,
                    end   : end
            ]
            start = end
        }
        yamlFile.get().asFile.withWriter('UTF-8') { writer ->
            def comments = new Properties()
            comments.load(commentsFile.get().asFile.newInputStream())
            comments.toSorted { it.key }.each { key, value ->
                writer.writeLine("# $key: $value")
            }

            def options = new DumperOptions()
            options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
            def yaml = new Yaml(options)
            yaml.dump(utterances, writer)
        }
    }
}
