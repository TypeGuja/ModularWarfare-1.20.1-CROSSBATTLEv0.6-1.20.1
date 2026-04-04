package com.modularwarfare.common.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityBloodFX extends TextureSheetParticle {
    private boolean isCollided;

    protected EntityBloodFX(ClientLevel level, double x, double y, double z,
                            double dx, double dy, double dz, double mult) {
        super(level, x, y, z, dx, dy, dz);
        this.gravity = 1.2f;
        this.rCol = 1.0f;
        this.gCol = 0.0f;
        this.bCol = 0.0f;
        this.alpha = 0.0f;

        double scale = 1.5 * ((1.0 + mult) / 2.0);
        this.setSize((float) scale, (float) scale);

        this.xd += random.nextFloat() * 0.15f;
        this.yd *= 0.4f / (random.nextFloat() * 0.9f + 0.1f);
        this.zd *= 0.4f / (random.nextFloat() * 0.9f + 0.1f);
        this.lifetime = (int) (200.0f + 20.0f / (random.nextFloat() * 0.9f + 0.1f));
        this.isCollided = false;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.lifetime-- <= 0) {
            this.remove();
        }

        if (!this.isCollided) {
            this.yd -= 0.04 * this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.98;
            this.yd *= 0.98;
            this.zd *= 0.98;
        } else {
            this.remove();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            EntityBloodFX particle = new EntityBloodFX(level, x, y, z, dx, dy, dz, dz);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}