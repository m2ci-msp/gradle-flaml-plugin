package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class GenerateFlac extends DefaultTask {

    @InputFiles
    FileCollection srcFiles = project.files()

    @Input
    final Property<Boolean> addReplayGain = project.objects.property(Boolean)
            .convention(true)

    @InputFile
    final RegularFileProperty commentsFile = project.objects.fileProperty()

    @OutputFile
    final RegularFileProperty flacFile = project.objects.fileProperty()

    @TaskAction
    void generate() {
        def comments = new Properties()
        comments.load(commentsFile.get().asFile.newInputStream())
        project.exec {
            def cmd = ['sox'] + srcFiles
            if (addReplayGain.get())
                cmd += ['--replay-gain', 'album']
            comments.toSorted { it.key }.each { key, value ->
                cmd += ["--add-comment", "$key=$value"]
            }
            cmd += [flacFile.get().asFile]
            commandLine = cmd
        }
    }
}
