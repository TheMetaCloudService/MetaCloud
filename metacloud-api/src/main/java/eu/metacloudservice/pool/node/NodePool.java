package eu.metacloudservice.pool.node;/*
 * this class is by RauchigesEtwas
 */


import eu.metacloudservice.pool.node.entrys.CloudNode;

import java.util.ArrayList;
import java.util.List;

public class NodePool {

    private final ArrayList<CloudNode> connectedNodes;

    public NodePool(ArrayList<CloudNode> connectedNodes) {
        this.connectedNodes = connectedNodes;
    }


    public List<CloudNode> getNodes() {
        return connectedNodes;
    }

    public List<String> getNodesByName() {
        return connectedNodes.stream().map(CloudNode::getNodeName).toList();
    }

    public CloudNode getNode(String node){
        return connectedNodes.stream().filter(cloudNode -> cloudNode.getNodeName().equals(node)).findFirst().orElse(null);
    }

    public void createNode(CloudNode cloudNode){
        this.connectedNodes.add(cloudNode);
    }

    public void deleteNode(String node){
        connectedNodes.removeIf(cloudNode -> cloudNode.getNodeName().equals(node));
    }



}
