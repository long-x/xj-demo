package com.ecdata.cmp.iaas.entity.dto.response.provider;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class VSphereResponseTest {

    public static void main(String[] args) {
        List<ClusterNetworkResponse> networkList = new ArrayList<>();
        ClusterNetworkResponse network1 = new ClusterNetworkResponse();
        network1.setName("name1");
        network1.setType("type1");
        network1.setNetworkKey("NetworkKey1");
        networkList.add(network1);

        ClusterNetworkResponse network2 = new ClusterNetworkResponse();
        network2.setName("name2");
        network2.setType("type2");
        network2.setNetworkKey("NetworkKey2");
        networkList.add(network2);

        List<DatastoreResponse> datastoreList = new ArrayList<>();

        datastoreList.add(DatastoreResponse.builder()
                .datastoreName("datastoreName1")
                .driveType("driveType1")
                .spaceTotal(100.00)
                .datastoreKey("datastoreKey1")
                .spaceUsed(80.00).build());

        datastoreList.add(DatastoreResponse.builder()
                .datastoreName("datastoreName2")
                .driveType("driveType2")
                .datastoreKey("datastoreKey2")
                .spaceTotal(110.00)
                .spaceUsed(60.00).build());


        List<HostResponse> hosts = new ArrayList<>();
        hosts.add(HostResponse.builder()
                .cpuTotal(100)
                .cpuUsed(80)
                .dataCenter("dataCenter1")
                .exsiBuildNumber("exsiBuildNumber1")
                .exsiVersion("exsiVersion1")
                .fromPort("fromPort1")
                .hostAgentApiVersion("hostAgentApiVersion1")
                .hostKey("hostKey1")
                .hostName("hostName1")
                .nics("nics1")
                .datastoreList(datastoreList)
                .build());

        ClusterResponse cluster = new ClusterResponse();
        cluster.setClusterName("clusterName");
        cluster.setClusterKey("clusterKey");
        cluster.setHostList(hosts);
        cluster.setClusterNetworkList(networkList);

        List<ClusterResponse> clusterList = new ArrayList<>();
        clusterList.add(cluster);

        List<NetworkResponse> networkResponses=new ArrayList<>();
        NetworkResponse networkResponse=new NetworkResponse();
        networkResponse.setHostId("hostId");
        networkResponse.setNetworkId("networkId");
        networkResponses.add(networkResponse);

        cluster.setNetworkList(networkResponses);

        AreaResponse area = new AreaResponse();

        area.setAreaName("AreaName1");
        area.setAreaKey("areaKey1");
        area.setClusterList(clusterList);

        List<AreaResponse> areaList = new ArrayList<>();
        areaList.add(area);

        VSphereResponse vSphereResponse = new VSphereResponse();
        vSphereResponse.setAreaData(areaList);

        String jsonString = JSON.toJSONString(vSphereResponse);

        System.err.println(jsonString);

//        String ss = "{\"areaList\":[{\"areaName\":\"AreaName1\",\"clusterList\":[{\"clusterName\":\"clusterName\",\"hostList\":[{\"cpuTotal\":\"cpuTotal1\",\"cpuUsed\":\"cpuUsed1\",\"dataCenter\":\"dataCenter1\",\"datastoreList\":[{\"datastoreName\":\"datastoreName1\",\"driveType\":\"driveType1\",\"spaceTotal\":\"spaceTotal1\",\"spaceUsed\":\"spaceUsed1\"},{\"datastoreName\":\"datastoreName2\",\"driveType\":\"driveType2\",\"spaceTotal\":\"spaceTotal2\",\"spaceUsed\":\"spaceUsed2\"}],\"exsiBuildNumber\":\"exsiBuildNumber1\",\"exsiVersion\":\"exsiVersion1\",\"fromPort\":\"fromPort1\",\"hostAgentApiVersion\":\"hostAgentApiVersion1\",\"hostKey\":\"hostKey1\",\"hostName\":\"hostName1\",\"networkList\":[{\"name\":\"name1\",\"type\":\"type1\"},{\"name\":\"name2\",\"type\":\"type2\"}],\"nics\":\"nics1\"}]}]}]}\n";

//        VSphereResponse vSphereResponse1 = JSON.parseObject(ss, VSphereResponse.class);

//        System.out.println(vSphereResponse1);
    }
}