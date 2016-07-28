package uk.co.wehavecookies56.kizunacraft;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by Toby on 17/05/2016.
 */
public class KiznaiverDamage extends EntityDamageSource {

    public KiznaiverDamage(String damageTypeIn, Entity damageSourceEntityIn) {
        super(damageTypeIn, damageSourceEntityIn);
        this.setDamageBypassesArmor();
    }

    @Override
    public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
        return new TextComponentTranslation("death.attack.kizunacraft.kiznaiverDamage", entityLivingBaseIn.getDisplayName());
    }
}
