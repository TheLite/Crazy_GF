/*
 * Copyright 2016 Mark Fairchild.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package resaver.ess;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import resaver.Analysis;
import resaver.ess.papyrus.Papyrus;
import resaver.ess.papyrus.PapyrusContext;
import resaver.ess.papyrus.ScriptInstance;
import static j2html.TagCreator.*;
import j2html.tags.DomContent;
import static resaver.ResaverFormatting.makeMetric;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * Abstraction for plugins.
 *
 * @author Mark Fairchild
 */
final public class Plugin implements AnalyzableElement, Linkable, Comparable<Plugin>, java.io.Serializable {

    static public Plugin PROTOTYPE = new Plugin("Unofficial Skyrim Legendary Edition Patch", -1, false);

    /**
     * Creates a new <code>Plugin</code> by reading from an input stream.
     *
     * @param input The input stream.
     * @param index The index of the plugin.
     * @return The new Plugin.
     */
    static public Plugin readFullPlugin(ByteBuffer input, int index) {
        Objects.requireNonNull(input);
        if (index < 0 || index > 255) {
            throw new IllegalArgumentException("Invalid index: " + index);
        }

        String name = mf.BufferUtil.getWString(input);
        return new Plugin(name, index, false);

    }

    /**
     * Creates a new <code>Plugin</code> by reading from an input stream.
     *
     * @param input The input stream.
     * @param index The index of the plugin.
     * @return The new Plugin.
     * @throws IOException
     */
    static public Plugin readLitePlugin(ByteBuffer input, int index) throws IOException {
        if (index < 0 || index >= 4096) {
            throw new IllegalArgumentException("Invalid index: " + index);
        }

        String name = mf.BufferUtil.getWString(input);
        return new Plugin(name, index, true);

    }

    /**
     * Creates a new <code>Plugin</code> that is not part of a savefile.
     *
     * @param name The name for the unloaded plugin.
     * @return The new Plugin.
     */
    static public Plugin makeUnloadedPlugin(String name) {
        Objects.requireNonNull(name);
        return new Plugin(name, -1, false);
    }

    /**
     * Creates a new Created pseudo-plugin.
     * @return The Created plugin.
     */
    static public Plugin makeCreated() {
        return new Plugin("(Created)", 0xFF, false);
    }

    /**
     * Creates a new <code>Plugin</code>.
     *
     * @param name The name of the plugin.
     * @param index The index of the plugin.
     * @param lightweight A flag indicating that it is a lightweight plugin.
     * @throws IOException
     */
    private Plugin(String name, int index, boolean lightweight) {
        Objects.requireNonNull(name);
        this.NAME = name;
        this.INDEX = index;
        this.LIGHTWEIGHT = lightweight;
    }

    /**
     * @see resaver.ess.Element#write(ByteBuffer)
     * @param output The output stream.
     */
    @Override
    public void write(ByteBuffer output) {
        mf.BufferUtil.putWString(output, this.NAME);
    }

    /**
     * @see resaver.ess.Element#calculateSize()
     * @return The size of the <code>Element</code> in bytes.
     */
    @Override
    public int calculateSize() {
        return 2 + this.NAME.getBytes(StandardCharsets.ISO_8859_1).length;
    }

    /**
     * @see resaver.ess.Linkable#toHTML(Element)
     * @param target A target within the <code>Linkable</code>.
     * @return
     */
    @Override
    public String toHTML(Element target) {
        return Linkable.makeLink("plugin", this.NAME, this.toString());
    }

    /**
     * @return String representation.
     */
    @Override
    public String toString() {
        return this.NAME;
    }

    /**
     * @return String representation.
     */
    public String indexName() {
        return this.LIGHTWEIGHT
                ? String.format("FE%03x: %s", this.INDEX, this.NAME)
                : String.format("%02x: %s", this.INDEX, this.NAME);
    }

    /**
     * Finds all of the changeforms associated with this Plugin.
     *
     * @param save The savefile to search.
     * @return A set of changeforms.
     */
    public Set<ChangeForm> getChangeForms(ESS save) {
        final Set<ChangeForm> FORMS = save.getChangeForms().stream()
                .filter(form -> Objects.equals(this, form.getRefID().PLUGIN))
                .collect(Collectors.toSet());

        return FORMS;
    }

