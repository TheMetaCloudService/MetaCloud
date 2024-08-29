/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.logging;


import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SimpleLatestLog {

    private final File latestLog;

    @SneakyThrows
    public SimpleLatestLog() {

        File logsDir = new File("./local/logs/");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        if (new File(logsDir, "latest.log").exists()) {
            getAllLogs().stream()
                    .filter(s -> !s.equalsIgnoreCase("latest.log"))
                    .map(s -> new File(logsDir, s))
                    .forEach(File::delete);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            File oldLog = new File(logsDir, "latest.log");
            File newLog = new File(logsDir, "Log_" + dtf.format(LocalDateTime.now()) + ".log");
            Files.copy(oldLog.toPath(), newLog.toPath(), StandardCopyOption.REPLACE_EXISTING);
            oldLog.delete();
        }

        this.latestLog = new File(logsDir, "latest.log");
        try {
            this.latestLog.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(String line) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(this.latestLog, true)))) {
            writer.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getAllLogs() {
        File file = new File("./local/logs/");
        File[] files = file.listFiles();
        List<String> logs = new ArrayList<>();
        for (File value : files != null ? files : new File[0]) {
            logs.add(value.getName());
        }
        return logs;
    }
}
