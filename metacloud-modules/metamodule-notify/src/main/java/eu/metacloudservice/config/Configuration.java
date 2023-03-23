package eu.metacloudservice.config;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;

public class Configuration implements IConfigAdapter {

    private String servicePrepared;
    private String serviceConnected;
    private String serviceDiconnected;
    private String proxiedServicePrepared;
    private String proxiedServiceConnected;
    private String proxiedServiceDiconnected;

    public String getServicePrepared() {
        return servicePrepared;
    }

    public void setServicePrepared(String servicePrepared) {
        this.servicePrepared = servicePrepared;
    }

    public String getServiceConnected() {
        return serviceConnected;
    }

    public void setServiceConnected(String serviceConnected) {
        this.serviceConnected = serviceConnected;
    }

    public String getServiceDiconnected() {
        return serviceDiconnected;
    }

    public void setServiceDiconnected(String serviceDiconnected) {
        this.serviceDiconnected = serviceDiconnected;
    }

    public String getProxiedServicePrepared() {
        return proxiedServicePrepared;
    }

    public void setProxiedServicePrepared(String proxiedServicePrepared) {
        this.proxiedServicePrepared = proxiedServicePrepared;
    }

    public String getProxiedServiceConnected() {
        return proxiedServiceConnected;
    }

    public void setProxiedServiceConnected(String proxiedServiceConnected) {
        this.proxiedServiceConnected = proxiedServiceConnected;
    }

    public String getProxiedServiceDiconnected() {
        return proxiedServiceDiconnected;
    }

    public void setProxiedServiceDiconnected(String proxiedServiceDiconnected) {
        this.proxiedServiceDiconnected = proxiedServiceDiconnected;
    }
}
