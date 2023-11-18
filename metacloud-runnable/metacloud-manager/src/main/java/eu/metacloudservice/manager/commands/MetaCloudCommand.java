package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;
import java.util.ArrayList;

@CommandInfo(command = "cloud", description = "command-me-description", aliases = {"me", "metacloud", "info"})
public class MetaCloudCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        double usedMemory = (double) memoryMXBean.getHeapMemoryUsage().getUsed() / 1048576;
        double maxMemory =    (double) memoryMXBean.getHeapMemoryUsage().getMax() / 1048576;
        int processors =    operatingSystemMXBean.getAvailableProcessors() ;
        int loadedClassCount = ManagementFactory.getClassLoadingMXBean().getLoadedClassCount();
        long totalLoadedClassCount = ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount();
        double prozent = (usedMemory*100)/maxMemory;
        DecimalFormat f = new DecimalFormat("#0.00");
        DecimalFormat f2 = new DecimalFormat("#0");
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Version:§f Metacloud-" +Driver.getInstance().getMessageStorage().version,
                "Authors: §fRauchigesEtwas  §r| §7Discord: §fhttps://discord.gg/4kKEcaP9WC",
                "Website: §fhttp://metacloudservice.eu/","",
                "OS Version: §f" + System.getProperty("os.name"),
                "User: §f" + System.getProperty("user.name"),
                "Java version: §f" + System.getProperty("java.version"),
                "Memory: §f" + f2.format(usedMemory) + "MB§7/§f" + f2.format(maxMemory)   + "MB §7(§f"+f.format(prozent)+ "%§7)",
                "Cores: §f" + processors,
                "Current Services: §f" + CloudManager.serviceDriver.getServices().size(),
                "Loaded Classes: §f" + loadedClassCount,
                "Totale Classes: §f" + totalLoadedClassCount);
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return new ArrayList<>();
    }
}
