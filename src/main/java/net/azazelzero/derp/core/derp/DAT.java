package net.azazelzero.derp.core.derp;

import net.azazelzero.derp.core.derp.requirements.DatRequirement;
import net.azazelzero.derp.core.derp.requirements.RequirementRegistryEntry;
import net.azazelzero.derp.core.derp.skillactions.SkillAction;
import net.azazelzero.derp.core.derp.skillactions.SkillActionEntry;
import org.luaj.vm2.ast.Str;

import java.io.Serializable;

public class DAT implements Serializable {
    public String name;
    public String Id;
    public SkillReference Info;
    public String Description="the quick brown fox jumped over the lazy dog";
    public int Panel;
    public int X;
    public int Y;
    public boolean unlocked=false;
    public boolean beingDragged=false;
    public boolean Slottable=false;
    public String SlottableEvent;
    public int Cooldown;
    public String Icon="textures/item/apple.png";
    public DatRequirement[] Requirements;
    public SkillAction[] Actions;

}
