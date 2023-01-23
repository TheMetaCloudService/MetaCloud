package eu.themetacloudservice.manager.commands;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.commands.CommandAdapter;
import eu.themetacloudservice.terminal.commands.CommandInfo;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.utils.TerminalStorageLine;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.text.DecimalFormat;
import java.util.ArrayList;

@CommandInfo(command = "cloud", DEdescription = "Hier finden Sie die vollständigen Informationen zum CloudSystem", ENdescription = "here you can find the full Information about the CloudSystem", aliases = {"me", "metacloud", "info"})
public class MetaCloudCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {


        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        double usedMemory = memoryMXBean.getHeapMemoryUsage().getUsed() / 1048576;
        double maxMemory =    memoryMXBean.getHeapMemoryUsage().getMax() / 1048576;
        int processors =    operatingSystemMXBean.getAvailableProcessors() ;
        int loadedClassCount = ManagementFactory.getClassLoadingMXBean().getLoadedClassCount();
        long totalLoadedClassCount = ManagementFactory.getClassLoadingMXBean().getTotalLoadedClassCount();
        double prozent = (usedMemory*100)/maxMemory;
        DecimalFormat f = new DecimalFormat("#0.00");
        DecimalFormat f2 = new DecimalFormat("#0");
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Version:§f Metacloud-" +Driver.getInstance().getMessageStorage().version,
                "authors: §fRauchigesEtwas  §r| §7Discord: §fhttps://discord.gg/4kKEcaP9WC",
                "website: §fhttp://metacloudservice.eu/","",
                "OS Version: §f" + System.getProperty("os.name"),
                "User: §f" + System.getProperty("user.name"),
                "java version: §f" + System.getProperty("java.version"),
                "memory: §f" + f2.format(usedMemory) + "MB§7/§f" + f2.format(maxMemory)   + "MB §7(§f"+f.format(prozent)+ "%§7)",
                "Cores: §f" + processors,
                "Loaded Classes: §f" + loadedClassCount,
                "Totale Classes: §f" + totalLoadedClassCount);


    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
