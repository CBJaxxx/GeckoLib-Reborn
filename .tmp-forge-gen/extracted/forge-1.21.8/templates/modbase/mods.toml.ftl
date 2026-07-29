modLoader="javafml"
loaderVersion="[58,)"
license="${JavaConventions.escapeStringForJava(settings.getLicense())}"

[[mods]]
modId="${settings.getModID()}"
version="${settings.getCleanVersion()}"
displayName="${JavaConventions.escapeStringForJava(settings.getModName())}"
<#if settings.getUpdateURL()?has_content>
updateJSONURL="${JavaConventions.escapeStringForJava(settings.getUpdateURL())}"
</#if>
<#if settings.getWebsiteURL()?has_content>
displayURL="${JavaConventions.escapeStringForJava(settings.getWebsiteURL())}"
</#if>
<#if settings.getModPicture()?has_content>
logoFile="logo.png"
</#if>
<#if settings.getCredits()?has_content>
credits="${JavaConventions.escapeStringForJava(settings.getCredits())}"
</#if>
<#if settings.getAuthor()?has_content>
authors="${JavaConventions.escapeStringForJava(settings.getAuthor())}"
</#if>
<#if settings.getDescription()?has_content>
description='''${settings.getDescription()}'''
</#if>
<#if settings.isServerSideOnly()>
displayTest="IGNORE_SERVER_VERSION"
</#if>

# Start of user code block mod configuration
# End of user code block mod configuration

<#if w.getGElementsOfType('biome')?filter(e -> e.spawnBiome || e.spawnInCaves || e.spawnBiomeNether)?size != 0>
[[mixins]]
    config = "${settings.getModID()}.mixins.json"
</#if>

# Start of user code block custom mixins
# End of user code block custom mixins

[[dependencies.${settings.getModID()}]]
    modId="forge"
    mandatory=true
    versionRange="[${generator.getGeneratorBuildFileVersion()},)"
    ordering="NONE"
    side="BOTH"

[[dependencies.${settings.getModID()}]]
    modId="minecraft"
    mandatory=true
    versionRange="[${generator.getGeneratorMinecraftVersion()},1.22)"
    ordering="NONE"
    side="BOTH"

<#list settings.getRequiredMods() as e>
[[dependencies.${settings.getModID()}]]
    modId="${e}"
    mandatory=true
    versionRange="[0,)"
    ordering="NONE"
    side="BOTH"
</#list>

<#list settings.getDependencies() as e>
[[dependencies.${settings.getModID()}]]
    modId="${e}"
    mandatory=false
    versionRange="[0,)"
    ordering="NONE"
    side="BOTH"
</#list>

<#list settings.getDependants() as e>
[[dependencies.${settings.getModID()}]]
    modId="${e}"
    mandatory=false
    versionRange="[0,)"
    ordering="NONE"
    side="BOTH"
</#list>

# Start of user code block dependencies configuration
# End of user code block dependencies configuration
