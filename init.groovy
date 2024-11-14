import jenkins.*
import jenkins.model.* 
import hudson.*
import hudson.model.*


// find all plugins related to Bitbucket

def pm = Jenkins.instance.pluginManager

def bbPlugins = pm.plugins.findAll { plugin ->
  def description = plugin.getManifest().getMainAttributes().getValue("Long-Name")
  description && description.toLowerCase().contains("bitbucket")
}

// iterate over the list and disable each one

bbPlugins.each { plugin ->
  println("Disabling ${plugin.getShortName()}")
  plugin.disable()
}

Jenkins.instance.save()
