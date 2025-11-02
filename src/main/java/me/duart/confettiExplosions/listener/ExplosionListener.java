package me.duart.confettiExplosions.listener;

import me.duart.confettiExplosions.ConfettiExplosions;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

import static java.awt.Color.HSBtoRGB;

public class ExplosionListener implements Listener {

    boolean isCharged = false;
    private final ConfettiExplosions plugin;

    private static final Particle[] CONFETTI = {
            Particle.FIREWORKS_SPARK,
            Particle.REDSTONE,
            Particle.DUST_COLOR_TRANSITION
    };

    public ExplosionListener(ConfettiExplosions plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onExplosion(@Nonnull EntityExplodeEvent event) {
        try {
            EntityType type = event.getEntityType();
            if (type != EntityType.CREEPER
                    && type != EntityType.PRIMED_TNT
                    && type != EntityType.MINECART_TNT
                    && type != EntityType.FIREBALL
                    && type != EntityType.ENDER_CRYSTAL) return;

            if (!isEnabled(type)) return;

            event.blockList().clear();
            event.setYield(0.0F);
            event.setCancelled(true);

            Location center = event.getLocation();
            World world = center.getWorld();
            if (world == null) return;

            float power;
            double damage;

            switch (type) {
                case CREEPER -> {
                    Creeper creeper = (Creeper) event.getEntity();
                    isCharged = creeper.isPowered();
                    power = isCharged ? 6.0F : 3.0f;
                    damage = creeperDamage(creeper);
                }
                case PRIMED_TNT, MINECART_TNT -> {
                    power = 4.0F;
                    damage = 28.0;
                }
                case FIREBALL -> {          // ghast projectile and /summon fireball
                    Explosive explosive = (Explosive) event.getEntity();
                    power = explosive.getYield();
                    damage = difficultyExplosionDamage((Fireball) explosive);
                    isCharged = power >= 3; // visual threshold
                }
                case ENDER_CRYSTAL -> {
                    isCharged = true;
                    power = 6.0F;
                    damage = 127.5;
                }
                default -> {
                    power = 3.0F;
                    damage = 0;
                }
            }

            double radius = power * 2.0D;

            for (Entity entity : world.getNearbyEntities(center, radius, radius, radius)) {
                if (!(entity instanceof LivingEntity livingEntity) || livingEntity.isDead()) continue;

                double distance = livingEntity.getLocation().distance(center);
                if (distance <= radius) {
                    double distanceFactor = 1.0D - (distance / radius);
                    double finalDamage = damage * Math.max(0.5, distanceFactor);

                    livingEntity.damage(finalDamage, event.getEntity());
                }
            }

            confettiBurst(center);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error handling explosion event", e);
        }
    }

    private void confettiBurst(@Nonnull Location origin) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        World world = origin.getWorld();
        if (world == null) return;

        final int particles = isCharged ? 500 : 350;
        final float speed = isCharged ? 1.8f : 1.3f;
        final double spreadMin = isCharged ? 4.0 : 3.0;
        final double spreadMax = isCharged ? 8.0 : 6.0;

        for (int i = 0; i < particles; i++) {
            double spread = random.nextDouble(spreadMin, spreadMax);
            Location spawn = origin.clone().add(
                    random.nextDouble(-spread, spread),
                    random.nextDouble(-spread, spread),
                    random.nextDouble(-spread, spread));

            Particle type = CONFETTI[random.nextInt(CONFETTI.length)];

            Color color = Color.fromRGB(HSBtoRGB(random.nextFloat(), 0.8f
                            + random.nextFloat()
                            * 0.2f, 0.9f
                            + random.nextFloat()
                            * 0.1f) & 0xFFFFFF);


            if (type == Particle.REDSTONE) {
                world.spawnParticle(type, spawn, 0, 0, 0, 0, 0,
                        new Particle.DustOptions(color, 1.2f));

            } else if (type == Particle.DUST_COLOR_TRANSITION ) {
                Color fade = Color.fromRGB(random.nextInt(1 << 24));
                world.spawnParticle(type, spawn, 0, 0, 0, 0, 0,
                        new Particle.DustTransition(color, fade, 1.2f));

            } else {
                Vector direction = spawn.toVector().subtract(origin.toVector()).normalize();

                direction.setY(direction.getY() + 0.5);
                direction.normalize().multiply(speed);

                world.spawnParticle(type, spawn, 0,
                        direction.getX(), direction.getY(), direction.getZ(), 1);
            }
        }

        world.playSound(origin, Sound.ENTITY_GENERIC_EXPLODE, 2.2f, 1f);
    }

    private boolean isEnabled(@Nonnull EntityType type) {
        return switch (type) {
            case CREEPER -> plugin.creepersEnabled;
            case PRIMED_TNT -> plugin.tntEnabled;
            case MINECART_TNT -> plugin.tntMinecartEnabled;
            case FIREBALL -> plugin.fireballEnabled;
            case ENDER_CRYSTAL -> plugin.enderCrystalEnabled;
            default -> false;
        };
    }

    private static double creeperDamage(@Nonnull Creeper creeper) {
        return switch (creeper.getWorld().getDifficulty()) {
            case PEACEFUL -> 0.0;
            case EASY -> creeper.isPowered() ? 43.5 : 22.5;
            case NORMAL -> creeper.isPowered() ? 85.0 : 43.0;
            case HARD -> creeper.isPowered() ? 127.5 : 64.5;
        };
    }

    private double difficultyExplosionDamage(@Nonnull Fireball fireball) {
        return switch (fireball.getWorld().getDifficulty()) {
            case PEACEFUL -> 0.0;
            case EASY -> 4.25;
            case NORMAL -> 7.5;
            case HARD -> 11.25;
        };
    }
}
