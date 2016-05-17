package uk.co.wehavecookies56.kizunacraft;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

/**
 * Created by Toby on 17/05/2016.
 */
public class KiznaiverDamage extends EntityDamageSource {

    public KiznaiverDamage(String damageTypeIn, Entity damageSourceEntityIn) {
        super(damageTypeIn, damageSourceEntityIn);
        this.setDamageBypassesArmor();
    }
}
