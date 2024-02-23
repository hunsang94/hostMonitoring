package com.hostMonitoring.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.hostMonitoring.dto.Host;
import com.hostMonitoring.dto.HostPk;
import com.hostMonitoring.vo.HostRequest;
import com.hostMonitoring.vo.HostResponse;
import com.hostMonitoring.vo.HostStatus;
import com.hostMonitoring.vo.HostVO;
import com.hostMonitoring.repository.HostRepository;
import com.hostMonitoring.util.HostStatusMap;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class HostService {

    @Autowired
    private HostStatusMap hostStatusMap;

    @Autowired
    private HostRepository hostRepository;

    public List<HostVO> getHostAll() {
        List<HostVO> results = new ArrayList<HostVO>();
        List<Host> hostList = hostRepository.findAll();
        for (Host host : hostList) {
            results.add(new HostVO(host));
        }
        return results;
    }

    public HostVO getHost(String ip, String name) {
        HostPk hostPk = new HostPk(ip, name);
        Optional<Host> hostOptional = hostRepository.findById(hostPk);
        if (hostOptional.isPresent()) {
            return new HostVO(hostOptional.get());
        }
        throw new EntityNotFoundException("No host found on that IP.");
    }

    public HostResponse registHost(HostRequest hostReq) {
        String msg = "";
        HostPk hostPk = new HostPk();
        Host host = new Host();

        msg = emptyCheck(hostReq);
        if (msg.equals("") && hostRepository.count() >= 100) {
            msg = "Up to 100 hosts can be registered.";
        }

        if (!msg.equals("")) {
            return new HostResponse(msg);
        }

        BeanUtils.copyProperties(hostReq, hostPk);
        host.setHostPk(hostPk);
        try {
            hostRepository.save(host);
            hostStatusMap.putHostStatus(host.getHostPk());
        } catch (DataIntegrityViolationException e) {
            msg = "Duplicate IP and name.";
        }
        return new HostResponse(msg);
    }

    public HostResponse updateHost(HostRequest hostReq) {
        String msg = "";
        HostPk hostPk = new HostPk();
        Host host = new Host();
        Optional<Host> hostOptional;

        msg = emptyCheck(hostReq);
        if (!msg.equals("")) {
            return new HostResponse(msg);
        }

        BeanUtils.copyProperties(hostReq, hostPk);
        hostOptional = hostRepository.findById(hostPk);
        if (hostOptional.isPresent()) {
            host = hostOptional.get();
        } else {
            return new HostResponse("No host found on that IP and Name.");
        }

        host.setUpdateDateTime(LocalDateTime.now());
        hostRepository.save(host);
        hostStatusMap.replaceHostStatus(host.getHostPk());
        return new HostResponse(msg);
    }

    public void deleteHost(String ip, String name) {
        HostPk hostpk = new HostPk(ip, name);
        hostRepository.deleteById(hostpk);
        hostStatusMap.removeHostStatus(hostpk);
    }

    public String getCurrentHostStatus(String ip, String name) {
        String result = "";
        String lastAliveTime = "";
        try {
            HostStatus hostStatus = hostStatusMap.getHostStatusInfo(new HostPk(ip, name));
            InetAddress address = InetAddress.getByName(hostStatus.getIp());
            if (address.isReachable(1000)) {
                result = "Reachable";
                lastAliveTime = LocalDateTime.now().toString();
            } else {
                result = "Unreachable";
            }
        } catch (Exception e) {
            result = "Unreachable";
        }
        hostStatusMap.replaceHostStatus(new HostPk(ip, name), new HostStatus(ip, name, result, lastAliveTime));
        return result;
    }

    public List<HostStatus> getMonitoringAll() {
        return hostStatusMap.getHostStatusAll();
    }

    public List<HostStatus> getMonitoring(String ip, String name) {
        List<HostStatus> result = new ArrayList<HostStatus>();
        result.add(hostStatusMap.getHostStatus(new HostPk(ip, name)));
        return result;
    }

    private String emptyCheck(HostRequest hostReq) {
        if (hostReq.getIp() == null || hostReq.getIp().equals("")) {
            return "IP is empty.";
        } else if (hostReq.getName() == null || hostReq.getName().equals("")) {
            return "Name is empty.";
        } else {
            return "";
        }
    }
}
