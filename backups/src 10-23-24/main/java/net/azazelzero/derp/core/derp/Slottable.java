package net.azazelzero.derp.core.derp;

import net.azazelzero.derp.core.derp.skillactions.SkillActionEntry;

import java.io.Serializable;

public class Slottable implements Serializable {
    final String PlayerName;
    final SkillActionEntry[] Actions;
    public Slottable(String playerName, SkillActionEntry[] actions){
        PlayerName=playerName;
        Actions=actions;
    }

}
