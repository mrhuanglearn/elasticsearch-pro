package com.hsit.elasticsearch.request;

import java.util.List;

public class InputCondition {
    private String generation;
    private List<ELKRequest> inputItemCondition;

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public List<ELKRequest> getInputItemCondition() {
        return inputItemCondition;
    }

    public void setInputItemCondition(List<ELKRequest> inputItemCondition) {
        this.inputItemCondition = inputItemCondition;
    }
}
