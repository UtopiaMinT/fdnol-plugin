package me.lkp111138.plugin.rpg.damage;

public class ElementalDamage {
    public int neutral;
    public int earth;
    public int water;
    public int fire;
    public int thunder;

    public int sum() {
        return neutral + earth + water + fire + thunder;
    }
}
