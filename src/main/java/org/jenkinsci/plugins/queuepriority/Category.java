package org.jenkinsci.plugins.queuepriority;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;

import org.kohsuke.stapler.DataBoundConstructor;


/**
 * Just reference holder to PriorityCategory, I wasn't able to make req.bindJSONToList in PrioritySorter 
 * working without this class.
 *   
 * @author vjuranek
 *
 */
public class Category implements Describable<Category> {

    private final PriorityCategory category;
    
    @DataBoundConstructor
    public Category(PriorityCategory category){
        this.category = category;
    }
    
    public PriorityCategory getCategory(){
        return category;
    }
    
    public CategoryDescriptor getDescriptor() {
        return (CategoryDescriptor) Hudson.getInstance().getDescriptor(getClass());
    }
    
    @Extension
    public static class CategoryDescriptor extends Descriptor<Category> {
        public String getDisplayName() { return ""; }
    }
}