    /**
     * Finds all of the scriptinstances associated with this Plugin.
     *
     * @param save The savefile to search.
     * @return A set of scriptinstances.
     */
    public Set<ScriptInstance> getInstances(ESS save) {
        final Set<ScriptInstance> INSTANCES = save.getPapyrus().getScriptInstances().values().stream()
                .filter(instance -> instance.getRefID() != null)
                .filter(instance -> instance.getRefID().PLUGIN == this)
                .collect(Collectors.toSet());

        return INSTANCES;
    }

    private String createIndexString() {
        return this.LIGHTWEIGHT
                ? String.format("FE%02x", INDEX)
                : String.format("%02x", INDEX);
    }
    
    /**
     * @see AnalyzableElement#getInfo(resaver.Analysis, resaver.ess.ESS)
     * @param analysis
     * @param ess
     * @return
     */
    @Override
    public String getInfo(resaver.Analysis analysis, ESS ess) {

        Set<ScriptInstance> instances = this.getInstances(ess);
        Set<ChangeForm> forms = this.getChangeForms(ess);
        java.util.function.IntFunction<DomContent[]> DomList = n -> new DomContent[n];
        PluginMetrics metrics = this.createPluginMetrics(ess, analysis);
        
        List<String> instanceNames = instances.stream()
                .limit(50)
                .map(i -> i.toHTML(null))
                .collect(Collectors.toList());
        List<String> formNames = forms.stream()
                .limit(50)
                .map(i -> i.toLinkedString())
                .collect(Collectors.toList());
        
        return html(body(
                h2(MessageFormat.format("{0} PLUGIN", LIGHTWEIGHT ? "LITE" : "FULL")),
                p(MessageFormat.format("Name: {0}", NAME)),
                p(MessageFormat.format("Index: {0}", createIndexString())),
                h3("Metrics"),
                dl(
                        dt("ChangeForms"),
                        dd(makeMetric(metrics.changeFormCount, null, metrics.changeFormTotal, metrics.changeFormPercentage)),
                        dt("ScriptInstances"),
                        dd(makeMetric(metrics.scriptInstanceCount, null, metrics.scriptInstanceTotal, metrics.scriptInstancePercentage)),
                        dt("Plugin-Unique Data"),
                        dd(makeMetric(metrics.uniqueData/1024, "kb", null, metrics.changeFormPercentage)),
                        dt("Indirect ChangeForms"),
                        dd(makeMetric(metrics.indirectChangeFormCount, null, metrics.changeFormTotal, metrics.changeFormPercentage)),
                        dt("Indirect ScriptInstances"),
                        dd(makeMetric(metrics.indirectInstanceCount, null, metrics.scriptInstanceTotal, metrics.scriptInstancePercentage))
                ),
                h3("Mods"),
                metrics.mod
                        .map(mod -> p(
                                h4(MessageFormat.format("The plugin probably came from \"{0}\".", mod.probableProvider)),
                                h4(MessageFormat.format("{0} script files were found in the mod list.", mod.numScripts)),
                                h4("Providers"),
                                ul(mod.providers.stream().limit(20).map(p -> li(p)).toArray(DomList))
                        ))
                        .orElse(p(em(analysis == null 
                                ? "Mod analysis is only available for Mod Organizer 2 and requires Plugin Parsing to be enabled."
                                : "No analysis information is available for this plugin."))),
                
                h3(instances.size() > 50 ? "Script Instances (first 50)" : "ChangeForms"),
                instances.isEmpty() 
                    ? p(em("None"))
                    : ul(each(instanceNames, name -> li(rawHtml(name)))),
                
                h3(forms.size() > 50 ? "ChangeForms (first 50)" : "ChangeForms"),
                forms.isEmpty() 
                    ? p(em("None"))
                    : ul(each(formNames, name -> li(rawHtml(name))))
                    //: ul(forms.stream().limit(50).map(i -> li(i.toHTML(null))).toArray(DomList))

        )).render();
    }

