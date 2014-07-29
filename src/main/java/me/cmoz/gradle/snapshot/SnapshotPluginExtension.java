/*
 * Copyright 2014 the original author or authors.
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
package me.cmoz.gradle.snapshot;

import lombok.Data;
import lombok.NonNull;

import java.text.SimpleDateFormat;

/**
 * An extension object to configure the {@code SnapshotTask}.
 */
@Data
public class SnapshotPluginExtension {

    /** The validator used to verify a user supplied date format. */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat();

    /** The default filename for the properties file. */
    public static final String DEFAULT_FILENAME = "snapshot.properties";
    /** The default format of the date fields in the properties file. */
    public static final String DEFAULT_DATEFORMAT = "dd.MM.yyyy '@' HH:mm:ss z";
    /** The default logging mode for plugin output. */
    public static final boolean DEFAULT_VERBOSE = Boolean.FALSE;

    /** The filename used when generating the build information file. */
    private String filename;
    /** The date format to be used for any dates exposed in properties. */
    private String dateFormat;
    /** Whether to run the plugin in verbose mode, defaults to {@code false}. */
    private boolean verbose;

    public SnapshotPluginExtension() {
        setFilename(DEFAULT_FILENAME);
        setDateFormat(DEFAULT_DATEFORMAT);
        setVerbose(DEFAULT_VERBOSE);
    }

    public void setDateFormat(@NonNull final String format) {
        SIMPLE_DATE_FORMAT.applyPattern(format);
        this.dateFormat = format;
    }

}
