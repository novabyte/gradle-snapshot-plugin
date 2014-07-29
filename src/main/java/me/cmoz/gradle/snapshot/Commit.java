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

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Builder;

import java.util.HashMap;
import java.util.Map;

/**
 * A POJO that hold build metadata for the most recent commit in the SCM repository.
 */
@Data
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class Commit {

    /** Defines the standard commit abbrev length. */
    static final int ABBREV_LENGTH = 7;

    /** The property keys for the build information. */
    public static final String BUILD_TIME = "build.time";
    public static final String BUILD_USER_NAME = "build.user.name";
    public static final String BUILD_USER_EMAIL = "build.user.email";

    /** The property keys for the SCM commit information. */
    public static final String BRANCH = "branch";
    public static final String ID = "commit.id";
    public static final String ID_ABBREV = "commit.id.abbrev";
    public static final String TIME = "commit.time";
    public static final String USER_NAME = "commit.user.name";
    public static final String USER_EMAIL = "commit.user.email";
    public static final String MESSAGE_FULL = "commit.message";
    public static final String MESSAGE_SHORT = "commit.message.short";

    /** The value for the {@code build.time} key. */
    private final String buildTime;

    /** The value for the {@code build.user.name} key. */
    private final String buildAuthorName;

    /** The value for the {@code build.user.email} key. */
    private final String buildAuthorEmail;

    /** The value for the {@code branch} key. */
    private final String branchName;

    /** The value for {@code commit.id} and 'abbrev' keys. */
    private final String commitId;

    /** The value for the {@code commit.time} key. */
    private final String commitTime;

    /** The value for the {@code commit.user.name} key. */
    private final String commitUserName;

    /** The value for the {@code commit.user.email} key. */
    private final String commitUserEmail;

    /** The value for {@code commit.message} and 'short' keys. */
    private final String commitMessage;

    public Map<String, String> asMap() {
        final Map<String, String> map = new HashMap<String, String>();

        map.put(BUILD_TIME, getBuildTime());
        map.put(BUILD_USER_NAME, getBuildAuthorName());
        map.put(BUILD_USER_EMAIL, getBuildAuthorEmail());
        map.put(BRANCH, getBranchName());
        map.put(ID, getCommitId());
        map.put(ID_ABBREV, getCommitId().substring(0, ABBREV_LENGTH));
        map.put(TIME, getCommitTime());
        map.put(USER_NAME, getCommitUserName());
        map.put(USER_EMAIL, getCommitUserEmail());
        map.put(MESSAGE_FULL, getCommitMessage());

        final int endIndex = (getCommitMessage().indexOf('.') != -1)
                ? getCommitMessage().indexOf('.')
                : getCommitMessage().length();
        map.put(MESSAGE_SHORT, getCommitMessage().substring(0, endIndex));

        return map;
    }

}
