import jenkins.model.*
import org.jenkinsci.plugins.workflow.job.WorkflowJob
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition
import hudson.model.*

def instance = Jenkins.getInstance()

// Set Jenkins URL
def jlc = JenkinsLocationConfiguration.get()
jlc.setUrl("http://localhost:2121")
jlc.save()

// Enable CLI over Remoting
instance.setDisableRemotingJnlp(false)

// Create the pipeline job pointing to Jenkinsfile in workspace
def job = new WorkflowJob(instance, "ai-igris")
def flowDef = new CpsFlowDefinition(new File("/workspace/Jenkinsfile").text, true)
job.setDefinition(flowDef)

instance.createProject(WorkflowJob, "ai-igris")
instance.save()

println "Pipeline job 'ai-igris' created successfully"
println "Jenkins URL set to: http://localhost:2121"
