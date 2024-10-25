package net.azazelzero.derp.core.derp;

import net.azazelzero.derp.core.derp.requirements.RequirementRegistryEntry;
import net.azazelzero.derp.core.derp.skillactions.SkillActionEntry;
import org.luaj.vm2.ast.Str;

import java.io.Serializable;

public class DAT implements Serializable {
    public String name;
    public String Description="the quick brown fox jumped over the lazy dog";
    public int Panel;
    public int X;
    public int Y;
    public boolean unlocked=false;
    public boolean Slottable;
    public String Icon="textures/item/apple.png";
    public RequirementRegistryEntry[] Requirements;
    public SkillActionEntry[] Actions;

}
