import jenkins.model.*
import java.lang.reflect.Field
import org.jenkinsci.plugins.ghprb.GhprbGitHubAuth
import hudson.util.Secret
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*

println "--> setting ghprhook creds"

def global_domain = Domain.global()

def credentialsStore =
        Jenkins.instance.getExtensionList(
                'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
        )[0].getStore()

def envVars = Jenkins.instance.getGlobalNodeProperties()[0].getEnvVars()

if(!envVars['JENKINS_SECURITY_INITIALIZED'] || envVars['JENKINS_SECURITY_INITIALIZED'] != "true") {
        // IMPORTANT! corporate url has different format: https://github.mydomain.com/api/v3
        def gitHubApiUrl = "https://api.github.com"

        def id = "ghprbhook-token"
        def username = "CHANGE_ME"
        def password = "CHANGE_ME"
        def description = "GitHub Pull Request Builder token"

        def ghprbhookCredentials = new UsernamePasswordCredentialsImpl(
                CredentialsScope.GLOBAL,
                id,
                description,
                username,
                password
        )

        credentialsStore.addCredentials(global_domain, ghprbhookCredentials)

        def descriptor = Jenkins.instance.getDescriptorByType(org.jenkinsci.plugins.ghprb.GhprbTrigger.DescriptorImpl.class)

        Field auth = descriptor.class.getDeclaredField("githubAuth")

        auth.setAccessible(true)

        def githubAuth = new ArrayList<GhprbGitHubAuth>(1)

        Secret secret = Secret.fromString('')
        githubAuth.add(new GhprbGitHubAuth(gitHubApiUrl, "", id, description, username, secret))

        auth.set(descriptor, githubAuth)

        descriptor.save()
}