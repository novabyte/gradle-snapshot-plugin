// vi: set softtabstop=4 shiftwidth=4 expandtab:
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
    private @NonNull String snapshotPropertiesFilename;

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
        setSnapshotPropertiesFilename("snapshot.properties");
        setDateFormat("dd.MM.yyyy '@' HH:mm:ss z");
        setVerbose(false);
    }

}
