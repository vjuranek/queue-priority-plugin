package org.jenkinsci.plugins.queuepriority;

import hudson.Extension;
import hudson.model.Hudson;

import java.util.regex.Pattern;

import org.kohsuke.stapler.DataBoundConstructor;

public class RegExpCategory extends PriorityCategory {
    
    private final String regExp;
    private final Pattern pattern;
    
    @DataBoundConstructor
    public RegExpCategory(int priority, String regExp){
        super(priority);
        this.regExp = regExp;
        pattern = Pattern.compile(regExp);
    }

    public String getRegExp(){
        return regExp;
    }
    
    public boolean isInCategory(String projectName){
        return pattern.matcher(projectName).matches();
    }
    
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) Hudson.getInstance().getDescriptor(getClass());
    }
    
    @Extension
    public static class DescriptorImpl extends PriorityCategoryDescriptor {
        public String getDisplayName() { return ""; }
    }
    
}
