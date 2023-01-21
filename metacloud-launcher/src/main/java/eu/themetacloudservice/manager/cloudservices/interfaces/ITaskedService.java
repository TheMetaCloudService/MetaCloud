package eu.themetacloudservice.manager.cloudservices.interfaces;

import eu.themetacloudservice.manager.cloudservices.enums.TaskedServiceStatus;

public interface ITaskedService {

  void handelExecute(String line);
  void handelLaunch();
  void handelQuit();
  void handelPlayers();
  void handelStatusChange(TaskedServiceStatus status);
  void handelCloudPlayerConnection(boolean connect);

}
