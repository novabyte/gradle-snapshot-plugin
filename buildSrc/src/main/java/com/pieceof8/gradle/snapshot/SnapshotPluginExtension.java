// vi: set softtabstop=4 shiftwidth=4 expandtab:
/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pieceof8.gradle.snapshot;

import org.gradle.api.Project;

import lombok.*;

/**
 * An extension object for the Snapshot plugin which allows it to be configured
 * through the {@code build.gradle} script.
 *
 * <p>Extension objects require Gradle 1.0-milestone-4 or greater.
 */
@ToString
class SnapshotPluginExtension {

    /**
     * The filename used when generating the build information file. This
     * defaults to {@code snapshot.properties}.
     */
    @Getter @Setter
    private @NonNull String filename;

    /**
     * The date format to be used for any dates exposed in properties. This
     * defaults to {@code "dd.MM.yyyy '@' HH:mm:ss z"}. This must be a valid
     * {@code SimpleDateFormat} value.
     *
     * @see java.text.SimpleDateFormat#parse(String)
     */
    @Getter @Setter
    private @NonNull String dateFormat;

    /**
     * Whether to run the plugin in verbose mode. This defaults to {@code false}.
     */
    @Getter @Setter
    private boolean verbose;

    /**
     * The extension object for the Snapshot plugin.
     *
     * @param project The project this extension is being applied to.
     */
    public SnapshotPluginExtension(final @NonNull Project project) {
        setFilename("snapshot.properties");
        setDateFormat("dd.MM.yyyy '@' HH:mm:ss z");
        setVerbose(false);
    }

}
