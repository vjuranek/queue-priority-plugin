package org.jenkinsci.plugins.queuepriority;

import hudson.DescriptorExtensionList;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;

import java.util.List;

public abstract class PriorityCategory implements Describable<PriorityCategory> {
    
    public final static int DEFAULT_PRIORITY = 0;
    
    private final int priority;
    
    public PriorityCategory(int priority){
        this.priority = priority;
    }
    
    public int getPriority(){
        return priority;
    }
    
    public int getProjectPriority(String projectName){
        if(isInCategory(projectName))
            return priority;
        return DEFAULT_PRIORITY;
    }
    
    public abstract boolean isInCategory(String projectName);
    
    public static DescriptorExtensionList<PriorityCategory, PriorityCategory.PriorityCategoryDescriptor> all() {
        return Hudson.getInstance().getDescriptorList(PriorityCategory.class);
    }
    
    public static abstract class PriorityCategoryDescriptor extends Descriptor<PriorityCategory> {
    }
    
}
