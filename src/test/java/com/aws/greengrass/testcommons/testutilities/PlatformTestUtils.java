/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.aws.greengrass.testcommons.testutilities;

import com.aws.greengrass.config.PlatformResolver;
import com.aws.greengrass.logging.api.Logger;
import com.aws.greengrass.logging.impl.LogManager;
import com.aws.greengrass.testcommons.testutilities.unix.UnixPlatformTestUtils;
import com.aws.greengrass.testcommons.testutilities.windows.WindowsPlatformTestUtils;
import com.aws.greengrass.util.FileSystemPermission;

import java.nio.file.Path;

public abstract class PlatformTestUtils {

    private static PlatformTestUtils INSTANCE;
    private static final long TIMEOUT_MULTIPLIER = (long)(PlatformResolver.isWindows ? 1.5 : 1.0);

    private static final Logger logger = LogManager.getLogger(PlatformTestUtils.class);

    public static synchronized PlatformTestUtils getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }

        if (PlatformResolver.isWindows) {
            INSTANCE = new WindowsPlatformTestUtils();
        } else {
            INSTANCE = new UnixPlatformTestUtils();
        }

        logger.atTrace().log("Getting platform test utils instance {}", INSTANCE.getClass().getName());
        return INSTANCE;
    }

    /**
     * Returns a multiplier amount to increase the timeout time of certain tests due to performance differences
     * between Windows and Unix when running unit tests
     * @return the final long value set for the current platform
     */
    public static long getTimeoutMultiplier() {
        return TIMEOUT_MULTIPLIER;
    }

    public abstract boolean hasPermission(FileSystemPermission expected, Path path);
}
