package com.zafiru.characters;

import com.zafiru.components.Sprite;
import com.zafiru.equipments.EquipmentSlot;
import com.zafiru.equipments.IEquipment;
import com.zafiru.equipments.IEquipmentFactory;
import com.zafiru.equipments.weapons.IWeapon;
import com.zafiru.equipments.weapons.behaviours.IWeaponBehaviour;
import com.zafiru.observables.Health;
import com.zafiru.observables.Mana;
import com.zafiru.spells.ISpell;

import java.util.HashMap;

public abstract class ICharacter extends Sprite {

    private String mName;
    private String mType;
    private Health mHealth;
    private Mana mMana;
    private ICharacter mTarget;

    private HashMap<EquipmentSlot, IEquipment> equipments = new HashMap<>();

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Health getHealth() {
        return mHealth;
    }

    public void setHealth(Health health) {
        this.mHealth = health;
    }

    public Mana getMana() {
        return mMana;
    }

    public void setMana(Mana mana) {
        this.mMana = mana;
    }

    public ICharacter getTarget() {
        return mTarget;
    }

    public void setTarget(ICharacter target) {
        this.mTarget = target;
    }

    public void equip(EquipmentSlot slot, String name) {
        /*
        ICharacter doesn't know or care about the equipment itself.
        It doesn't need to know even if it is a weapon or an armor.
        It just gets the equipment and assigns it to corresponding slot.
         */

        IEquipmentFactory factory = IEquipmentFactory.getFactory(slot);
        IEquipment equipment = factory.getEquipment(name);

        if (equipment == null) {
            System.out.println("Unknown Item");
            return;
        } else if (equipment.getSlot() != slot) {
            System.out.println("Can't equip item to that slot");
            return;
        }

        if (equipments.containsKey(slot)) {
            System.out.println("An item is already equipped in that slot");
        } else {
            equipments.put(slot, equipment);
            System.out.println(equipment.getName() + " is equipped");
        }
    }

    public void unequip(EquipmentSlot slot) {
        equipments.remove(slot);
        System.out.println("Item unequipped");
    }

    public IWeapon getWeapon() {
        if (equipments.containsKey(EquipmentSlot.TWO_HAND)) {
            return (IWeapon) equipments.get(EquipmentSlot.TWO_HAND);
        } else if (equipments.containsKey(EquipmentSlot.RIGHT_HAND)) {
            return (IWeapon) equipments.get(EquipmentSlot.RIGHT_HAND);
        } else {
            return null;
        }
    }

    public void updateWeapon(IEquipment equipment) {
        equipments.put(equipment.getSlot(), equipment);
    }

    public void hit() {
        IWeapon weapon = getWeapon();

        if (getTarget() != null && weapon != null) {
            int damage = weapon.calculateDamage();
            getTarget().getHealth().decrease(damage);
        }
    }

    public void move() {
        System.out.println(getType() + " moved");
    }

    public void cast(ISpell spell) {
        if (getMana() != null && getTarget() != null) {
            if (getMana().getValue() > spell.getMana()) {
                getMana().decrease(spell.getMana());
                spell.cast();
            } else {
                System.out.println("Not enough mana");
            }
        }
    }
}