    /**
     * @see AnalyzableElement#matches(resaver.Analysis, resaver.Mod)
     * @param analysis
     * @param mod
     * @return
     */
    @Override
    public boolean matches(Analysis analysis, String mod) {
        Objects.requireNonNull(analysis);
        Objects.requireNonNull(mod);

        Predicate<String> filter = esp -> esp.equalsIgnoreCase(NAME);
        final List<String> PROVIDERS = new ArrayList<>();

        analysis.ESPS.forEach((m, esps) -> {
            esps.stream().filter(filter).forEach(esp -> PROVIDERS.add(m));
        });

        return PROVIDERS.contains(mod);
    }

    /**
     * The name field.
     */
    final public String NAME;

    /**
     * The index field.
     */
    final public int INDEX;

    /**
     *
     */
    final public boolean LIGHTWEIGHT;

    /**
     * @see Comparable#compareTo(java.lang.Object)
     * @param o
     * @return
     */
    @Override
    public int compareTo(Plugin o) {
        if (null == o) {
            return 1;
        }

        return Objects.compare(this.NAME, o.NAME, String::compareToIgnoreCase);
    }

    /**
     * @see Object#hashCode()
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.NAME.toLowerCase());
        return hash;
    }

    /**
     * @see Object#equals(java.lang.Object)
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            final Plugin other = (Plugin) obj;
            return this.NAME.equalsIgnoreCase(other.NAME);
        }
    }

    public PluginMetrics createPluginMetrics(ESS ess, Analysis analysis) {
        Papyrus papyrus = ess.getPapyrus();
        PapyrusContext context = papyrus.getContext();
        Set<Element> uniqueRefs = context.getPluginReferences(this);
        
        PluginMetrics metrics = new PluginMetrics();
        
        metrics.changeFormCount = this.getChangeForms(ess).size();
        metrics.changeFormTotal = ess.getChangeForms().size();
        metrics.changeFormPercentage = (float)metrics.changeFormCount / (float)metrics.changeFormTotal;
        metrics.indirectChangeFormCount = (int)uniqueRefs.stream().filter(f -> f instanceof ChangeForm).count();
        
        metrics.scriptInstanceCount = this.getInstances(ess).size();
        metrics.scriptInstanceTotal = papyrus.getScriptInstances().size();
        metrics.scriptInstancePercentage = (float)metrics.scriptInstanceCount / (float)metrics.scriptInstanceTotal;
        metrics.indirectInstanceCount = (int)uniqueRefs.stream().filter(f -> f instanceof ScriptInstance).count();

        metrics.uniqueData = uniqueRefs.stream().mapToInt(e -> e.calculateSize()).sum();
        
        metrics.uniqueDataPercentage = (float)metrics.uniqueData / (float)ess.getOriginalSize();

        if (null == analysis) {
            metrics.mod = Optional.empty();
        } else {
            ModMetrics mod = new ModMetrics();
            mod.providers = analysis.ESPS.entrySet()
                        .stream()
                        .filter(e -> e.getValue().stream().anyMatch(NAME::equalsIgnoreCase))
                        .map(e -> e.getKey())
                        .collect(Collectors.toList());
                
            if (mod.providers.isEmpty()) {
                metrics.mod = Optional.empty();
                
            } else {
                metrics.mod = Optional.of(mod);
                mod.probableProvider = mod.providers.get(mod.providers.size()-1);

                Predicate<SortedSet<String>> modFilter = e -> e.contains(mod.probableProvider);
                mod.numScripts = (int) analysis.SCRIPT_ORIGINS.values().stream().filter(modFilter).count();                
            }
        }
        
        return metrics;
    }
    
    public class PluginMetrics {
        
        int changeFormCount;
        int changeFormTotal;
        int indirectChangeFormCount;
        float changeFormPercentage;
        
        int scriptInstanceCount;
        int scriptInstanceTotal;
        int indirectInstanceCount;
        float scriptInstancePercentage;
        
        int uniqueData;
        float uniqueDataPercentage;
        
        Optional<ModMetrics> mod;
        
    }
    
    public class ModMetrics {
        int numScripts;
        String probableProvider;
        List<String> providers;
    }
}
