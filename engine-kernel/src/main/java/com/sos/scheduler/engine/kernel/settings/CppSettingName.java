package com.sos.scheduler.engine.kernel.settings;

public enum CppSettingName {
    /** Wie -db= */
    dbName(1),

    /** Erweitert den Class-Path für einen Java-Job. */
    jobJavaClasspath(2),

    /** Default für factory.ini [spooler] html_dir */
    htmlDir(3),     // For C++ web server

    /** Grundeinstellung der Java-Optionen für alle Jobs. */
    jobJavaOptions(4),

    /** Datenbank über neue Java-Schnittstelle statt über die alte Hostware. */
    useJavaPersistence(5),

    orderDistributedBalanced(7),

    supervisorConfigurationPollingInterval(8),

    clusterRestartAfterEmergencyAbort(9),

    useOldMicroschedulingForJobs(10),

    useOldMicroschedulingForTasks(11),

    alwaysCreateDatabaseTables(12),

    roles(13),

    httpPort(14),

    agentConnectRetryDelay(15),

    /** For JettyPlugin: Base directory for files beneath URL "/jobscheduler/". */
    WebDirectoryUrl(16);  // For JettyPlugin

    /** Die Zahl muss mit der Zahl im C++-Code Settings.cxx übereinstimmen. */
    private final int number;

    CppSettingName(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
