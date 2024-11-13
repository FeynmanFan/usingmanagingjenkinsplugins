import jenkins.*
import jenkins.model.* 
import hudson.*
import hudson.model.*

pipeline {
    agent any
    stages {
        stage ('Plugin Management') {
            steps {
                script {
                    // define the whitelist
                    def pluginManager = Jenkins.instance.pluginManager
                    
                    def whitelist = ['ant',
'antisamy-markup-formatter',
'apache-httpcomponents-client-4-api',
'atlassian-bitbucket-server-integration',
'authentication-tokens',
'bitbucket',
'blueocean-commons',
'blueocean-core-js',
'blueocean-jwt',
'blueocean-pipeline-api-impl',
'blueocean-pipeline-scm-api',
'blueocean-rest-impl',
'blueocean-rest',
'blueocean-web',
'bootstrap5-api',
'bouncycastle-api',
'branch-api',
'build-failure-analyzer',
'build-timeout',
'caffeine-api',
'checks-api',
'cloudbees-bitbucket-branch-source',
'cloudbees-folder',
'commons-lang3-api',
'commons-text-api',
'credentials-binding',
'credentials',
'dark-theme',
'display-url-api',
'dotnet-sdk',
'durable-task',
'echarts-api',
'email-ext',
'favorite',
'font-awesome-api',
'git-client',
'git',
'github-api',
'github-branch-source',
'github',
'gradle',
'gson-api',
'handy-uri-templates-2-api',
'htmlpublisher',
'instance-identity',
'ionicons-api',
'jackson2-api',
'jakarta-activation-api',
'jakarta-mail-api',
'javax-activation-api',
'javax-mail-api',
'jaxb',
'jenkins-design-language',
'jjwt-api',
'joda-time-api',
'jquery3-api',
'jsch',
'json-api',
'json-path-api',
'junit',
'ldap',
'login-theme',
'mailer',
'matrix-auth',
'matrix-project',
'mercurial',
'metrics',
'mina-sshd-api-common',
'mina-sshd-api-core',
'okhttp-api',
'pam-auth',
'pipeline-build-step',
'pipeline-github-lib',
'pipeline-graph-analysis',
'pipeline-groovy-lib',
'pipeline-input-step',
'pipeline-milestone-step',
'pipeline-model-api',
'pipeline-model-definition',
'pipeline-model-extensions',
'pipeline-rest-api',
'pipeline-stage-step',
'pipeline-stage-tags-metadata',
'pipeline-stage-view',
'plain-credentials',
'plugin-util-api',
'pubsub-light',
'resource-disposer',
'scm-api',
'script-security',
'snakeyaml-api',
'ssh-credentials',
'ssh-slaves',
'stashNotifier',
'structs',
'theme-manager',
'timestamper',
'token-macro',
'trilead-api',
'variant',
'workflow-aggregator',
'workflow-api',
'workflow-basic-steps',
'workflow-cps',
'workflow-durable-task-step',
'workflow-job',
'workflow-multibranch',
'workflow-scm-step',
'workflow-step-api',
'workflow-support',
'ws-cleanup']
                    
                    // get installed plugins
                    def installedPlugins = pluginManager.plugins.collect { it.shortName}
                    
                    // uninstall plugins not in the whitelist
                    def pluginsToRemove = installedPlugins - whitelist
                    
                    def isDirty = false;
                    
                    if (!pluginsToRemove.isEmpty()){
                        isDirty = true;
                        
                        pluginsToRemove.each { pluginName ->
                           def plugin = pluginManager.getPlugin(pluginName)
                           if (plugin){
                               echo "Removing '${pluginName}'"
                               plugin.doDoUninstall()
                           }
                        }
                    }
                    
                    // install plugins in the whitelist but not in the installed set
                    def pluginsToInstall = whitelist - installedPlugins;

                    if (!pluginsToInstall.isEmpty()){
                        isDirty = true;
                        
                        def updateCenter = Jenkins.instance.updateCenter
                        pluginsToInstall.each {pluginName -> 
                           def plugin = updateCenter.getPlugin(pluginName)
                           if (plugin) {
                               echo "Installing ${pluginName}"
                               plugin.deploy()
                           }
                        }
                    }
                    
                    // save the instance
                    if (isDirty){
                        Jenkins.instance.save()
                    }
                }
            }
        }
    }
    
}