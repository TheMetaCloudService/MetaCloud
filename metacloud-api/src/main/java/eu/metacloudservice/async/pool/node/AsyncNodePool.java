package eu.metacloudservice.async.pool.node;/*
 * this class is by RauchigesEtwas
 */


import eu.metacloudservice.pool.node.entrys.CloudNode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AsyncNodePool {

    private final ArrayList<CloudNode> connectedNodes;

    public AsyncNodePool(ArrayList<CloudNode> connectedNodes) {
        this.connectedNodes = connectedNodes;
    }


    public CompletableFuture<List<CloudNode>> getNodes() {
        return CompletableFuture.supplyAsync(()->connectedNodes);
    }

    public CompletableFuture<List<String>> getNodesByName() {
        return CompletableFuture.supplyAsync( () ->connectedNodes.stream().map(CloudNode::getNodeName).toList());
    }

    public CompletableFuture<CloudNode> getNode(String node){
        return CompletableFuture.supplyAsync( () ->connectedNodes.stream().filter(cloudNode -> cloudNode.getNodeName().equals(node)).findFirst().orElse(null));
    }

    public void createNode(CloudNode cloudNode){
        this.connectedNodes.add(cloudNode);
    }

    public void deleteNode(String node){
        connectedNodes.removeIf(cloudNode -> cloudNode.getNodeName().equals(node));
    }



}
