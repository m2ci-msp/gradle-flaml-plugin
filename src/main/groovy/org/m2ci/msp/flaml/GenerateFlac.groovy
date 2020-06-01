package org.m2ci.msp.flaml

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class GenerateFlac extends DefaultTask {

    @InputFiles
    FileCollection srcFiles = project.files()

    @OutputFile
    final RegularFileProperty flacFile = project.objects.fileProperty()

    @TaskAction
    void generate() {
        project.exec {
            commandLine = ['sox'] + srcFiles + [flacFile.get().asFile]
        }
    }
}
