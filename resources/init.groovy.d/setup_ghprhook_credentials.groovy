import jenkins.model.*
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

def env = System.getenv()
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
