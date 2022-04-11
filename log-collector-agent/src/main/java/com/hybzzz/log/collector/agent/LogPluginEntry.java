package com.hybzzz.log.collector.agent;

import com.janetfilter.core.Environment;
import com.janetfilter.core.plugin.MyTransformer;
import com.janetfilter.core.plugin.PluginConfig;
import com.janetfilter.core.plugin.PluginEntry;

import java.util.ArrayList;
import java.util.List;

public class LogPluginEntry implements PluginEntry {
    private final List<MyTransformer> transformers = new ArrayList<>();



    @Override
    public void init(Environment environment, PluginConfig config) {
        String appName = environment.getAppName();
        System.out.println("=================log plugin===========================");
        JedisUtils.initJedis(config);
        AttachOnlyTransformer samplePlugin = new AttachOnlyTransformer(config.getBySection("LOG"));
        transformers.add(samplePlugin);

    }

    @Override
    public String getName() {
        return "LogPlugin";
    }

    @Override
    public String getAuthor() {
        return "hyb";
    }

    @Override
    public String getVersion() {
        return "v1.3.0";
    }

    @Override
    public String getDescription() {
        return "a log filter ja-netfilter plugin";
    }

    @Override
    public List<MyTransformer> getTransformers() {
        return transformers;
    }
}
