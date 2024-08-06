package com.szelestamas.bookstorebddtest.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RunContext {
    @Getter
    private final String runId;
    @Getter
    @Setter
    private HttpStatusCode httpStatus;
    private final Map<Class<?>, Map<String, Object>> createdResources = new HashMap<>();
    @Getter
    private Map<Class<?>, String> lastCreatedResourceNames = new HashMap<>();

    public RunContext() {
        runId = UUID.randomUUID().toString();
    }

    public <T> T createdResource(Class<T> clazz, String name) {
        if (!createdResources.containsKey(clazz) || !createdResources.get(clazz).containsKey(name)) {
            throw new IllegalArgumentException("No stored resource present for class " + clazz + " with the name " + name);
        }
        return (T) createdResources.get(clazz).get(name);
    }

    public <T> void addCreatedResource(String name, T resource) {
        createdResources.computeIfAbsent(resource.getClass(), s -> new HashMap<>()).put(name, resource);
        lastCreatedResourceNames.put(resource.getClass(), name);
    }

    public <T> T lastCreatedResource(Class<T> clazz) {
        if (!lastCreatedResourceNames.containsKey(clazz)) {
            throw new IllegalArgumentException("No stored resource present for class " + clazz);
        }

        return createdResource(clazz, lastCreatedResourceNames.get(clazz));
    }

    public <T> boolean isResourceAlreadyCreated(Class<T> clazz) {
        return lastCreatedResourceNames.containsKey(clazz);
    }

    public String lastCreatedResourceName(Class clazz) {
        if (!lastCreatedResourceNames.containsKey(clazz)) {
            throw new IllegalArgumentException("No stored resource present for class " + clazz);
        }

        return lastCreatedResourceNames.get(clazz);
    }

    public void removeCreatedResource(String name, Class clazz) {
        if (createdResources.containsKey(clazz)) {
            createdResources.get(clazz).remove(name);
        }
    }

    public <T> Map<String, T> getResourceMap(Class<T> clazz) {
        if (!createdResources.containsKey(clazz)) {
            return new HashMap<>();
        } else {
            return createdResources.get(clazz).entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> (T)e.getValue()));
        }
    }

    public void resetCreatedResources() {
        createdResources.clear();
        lastCreatedResourceNames.clear();
    }
}
