package org.example.service;

import java.util.Map;

public interface HadoopService {
    public int dowork() throws Exception;
    public Map<String, Integer> show(int x) throws Exception;
    public Map<String, Integer> readFileToMap(String filePath);
    public void wordcount(String[] args) throws Exception;
}
