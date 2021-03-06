package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class DnDCharacter implements Serializable {
    private static final long serialVersionUID = 1L;
    // main stats
    protected int[] stats;
    // savingthrows base stats
    protected int[] savingthrowsbases;
    // infos
    protected int level;
    protected String name;
    protected String race;
    protected int runspeed;
    protected int exp;
    protected DnDClassManager classes = new DnDClassManager();
    protected ArrayList<Integer> liferolls;
    protected ArrayList<String> knownlanguages;
    protected ArrayList<Integer> basicattackbonus;
    protected int armorbonus;
    protected int shieldbonus;
    protected int naturalarmor;
    protected int deflectionarmor;
    protected int spellresist;
    protected int damagereduction;
    //    // abilities
    protected HashMap<ABILITIES, Integer> abilities;
    protected ArrayList<String> specialabilities;
    protected ArrayList<Feat> feats;
    //    protected ArrayList<Spell> knownspells;
    protected ArrayList<HashMap<Spell, Integer>> chosenspells;
    // equipment & inventory
    protected ArrayList<Equipment> equipment;
    protected ArrayList<Weapon> weapons;
    protected HashMap<String, Integer> inventory;
    // temporaries
    protected ArrayList<Integer> temphitpoints;
    protected int temphitpointsmax;
    protected int[] tempstats;
    protected int[] tempsavingthrows;
    protected ArrayList<String> tempstatuses;
    protected int tempAC;
    protected int tempattackroll;
    // other
    protected int mischitpointsmax;
    protected int miscAC;
    protected int miscinitiative;
    protected int[] miscsavingthrows;
    protected int[] miscmagicsavingthrows;
    protected int[] miscstats;
    protected int miscattackroll;

    // COSTR
    public DnDCharacter(String name, String race, String mainclass, int[] stats,
                        int runspeed, int[] savingthrowsbases) {
        this.name = name;
        this.level = 1;
        this.race = race;
        if (classes == null)
            classes = new DnDClassManager();
        classes.levelUpClass(mainclass);
        liferolls = new ArrayList<>(1);
        liferolls.add(classes.getLifeDice(mainclass));
        this.runspeed = runspeed;
        this.stats = stats;
        this.savingthrowsbases = savingthrowsbases;
        setupEmptyCharacter();
    }

    public DnDCharacter(String name, String race, String mainclass, int lifedice, int[] stats,
                        int runspeed, int[] savingthrowsbases) {
        classes = new DnDClassManager();
        classes.addNewClass(mainclass, lifedice);

        this.name = name;
        this.level = 1;
        this.race = race;
        classes.levelUpClass(mainclass);
        liferolls = new ArrayList<>(1);
        liferolls.add(classes.getLifeDice(mainclass));
        this.runspeed = runspeed;
        this.stats = stats;
        this.savingthrowsbases = savingthrowsbases;
        setupEmptyCharacter();
    }


    public int getTempstats(STATS s) {
        return tempstats[s.ordinal()];
    }

    public int getTempsavingthrows(SAVING s) {
        return tempsavingthrows[s.ordinal()];
    }

    public ArrayList<String> getTempstatuses() {
        return tempstatuses;
    }

    public int getTempAC() {
        return tempAC;
    }

    public int getTempattackroll() {
        return tempattackroll;
    }

    public int getMischitpointsmax() {
        return mischitpointsmax;
    }

    public int getMiscAC() {
        return miscAC;
    }

    public int getMiscinitiative() {
        return miscinitiative;
    }

    public int getMiscsavingthrows(SAVING saving) {
        return miscsavingthrows[saving.ordinal()];
    }

    public int getMiscmagicsavingthrows(SAVING saving) {
        return miscmagicsavingthrows[saving.ordinal()];
    }

    public int getMiscstats(STATS s) {
        return miscstats[s.ordinal()];
    }

    public int getMiscattackroll() {
        return miscattackroll;
    }

    public ArrayList<Integer> getliferolls() {
        return liferolls;
    }

    public HashMap<ABILITIES, Integer> getAbilities() {
        return abilities;
    }

    public ArrayList<String> getAbilitiesAsStrings() {
        ArrayList<String> res = new ArrayList<>(0);
        for (ABILITIES a : abilities.keySet())
            res.add(a.toString() + " mod" + getAbilityMod(a));
        Collections.sort(res);
        return res;
    }

    public int getAbilityMod(ABILITIES a) {
        if (abilities == null)
            throw new InvalidCharacterException();
        int malus = 0;
        if (a.hasMalus())
            for (Equipment e : equipment)
                if (e.savepenalty > malus)
                    malus = e.savepenalty;

        if (a == ABILITIES.NUOTARE)
            malus *= 2;

        return abilities.get(a) + getMod(a.getStat()) - malus;
    }

    public int getAC() {
        return 10 + armorbonus + shieldbonus + getMod(STATS.DEX) + naturalarmor
                + deflectionarmor + miscAC + tempAC;
    }

    public int getAttackBonus(Weapon w, int mods) {
        return getAttackBonus(w, 0, mods);
    }

    public int getAttackBonus(Weapon w, int index, int mods) {
        if (basicattackbonus.size() <= 0)
            throw new InvalidCharacterException();
        int baseattack = basicattackbonus.get(index);
        int mod = getMod(w.stat);
        int miscmod = miscattackroll;
        int tempmod = tempattackroll;
        return baseattack + mod + miscmod + tempmod + mods;
    }

    public int getAttackBonusWithCustomAttackBonus(Weapon w, int index, int mods, ArrayList<Integer> customattackbonus) {
        if (customattackbonus.size() <= 0)
            throw new InvalidCharacterException();
        int baseattack = customattackbonus.get(index);
        int mod = getMod(w.stat);
        int miscmod = miscattackroll;
        int tempmod = tempattackroll;
        return baseattack + mod + miscmod + tempmod + mods;
    }

    public ArrayList<Integer> getAttackBonuses(Weapon w, int mods) {
        if (w.customattackbonus == null) {
            ArrayList<Integer> res = new ArrayList<>(0);
            for (int i = 0; i < basicattackbonus.size(); i++)
                res.add(getAttackBonus(w, i, mods));
            return res;
        }
        //else
        ArrayList<Integer> res = new ArrayList<>(0);
        for (int i = 0; i < w.customattackbonus.size(); i++)
            res.add(getAttackBonusWithCustomAttackBonus(w, i, mods, w.customattackbonus));
        return res;
    }

    public ArrayList<Integer> getBasicAttackBonuses() {
        return basicattackbonus;
    }

    public ArrayList<String> getClasses() {
        ArrayList<String> res = new ArrayList<>(0);
        for (String s : classes.getLevelledClasses().keySet())
            res.add(s);
        return res;
    }

    public ArrayList<String> getKnownClasses() {
        ArrayList<String> res = new ArrayList<>(0);
        for (String s : classes.getKnownClasses().keySet())
            res.add(s);
        Collections.sort(res);
        return res;
    }

    public int getLotta() {
        return basicattackbonus.get(0) + getMod(STATS.STR) + miscattackroll + tempattackroll;
    }

    public int getClassLevel(String s) {
        return classes.getLevel(s);
    }

    public int getcurrentHP() {
        int res = getTotalHP();
        for (int i : temphitpoints)
            res += i;
        return res;
    }

    public String getDamageCrit(Weapon w, String misc) {
        if (w.ammo == null)
            return "Not enough ammo";
        String res = "";
        res += w.damagedices + " + ";
        if (misc != null)
            res += misc + " + ";
        if (w.stat == STATS.STR)
            res += (Math.floor(getMod(w.stat)) * w.damagemod + w.additionaldamage) + "";
        else
            res += w.additionaldamage;
        return res + "x" + w.critmult + " (" + w.notes + ")";
    }

    public String getDamageNonCrit(Weapon w, String misc) {
        if (w.ranged && w.ammo == null)
            throw new NotEnoughAmmoException();
        String res = "";
        res += w.damagedices + " + ";
        if (misc != null)
            res += misc + " + ";
        if (w.stat == STATS.STR)
            res += (Math.floor((getMod(w.stat) * w.damagemod + w.additionaldamage)));
        else
            res += w.additionaldamage;
        if (w.notes.equals("none"))
            return res;
        return res + " (" + w.notes + ")";
    }

    public int getDamageReduction() {
        return damagereduction;
    }

    public ArrayList<String> getEquipment() {
        if (equipment == null)
            throw new InvalidCharacterException();
        ArrayList<String> res = new ArrayList<>(0);
        for (Equipment a : equipment)
            res.add(a.toString());
        return res;
    }

    public int getExp() {
        return exp;
    }

    public ArrayList<String> getFeatsAsStringList() {
        if (feats == null)
            throw new InvalidCharacterException();
        ArrayList<String> res = new ArrayList<>(0);
        for (Feat a : feats)
            res.add(a.toString());
        return res;
    }

    public ArrayList<String> getFeatsAsStringListWithoutDesc() {
        if (feats == null)
            throw new InvalidCharacterException();
        ArrayList<String> res = new ArrayList<>(0);
        for (Feat a : feats)
            res.add(a.name);
        return res;
    }

    public Feat getFeat(int index) {
        return feats.get(index);
    }

    public int getGlobalLevel() {
        return level;
    }

    public int getInititative() {
        return getMod(STATS.DEX) + miscinitiative;
    }

    public ArrayList<String> getInventoryAsStringList() {
        if (inventory == null)
            throw new InvalidCharacterException();
        ArrayList<String> res = new ArrayList<>(0);
        for (String a : inventory.keySet())
            res.add(a + "| x" + inventory.get(a));
        return res;
    }

    public HashMap<String, Integer> getInventory() {
        return inventory;
    }

    public ArrayList<String> getLanguages() {
        if (knownlanguages == null)
            throw new InvalidCharacterException();
        return knownlanguages;
    }

    // METHODS
    public int getMod(STATS stat) {
        int result = (getStat(stat) - 10) / 2;

        if (stat == STATS.DEX) {
            int max = 999;

            for (Equipment e : equipment)
                if (e.maxdex < max)
                    max = e.maxdex;
            result = result > max ? max : result;
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public int getRunspeed() {
        return runspeed;
    }

    public ArrayList<String> getSpecialAbilities()
            throws InvalidCharacterException {
        if (specialabilities == null)
            throw new InvalidCharacterException();
        return specialabilities;
    }

    public int getSpellResist() {
        return spellresist;
    }

//    public ArrayList<String> getSpells() {
//        if (knownspells == null)
//            throw new InvalidCharacterException();
//        ArrayList<String> res = new ArrayList<>(0);
//        for (Spell a : knownspells)
//            res.add(a.toString());
//        return res;
//    }

    public String getSpellDesciption(int spellset, String spellname, int level) {
        Iterator it = chosenspells.get(spellset).keySet().iterator();
        Spell temp = new Spell(spellname, null, level);
        Spell s;
        while (it.hasNext())
            if (temp.equals(s = (Spell) it.next())) {
                return s.descr;
            }
        return "Spell not found";
    }

    public ArrayList<String> getSpellSets() {
        if (chosenspells == null)
            throw new InvalidCharacterException();
        ArrayList<String> res = new ArrayList<>(0);
        String temp;
        ArrayList<Spell> temparr;
        for (HashMap<Spell, Integer> h : chosenspells) {
            temp = "";
            temparr = new ArrayList<>();
            for (Spell s : h.keySet()) {
                temparr.add(s);
            }
            Collections.sort(temparr);
            for (Spell s : temparr) {
                temp += s + " x" + h.get(s) + "\n";
            }
            res.add(temp);

        }
        return res;
    }

    public ArrayList<String> getSpellSet(int index) {
        if (chosenspells == null)
            throw new InvalidCharacterException();
        ArrayList<String> res = new ArrayList<>(0);
        ArrayList<Spell> temparr;
        HashMap<Spell, Integer> h = chosenspells.get(index);
        temparr = new ArrayList<>();
        for (Spell s : h.keySet()) {
            temparr.add(s);
        }
        Collections.sort(temparr);
        for (Spell s : temparr) {
            res.add(s + " x" + h.get(s));
        }

        return res;
    }

    public int getSprovvista() {
        return getAC() - getMod(STATS.DEX);
    }

    public int getStat(STATS stat) {
        if (stats[stat.ordinal()] <= 0)
            throw new InvalidCharacterException();
        return stats[stat.ordinal()] + tempstats[stat.ordinal()] + miscstats[stat.ordinal()];
    }

    public int getUnmodifiedStat(STATS stat) {
        if (stats[stat.ordinal()] <= 0)
            throw new InvalidCharacterException();
        return stats[stat.ordinal()];
    }

    public ArrayList<String> getStatuses() {
        if (tempstatuses == null)
            throw new InvalidCharacterException();
        return tempstatuses;
    }

    public ArrayList<Integer> getTempHP() {
        return temphitpoints;
    }

    public int getTempHPMax() {
        return temphitpointsmax;
    }

    public int[] getSavingthrowsbases() {
        return savingthrowsbases;
    }

    public int getThrow(SAVING s) {
        int index = s.ordinal();
        if (savingthrowsbases[index] < 0)
            throw new InvalidCharacterException();

        return savingthrowsbases[index] + getMod(STATS.values()[s.getRelatedStat()])
                + miscmagicsavingthrows[index] + miscsavingthrows[index]
                + tempsavingthrows[index];
    }

    public int getTotalHP() {
        if (level <= 0)
            throw new InvalidCharacterException();
        if (liferolls.size() != level)
            throw new InvalidCharacterException();

        int res = 0;
        for (int i : liferolls) {
            res += i + getMod(STATS.CON) < 1 ? 1 : i + getMod(STATS.CON);
        }

        return res + mischitpointsmax + temphitpointsmax;
    }

    public int getTouch() {
        return getAC() - armorbonus - shieldbonus - naturalarmor;
        // return 10 + getMod(STATS.DEX) + deflectionarmor + miscAC;
    }

    public ArrayList<String> getWeapons() {
        if (weapons == null)
            throw new InvalidCharacterException();
        ArrayList<String> res = new ArrayList<>(0);
        String temp;
        for (Weapon w : weapons) {
            temp = "";
            temp += "[ATK ROLLS: ";
            for (Integer i : getAttackBonuses(w, 0))
                temp += i + "/";
            temp = temp.substring(0, temp.length() - 1);
            temp += "] [";
            temp += getDamageNonCrit(w, null) + "] ";
            temp += w.toString();

            res.add(temp);
        }
        return res;
    }

    public Weapon getWeapon(int index) {
        return weapons.get(index);
    }

    private void setDefaultAbilities() {
        for (ABILITIES a : ABILITIES.values()) {
            if (a.isDef()) {
                abilities.put(a, 0);
            }
        }
    }

    //
    private void setupEmptyCharacter() {
        knownlanguages = new ArrayList<>(1);
        knownlanguages.add("Common");
        basicattackbonus = new ArrayList<>(1);
        basicattackbonus.add(1);
        abilities = new HashMap<>(0);
        setDefaultAbilities();
        specialabilities = new ArrayList<>(0);
        feats = new ArrayList<>(0);
//        knownspells = new ArrayList<>(0);
        chosenspells = new ArrayList<>(1);
        chosenspells.add(new HashMap<Spell, Integer>(0));
        temphitpoints = new ArrayList<>(0);
        tempstats = new int[6];
        tempsavingthrows = new int[3];
        tempstatuses = new ArrayList<>(0);
        equipment = new ArrayList<>(0);
        weapons = new ArrayList<>(0);
        inventory = new HashMap<>(0);
        miscsavingthrows = new int[3];
        miscmagicsavingthrows = new int[3];
        miscstats = new int[6];
    }


    protected void variousChecks() {
        boolean raise = false;

        //
        int level = 0;
        for (int i : classes.getLevelledClasses().values())
            level += i;
        if (level != this.level)
            raise = true;
        //

        if (raise)
            throw new InvalidCharacterException();
    }

    public static enum SAVING {
        FORTITUDE(STATS.CON.ordinal()), REFLEX(STATS.DEX.ordinal()), WILL(STATS.WIS.ordinal());
        private final int val;

        private SAVING(int v) {
            val = v;
        }

        public int getRelatedStat() {
            return val;
        }
    }

    public static enum STATS {
        STR, DEX, CON, INT, WIS, CHA;

        public String toString() {
            switch (this) {
                case STR:
                    return "Strenght";
                case DEX:
                    return "Dexterity";
                case CON:
                    return "Constitution";
                case INT:
                    return "Intelligence";
                case WIS:
                    return "Wisdom";
                case CHA:
                    return "Charisma";
                default:
                    return null;
            }
        }
    }

    public static class InvalidCharacterException extends RuntimeException {

    }

    public static class NotEnoughAmmoException extends RuntimeException {
    }

}
