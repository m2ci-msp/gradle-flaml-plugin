package org.m2ci.msp.flaml

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.WriteProperties

class WriteComments extends WriteProperties {

    @OutputFile
    final RegularFileProperty destFile = project.objects.fileProperty()

    WriteComments() {
        outputFile = destFile
    }
}
