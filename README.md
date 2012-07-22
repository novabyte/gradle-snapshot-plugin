![Gradle Snapshot Plugin](https://github.com/PieceOf8/gradle-snapshot-plugin/raw/master/src/site/images/snapshot-logo.png)

# Gradle Snapshot Plugin

Snapshot is a [Gradle](http://gradle.org/) plugin that generates metadata at build-time from a Source Control Management (SCM) tool.

It takes the most recent commit from the SCM repository and generates a [`.properties`](http://en.wikipedia.org/wiki/.properties) file containing this information. This file is created in the `build/snapshot/` folder with a configurable filename.

Snapshot integrates with the [Java](http://www.gradle.org/docs/current/userguide/java_plugin.html) and [War](http://gradle.org/docs/current/userguide/war_plugin.html) plugins from Gradle's core plugins to make packaging the generated file output very easy (auto-managed).

This plugin recognises Git repositories (via the [JGit](http://eclipse.org/jgit/) library) and Mercurial repositories (via the [Hg4j]() library). Support for other SCM repositories will be added upon request (or even better send us your pull request! =P), only SCMs that have a Java library to communicate with it's repository layout can be included.

The Gradle Snapshot Plugin is considered feature complete. It depends on the plugin API from __Gradle 1.0__ or greater. You can report bugs and suggest new features on the [issues](https://github.com/PieceOf8/gradle-snapshot-plugin/issues) page.

<br/>
Gradle Snapshot Plugin is an open-source component from Piece of 8. <br/>It's created and maintained by Chris Molozian (@novabyte). <br/>Code licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0). Documentation licensed under [CC BY 3.0](http://creativecommons.org/licenses/by/3.0/).


## Usage

Snapshot follows a convention-over-configuration approach to simplify usage of the plugin. Nevertheless, this plugin __does not__ package the JGit or Mercurial Java libraries as part of it's dependencies. Depending on the SCM repository tool your project uses, you must include the corresponding library to the buildscript classpath. This is illustrated in the examples below.

### Minimal Example

To use the plugin add the following to your `build.gradle` script with Git SCM support:

```groovy
buildscript {
    repositories {
        mavenCentral()
        add(new org.apache.ivy.plugins.resolver.URLResolver()) {
            name = 'GitHub'
addArtifactPattern 'http://cloud.github.com/downloads/[organisation]/[module]/[module]-[revision].[ext]'
        }
    }
    dependencies {
        classpath(
            [group: 'org.eclipse.jgit', name: 'org.eclipse.jgit', version: '2.0.0.201206130900-r'],
            [group: 'PieceOf8', name: 'gradle-snapshot-plugin', version: '0.3.0']
        )
    }
}

apply plugin: 'snapshot'
```

### Full Example

A complete example including some configurable options for a Mercurial SCM repository looks like this:

```groovy
buildscript {
    repositories {
        maven {
            url 'http://maven.tmatesoft.com/content/repositories/releases/'
        }
        add(new org.apache.ivy.plugins.resolver.URLResolver()) {
            name = 'GitHub'
addArtifactPattern 'http://cloud.github.com/downloads/[organisation]/[module]/[module]-[revision].[ext]'
        }
    }
    dependencies {
        classpath(
            [group: 'org.tmatesoft.hg4j', name: 'hg4j', version: '1.0.0'],
            [group: 'PieceOf8', name: 'gradle-snapshot-plugin', version: '0.3.0']
        )
    }
}

apply plugin: 'snapshot'

snapshot {
    filename   = 'snapshot.properties'          // default
    dateFormat = "dd.MM.yyyy '@' HH:mm:ss z"    // default
}
```

### Build Environment Example

Snapshot makes all the properties recorded to the 'snapshot.properties' file also available at build time:

```groovy
buildscript {
    repositories {
        mavenCentral()
        add(new org.apache.ivy.plugins.resolver.URLResolver()) {
            name = 'GitHub'
addArtifactPattern 'http://cloud.github.com/downloads/[organisation]/[module]/[module]-[revision].[ext]'
        }
    }
    dependencies {
        classpath(
            [group: 'org.eclipse.jgit', name: 'org.eclipse.jgit', version: '2.0.0.201206130900-r'],
            [group: 'PieceOf8', name: 'gradle-snapshot-plugin', version: '0.3.0']
        )
    }
}

apply plugin: 'snapshot'
apply plugin: 'java'

jar {
    dependsOn snapshot

    appendix project.ext.properties['commit.id.abbrev']
}
```

### Real World Example

If you're not sure how to integrate this code into your build script have a look at the [`build.gradle`](https://github.com/PieceOf8/gradle-snapshot-plugin/blob/master/testProject/gitProject/build.gradle) for one of the test projects for the Snapshot plugin.


## Configuration

All of the configuration options for the plugins are:

```groovy
snapshot {
    filename   = "snapshot.properties"          // default
    dateFormat = "dd.MM.yyyy '@' HH:mm:ss z"    // default
    verbose    = true
}
```

A full description of each of the options is as follows:

<table width="100%">
<thead>
<tr>
  <th>Name</th>
  <th>Type</th>
  <th>Description</th>
</tr>
</thead>
<tbody>
<tr>
  <td valign="top"><b>filename</b></td>
  <td valign="top">(string)</td>
  <td valign="top">
  <p>The name of the file that's generated by the Snapshot plugin.</p>
  <p>Defaults to <code>snapshot.properties</code>.</p>
  </td>
</tr>
<tr>
  <td valign="top"><b>dateFormat</b></td>
  <td valign="top">(string)</td>
  <td valign="top">
  <p>The date format used when generating the values for <code>snapshot.commit.time</code> and <code>snapshot.build.time</code>. This must be a <code><a href="http://docs.oracle.com/javase/1.5.0/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a></code> compatible string.</p>
  <p>Defaults to <code>dd.MM.yyyy '@' HH:mm:ss z</code>.</p>
  </td>
</tr>
<tr>
  <td valign="top"><b>verbose</b></td>
  <td valign="top">(boolean)</td>
  <td valign="top">
  <p>Enable verbose mode in the plugin, the properties recorded by the plugin are printed to <code>stdout</code>.</p>
  <p>Defaults to <code>false</code>.</p>
  </td>
</tr>
</tbody>
</table>


## Generated `.properties` file

The file generated by this plugin contains a snapshot of the information from the most recent commit made to the codebase. It's generated in the `.properties` format to make it easy to parse within application code. For example, it can be used to generate the build information required to display a _commit id_ alongside a version number in the help dialog of an application.

The following properties are recorded:

```properties
commit.id.abbrev     = fd8c338
commit.user.email    = someuser@emailaddress.com
commit.message.full  = The full length of the commit message.
commit.id            = fd8c33821f02beb8ec47ae73256b64171ff5eaed
commit.message.short = A truncated form of the commit message.
commit.user.name     = Some User
build.user.name      = Another User
build.user.email     = anotheruser@emailaddress.com
branch               = branch-name
commit.time          = dd.MM.yyyy '@' HH:mm:ss z
build.time           = dd.MM.yyyy '@' HH:mm:ss z
```

## Contribute

All contributions to the documentation and the codebase are very welcome.

This plugin is written in Java and can be built using version `1.0` or greater of Gradle.

Send us your pull requests! `:)`
