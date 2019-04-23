package com.petrovpavel.distributionmodule.core;


import com.petrovpavel.datamodule.entity.*;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface RouteBuilder {

    RouteData getRouteData();

    BigInteger getTaskCount(BigInteger taskSize);

    Route readTaskResultFiles(
            BigInteger taskCount,
            Path folderPath,
            Consumer<TaskResult> iterator
    ) throws IOException;

    Path writeJobAndTaskFiles(
            BigInteger taskSize, String jobName, Path jarFilePath,
            Path dataFilePath, Path tasksFolderPath,
            Path jobFolderPath, String remoteCommand
    ) throws IOException;

}
