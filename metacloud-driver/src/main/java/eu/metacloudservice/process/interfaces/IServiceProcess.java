package eu.metacloudservice.process.interfaces;

public interface IServiceProcess {

    void sync();
    void handelConsole();
    void handelLaunch();
    void handleRestart();
    void handelShutdown();
}
