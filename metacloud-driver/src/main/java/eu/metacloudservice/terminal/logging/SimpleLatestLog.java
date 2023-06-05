package eu.metacloudservice.terminal.logging;

import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SimpleLatestLog {

    private final File latestLog;
    private final LinkedList<String> logs;

    @SneakyThrows
    public SimpleLatestLog(){
        this.logs = new LinkedList<>();

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
            try (InputStream is = new BufferedInputStream(new FileInputStream(oldLog));
                 OutputStream os = new BufferedOutputStream(new FileOutputStream(newLog))) {
                byte[] buffer = new byte[8192];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } catch (IOException e) {
                // handle the exception or rethrow it
                e.printStackTrace();
            }
            oldLog.delete();
        }

        this.latestLog = new File(logsDir, "latest.log");
        try {
            this.latestLog.createNewFile();
        } catch (IOException e) {
            // handle the exception or rethrow it
            e.printStackTrace();
        }


    }



    public void log( String line) {
        try (Writer output = new BufferedWriter(new FileWriter(this.latestLog, true))) {
            output.append(line).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void saveLogs(){
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.latestLog, true), StandardCharsets.UTF_8)), true)) {
            for (String loggedLine : this.logs) {
                if (loggedLine != null) {
                    writer.println(loggedLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private ArrayList<String> getAllLogs() {
        File file = new File("./local/logs/");
        File[] files = file.listFiles();
        ArrayList<String> logs = new ArrayList<>();
        for (File value : files != null ? files : new File[0]) {
            String FirstFilter = value.getName();
            logs.add(FirstFilter);
        }
        return logs;
    }

}
