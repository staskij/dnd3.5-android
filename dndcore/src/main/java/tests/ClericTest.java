package tests;

import java.util.ArrayList;

import core.DnDCharacter.STATS;
import core.DnDCharacterManipulator;
import core.Equipment;
import core.Equipment.TYPE;
import core.Weapon;

public class ClericTest {
    public static void main(String[] args) {
        creaCleric();
    }

    private static void creaCleric() {
        int[] stats = new int[]{14, 12, 16, 10, 18, 12};
        int[] sav = new int[]{4, 1, 4};
        DnDCharacterManipulator cleric = new DnDCharacterManipulator("Cleric", "Umano",
                "Cleric", stats, 6, sav);
        cleric.levelup("Cleric", 3, null, null, 0, null);
        cleric.levelup("Cleric", 6, null, null, 0, null);
        cleric.levelup("Cleric", 4, null, null, 0, null);
        cleric.levelup("Cleric", 5, null, null, 0, null);
        ArrayList<Integer> atkrolls = new ArrayList<>();
        atkrolls.add(4);
        sav = new int[]{5, 2, 5};
        cleric.levelup("Cleric", 4, atkrolls, STATS.WIS, 1,
                sav);
        Weapon w = new Weapon("Mazza pes Folgore", false, STATS.STR, 1, 1.5,
                "1d8", 20, 2);
        w.notes = "+1d6 Elettr";
        cleric.setWeapon(w, false);
        w = new Weapon("Balestra Pes", true, STATS.DEX, 1, 30,
                "1d10", 19, 2);
        w.ammo = 1;
        cleric.setWeapon(w, false);
        Equipment cor = new Equipment("Armatura completa+1");
        cor.acbonus = 9;
        cor.maxdex = 1;
        cleric.setEquipment(cor, false);
        Equipment shield = new Equipment("Shield", TYPE.SHIELD);
        shield.acbonus = 3;
        shield.savepenalty = 2;
        cleric.setEquipment(shield, false);
        Equipment neck = new Equipment("Amuleto Arm Nat");
        neck.naturalbonus = 1;
        cleric.setEquipment(neck, false);
        Equipment ring = new Equipment("Anello Protettivo +1");
        ring.deflectionbonus = 1;
        cleric.setEquipment(ring, false);
        System.out.println("=====START=====");
        System.out.println(cleric.toString());
        System.out.println("=====================");
    }

}
