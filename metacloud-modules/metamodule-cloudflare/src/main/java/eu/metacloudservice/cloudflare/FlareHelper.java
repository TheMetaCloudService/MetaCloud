/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.cloudflare;


import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.FlareGroup;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.roboflax.cloudflare.CloudflareAccess;
import eu.roboflax.cloudflare.CloudflareRequest;
import eu.roboflax.cloudflare.CloudflareResponse;
import eu.roboflax.cloudflare.constants.Category;
import eu.roboflax.cloudflare.objects.dns.DNSRecord;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FlareHelper {


    private final CloudflareAccess access;
    private final String zoneID;
    private final Map<String, String> srvRecords;
    private final Map<String, String> aRecords;

    public FlareHelper(@NotNull CloudflareAccess access, @NotNull String zoneID) {
        this.access = access;
        this.zoneID = zoneID;
        this.srvRecords =  new ConcurrentHashMap<>();
        this.aRecords =  new ConcurrentHashMap<>();
    }


    public void createARecord(String node, String address){
        if (!aRecords.containsKey(node)){
            Configuration configuration = (Configuration) new ConfigDriver("./modules/cloudflare/config.json").read(Configuration.class);
            CloudflareRequest request = new CloudflareRequest(Category.CREATE_DNS_RECORD, this.access)
                    .identifiers(zoneID)
                    .body(new Json()
                            .append("type", "A")
                            .append("ttl", 1)
                            .append("proxied", false)
                            .append("name", node + "." + configuration.getDomain())
                            .append("content", address)
                            .build());
            CloudflareResponse<DNSRecord> response = request.asObject(DNSRecord.class);
            if (response.getErrors().isEmpty()) {
                aRecords.put(node, response.getObject().getId());

            }
        }
    }

    public void createSRVRecord(String service, String group, int port, String node){
        if (!srvRecords.containsKey(service)){
            Configuration configuration = (Configuration) new ConfigDriver("./modules/cloudflare/config.json").read(Configuration.class);
            FlareGroup fg =  configuration.getGroups().stream().filter(flareGroup -> flareGroup.getGroup().equalsIgnoreCase(group)).findFirst().get();
            CloudflareRequest request = new CloudflareRequest(Category.CREATE_DNS_RECORD, this.access)
                    .identifiers(zoneID)
                    .body(new Json()
                            .append("type", "SRV")
                            .append("ttl", 1)
                            .append("proxied", false)
                            .append(
                                    "data", new Json()
                                            .append("service", "_minecraft")
                                            .append("proto", "_tcp")
                                            .append("name", fg.getSub())
                                            .append("priority", fg.getPriority())
                                            .append("weight", fg.getWeight())
                                            .append("port", port)
                                            .append("target", node + "." + configuration.getDomain())
                            ).build());
            CloudflareResponse<DNSRecord> response = request.asObject(DNSRecord.class);
            if (response.getErrors().isEmpty()) {
                srvRecords.put(service, response.getObject().getId());
            }
        }
    }

    public void deleteARecord(String node) {
        if (aRecords.containsKey(node)){

            CloudflareRequest request = new CloudflareRequest(Category.DELETE_DNS_RECORD, this.access)
                    .identifiers(this.zoneID, aRecords.get(node));
            request.sendAsync();
            aRecords.remove(node);
        }
    }

    public void deleteSRVRecord(String service) {
      if (srvRecords.containsKey(service)){

          CloudflareRequest request = new CloudflareRequest(Category.DELETE_DNS_RECORD, this.access)
                  .identifiers(this.zoneID, aRecords.get(service));
          request.sendAsync();
          srvRecords.remove(service);
      }
    }

    public void deleteARecords(){
        for (String service : aRecords.keySet()){
            CloudflareRequest request = new CloudflareRequest(Category.DELETE_DNS_RECORD, this.access)
                    .identifiers(this.zoneID, aRecords.get(service));
            request.sendAsync();
            aRecords.remove(service);
        }
    }

    public void deleteSRVRecords(){
        for (String service : srvRecords.keySet()){
            CloudflareRequest request = new CloudflareRequest(Category.DELETE_DNS_RECORD, this.access)
                    .identifiers(this.zoneID, srvRecords.get(service));
            request.sendAsync();
            srvRecords.remove(service);
        }
    }

}
