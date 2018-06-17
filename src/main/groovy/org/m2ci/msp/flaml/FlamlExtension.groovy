package org.m2ci.msp.flaml

import org.gradle.api.Project

class FlamlExtension {

    Project project
    File flacFile
    File yamlFile

    FlamlExtension(Project project) {
        this.project = project
    }
}
