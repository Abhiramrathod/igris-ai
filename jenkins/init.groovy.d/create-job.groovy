import jenkins.model.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import hudson.model.*

def instance = Jenkins.getInstance()

// Create the pipeline job pointing to Jenkinsfile in workspace
def job = new WorkflowJob(instance, "ai-igris")
def flowDef = new CpsFlowDefinition(new File("/workspace/Jenkinsfile").text, true)
job.setDefinition(flowDef)

instance.createProject(WorkflowJob, "ai-igris")
instance.save()

println "Pipeline job 'ai-igris' created successfully"
