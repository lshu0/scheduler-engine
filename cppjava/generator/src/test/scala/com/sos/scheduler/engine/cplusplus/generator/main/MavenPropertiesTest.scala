package com.sos.scheduler.engine.cplusplus.generator.main

import org.hamcrest.MatcherAssert._
import org.hamcrest.Matchers._
import org.junit.Test

final class MavenPropertiesTest {
    val mavenProperties = new MavenProperties(getClass)

    @Test def testGroupId(): Unit = {
        assertThat(mavenProperties.groupId, equalTo("com.sos.scheduler.engine"))
    }

    @Test def testArtifactId(): Unit = {
        assertThat(mavenProperties.artifactId, equalTo("cppjava.generator"))
    }
    
    @Test def testVersion(): Unit = {
        assert(mavenProperties.version.length >= 1)
    }
}