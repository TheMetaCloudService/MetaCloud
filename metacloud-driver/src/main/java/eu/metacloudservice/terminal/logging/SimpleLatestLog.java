/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.logging;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleLatestLog {

    private static final String LOG_DIRECTORY = "./local/logs/";
    private static final String LATEST_LOG_FILENAME = "latest.log";
    private final File latestLog;

    public SimpleLatestLog() throws IOException {
        File logsDir = new File(LOG_DIRECTORY);
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        cleanupOldLogs(logsDir);

        this.latestLog = new File(logsDir, LATEST_LOG_FILENAME);
        latestLog.createNewFile();  // Handle potential IOException here
    }

    public void log(String line) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(latestLog, true)))) {
            writer.println(line);
        }
    }

    private void cleanupOldLogs(File logsDir) throws IOException {
        if (!logsDir.isDirectory()) {
            throw new IOException("Logs directory is not a directory");
        }

        File[] files = logsDir.listFiles();
        if (files == null) {
            return;  // No files to process
        }

        for (File file : files) {
            if (file.getName().equalsIgnoreCase(LATEST_LOG_FILENAME)) {
                continue;  // Skip "latest.log" file
            }
            file.delete();
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        File oldLog = new File(logsDir, LATEST_LOG_FILENAME);
        File newLog = new File(logsDir, "Log_" + dtf.format(LocalDateTime.now()) + ".log");
        Files.copy(oldLog.toPath(), newLog.toPath(), StandardCopyOption.REPLACE_EXISTING);
        oldLog.delete();
    }
}
