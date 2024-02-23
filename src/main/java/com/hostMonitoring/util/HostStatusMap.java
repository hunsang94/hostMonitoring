package com.hostMonitoring.util;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hostMonitoring.dto.Host;
import com.hostMonitoring.dto.HostPk;
import com.hostMonitoring.repository.HostRepository;
import com.hostMonitoring.vo.HostStatus;

import jakarta.annotation.PostConstruct;

@Component
public class HostStatusMap {
    private HashMap<HostPk, HostStatus> hostStatusMap;

    @Autowired
    private HostRepository hostRepository;

    @PostConstruct
    public void init() {
        hostStatusMap = new HashMap<>();
        List<Host> hostList = hostRepository.findAll();
        for (Host host : hostList) {
            putHostStatus(host.getHostPk());
        }
    }

    public void putHostStatus(HostPk key) {
        hostStatusMap.put(key, new HostStatus(key.getIp(), key.getName()));
    }

    public void replaceHostStatus(HostPk key) {
        hostStatusMap.put(key, new HostStatus(key.getIp(), key.getName()));
    }

    public void replaceHostStatus(HostPk key, HostStatus value) {
        hostStatusMap.put(key, value);
    }

    public void removeHostStatus(HostPk key) {
        hostStatusMap.remove(key);
    }

    public HostStatus getHostStatusInfo(HostPk key) {
        return hostStatusMap.get(key);
    }

    public HostStatus getHostStatus(HostPk key) {
        HostStatus hostStatus = hostStatusMap.get(key);
        if (hostStatus == null) {
            return hostStatus;
        }

        setReachable(hostStatus);
        hostStatusMap.put(key, hostStatus);

        return hostStatus;
    }

    public List<HostStatus> getHostStatusAll() {
        List<HostStatus> hostStatusList = new ArrayList<HostStatus>();
        List<CompletableFuture<HostStatus>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(100);

        for (Entry<HostPk, HostStatus> s : hostStatusMap.entrySet()) {
            CompletableFuture<HostStatus> future = CompletableFuture.supplyAsync(() -> {
                HostStatus hostStatus = s.getValue();
                setReachable(hostStatus);
                return hostStatus;
            }, executor).orTimeout(1, TimeUnit.SECONDS);
            futures.add(future);
        }

        for (CompletableFuture<HostStatus> future : futures) {
            try {
                HostStatus hostStatus = future.get();
                hostStatusMap.replace(new HostPk(hostStatus.getIp(), hostStatus.getName()), hostStatus);
                hostStatusList.add(hostStatus);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("future fail. Error : " + e.getMessage());
            }
        }

        executor.shutdown();
        return hostStatusList;
    }

    public void setReachable(HostStatus hostStatus) {
        try {
            InetAddress address = InetAddress.getByName(hostStatus.getIp());
            if (address.isReachable(1000)) {
                hostStatus.setStatus("Reachable");
                hostStatus.setLastAliveTime(LocalDateTime.now().toString());
            } else {
                hostStatus.setStatus("Unreachable");
            }
        } catch (Exception e) {
            hostStatus.setStatus("Unreachable");
        }
    }
}
