package org.jenkinsci.plugins.queuepriority;

import hudson.Extension;
import hudson.model.Hudson;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.kohsuke.stapler.DataBoundConstructor;

public class ListCategory extends PriorityCategory {
    
    private final String listExp;
    private final Set<String> projectSet;
    
    @DataBoundConstructor
    public ListCategory(int priority, String listExp){
        super(priority);
        this.listExp = listExp;
        this.projectSet = getProjectSet(listExp);
    }
    
    public String getListExp(){
        return listExp;
    }
    
    public boolean isInCategory(String projectName){
        return projectSet.contains(projectName);
    }
    
    private Set<String> getProjectSet(String projectList){
        Set<String> s = new HashSet<String>();
        Collections.addAll(s, projectList.split(","));
        return s;
    }
    
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) Hudson.getInstance().getDescriptor(getClass());
    }
    
    @Extension
    public static class DescriptorImpl extends PriorityCategoryDescriptor {
        public String getDisplayName() { return ""; }
    }

}
