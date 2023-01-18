package eu.themetacloudservice.terminal.logging;

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

        if (!new File("./local/logs/").exists()){
            new File("./local/logs/").mkdirs();
        }
        if (new File("./local/logs/latest.log").exists()){

            getAllLogs().forEach(s -> {
                if (!s.equalsIgnoreCase("latest.log"))      {
                    new File("./local/logs/" + s).delete();
                }
            });

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try (InputStream is = new FileInputStream(new File("./local/logs/latest.log")); OutputStream os = new FileOutputStream(new File("./local/logs/Log_" + dtf.format(LocalDateTime.now()) + ".log"))) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            }
            new File("./local/logs/latest.log").delete();
        }
        this.latestLog = new File("./local/logs/latest.log");
        this.latestLog.createNewFile();

        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.latestLog), StandardCharsets.UTF_8), true);
        printWriter.flush();
        printWriter.close();

    }



    public void log( String line) {
        String lineToLog = line;
        lineToLog = lineToLog.replaceAll("\033\\[[;\\d]*m", "");
        this.logs.add(lineToLog);
        try {
            Writer output = new BufferedWriter(new FileWriter(this.latestLog, true));
            output.write(lineToLog);
            output.write("\n");
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveLogs(){
        Thread execute = new Thread(() -> {

            try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.latestLog), StandardCharsets.UTF_8), true)) {
                for (String loggedLine :this.logs) {
                    if (loggedLine == null)
                        continue;
                    w.println(loggedLine);
                }
                w.flush();
            } catch (Exception ignored) {
            }
        });
        execute.setPriority(Thread.MIN_PRIORITY);
        execute.start();
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
