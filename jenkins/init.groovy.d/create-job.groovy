import jenkins.model.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import hudson.model.*

def instance = Jenkins.getInstance()

// Set Jenkins URL
def jlc = JenkinsLocationConfiguration.get()
jlc.setUrl("http://localhost:2121")
jlc.save()

// Enable CLI
instance.setDisableRemotingJnlp(false)

// Create pipeline job only if it doesn't exist
def jobName = "ai-igris"
def existingJob = Jenkins.instance.getItem(jobName)
if (existingJob == null) {
    def job = new WorkflowJob(instance, jobName)
    def flowDef = new CpsFlowDefinition(new File("/workspace/Jenkinsfile").text, true)
    job.setDefinition(flowDef)
    instance.createProject(WorkflowJob, jobName)
    instance.save()
    println "Pipeline job '${jobName}' created successfully"
} else {
    println "Pipeline job '${jobName}' already exists, skipping creation"
}

println "Jenkins URL: http://localhost:2121"
