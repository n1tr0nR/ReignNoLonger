package com.nitron.reign_no_longer.server.particles;

import com.nitron.reign_no_longer.ReignNoLonger;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ReignNoLongerParticles {
    public static final DefaultParticleType ORBITAL_STRIKE_PARTICLE = FabricParticleTypes.simple();

    public static void register() {
        Registry.register(Registries.PARTICLE_TYPE, new Identifier(ReignNoLonger.MOD_ID, "orbital_strike_particle"), ORBITAL_STRIKE_PARTICLE);
    }
}
