package org.jenkinsci.plugins.queuepriority;

import hudson.Extension;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import hudson.model.Queue.BuildableItem;
import hudson.model.queue.QueueSorter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;

@Extension
public class PrioritySorter extends QueueSorter implements Describable<PrioritySorter> {
    
    public void sortBuildableItems(List<BuildableItem> buildables) {
        System.out.println("SORTING THE QUEUE ...");
        Collections.sort(buildables,getComparator()); 
    }

    public Comparator<BuildableItem> getComparator(){
        if(getDescriptor().getGroovyComparator() != null)
            return getDescriptor().getGroovyComparator();
        System.out.println("Using default comparator");
        return getDescriptor().getDefaultComparator();
    }
    
    public static class DefaultComparator implements Comparator<BuildableItem> {
        
        private final List<Category> priorityCategories;
        
        public DefaultComparator(List<Category> priorityCategories){
            this.priorityCategories = priorityCategories;
        }
        
        public int compare(BuildableItem bi1, BuildableItem bi2){
            System.out.println("Comparing " + bi1.task.getDisplayName() + " and " + bi2.task.getDisplayName());
            int p1 = getPriority(bi1.task.getDisplayName());
            int p2 = getPriority(bi2.task.getDisplayName());
            System.out.println("Priorities: " + p1 + ", " + p2);
            //reverse order - first items get executed first
            if (p1 < p2)    return 1;
            if (p1 > p2)    return -1;
            return 0;
        }
        
        private int getPriority(String projectName){
            int priority = PriorityCategory.DEFAULT_PRIORITY;
            for(Category c : priorityCategories){
                if(c.getCategory().isInCategory(projectName)){
                    priority = c.getCategory().getPriority();
                    break;
                }
            }
            return priority;
        }
    }

    public static class GroovyComparator implements Comparator<BuildableItem> {
        
        private final String sorterScript;
        
        public GroovyComparator(String sorterScript){
           this.sorterScript = sorterScript;
        }
        
        public String getSorterScript(){
            return sorterScript;
        }
        
        public int compare(BuildableItem bi1, BuildableItem bi2){
            return 0;
        }
    }
    
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) Hudson.getInstance().getDescriptor(getClass());
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<PrioritySorter> {

        private List<Category> priorityCategories;
        private String sorterScript; 
        private transient DefaultComparator defaultComparator;
        private transient GroovyComparator groovyComparator;
        
        
        public DescriptorImpl() {
            load();            
        }
        
        private Object readResolve(){
            defaultComparator = new DefaultComparator(priorityCategories);
            groovyComparator = (sorterScript != null && !(sorterScript.trim().equals(""))) ?  new GroovyComparator(sorterScript) : null;
            return this;
        }
        
        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            priorityCategories = req.bindJSONToList(Category.class, formData.get("priorityCategories"));
            sorterScript = formData.getString("sorterScript");
            save();
            defaultComparator = new DefaultComparator(priorityCategories);
            groovyComparator = (sorterScript != null && !(sorterScript.trim().equals(""))) ?  new GroovyComparator(sorterScript) : null;
            return super.configure(req, formData);
        }

        public List<Category> getPriorityCategories() {
            return priorityCategories;
        }
        
        public String getSorterScript(){
            return sorterScript;
        }
        
        public GroovyComparator getGroovyComparator(){
            return groovyComparator;
        }

        public DefaultComparator getDefaultComparator(){
            return defaultComparator;
        }
        
        public String getDisplayName() {
            return "Priority queue sorter";
        }
    }

}
