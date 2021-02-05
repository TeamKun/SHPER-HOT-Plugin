package net.kunmc.lab.superhotplugin.event;

import net.kunmc.lab.superhotplugin.SuperHotPlugin;
import net.kunmc.lab.superhotplugin.helper.SuperHotPluginHelper;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SuperHotPluginConstantEvent extends BukkitRunnable {
	private final SuperHotPlugin plugin;
	public static KunMovementState kunMovementState = KunMovementState.Stopping;

	public SuperHotPluginConstantEvent(SuperHotPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		if (SuperHotPluginHelper.clockHolder != null) return;
		Player kun = plugin.getServer().getPlayer("Shojo_Virgim");
		if (kun != null) {
			World world = kun.getWorld();
			if (SuperHotPluginHelper.isKunMoving(kun) && world != null) {
				if (kun.isSprinting() && kunMovementState != KunMovementState.Running) {
					world.getEntities().stream()
						.filter(e -> !SuperHotPluginHelper.isKun(e))
						.forEach(SuperHotPluginHelper::accelerate);
					kunMovementState = KunMovementState.Running;
					//kun.sendMessage("時間の流れが加速した！");
				} else if (kun.isSneaking() && kunMovementState != KunMovementState.Sneaking) {
					world.getEntities().stream()
						.filter(e -> !SuperHotPluginHelper.isKun(e))
						.forEach(SuperHotPluginHelper::decelerate);
					kunMovementState = KunMovementState.Sneaking;
					//kun.sendMessage("時間の流れが減速した！");
				} else if (!kun.isSprinting() && !kun.isSneaking() && kunMovementState != KunMovementState.Walking) {
					world.getEntities().stream()
						.filter(e -> !SuperHotPluginHelper.isKun(e))
						.forEach(SuperHotPluginHelper::release);
					kunMovementState = KunMovementState.Walking;
					//kun.sendMessage("時間の流れが元通りになった！");
				}
			} else if (!SuperHotPluginHelper.isKunMoving(kun) && kunMovementState != KunMovementState.Stopping) {
				world.getEntities().stream()
					.filter(e -> !SuperHotPluginHelper.isKun(e))
					.forEach(SuperHotPluginHelper::freeze);
				kunMovementState = KunMovementState.Stopping;
				//kun.sendMessage("時間の流れが止まった！");
			}
		}
	}

	public enum KunMovementState {
		Running,
		Sneaking,
		Walking,
		Stopping
	}
}