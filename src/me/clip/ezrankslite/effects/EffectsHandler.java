/* This file is a class of EZRanksLite
 * @author Ryan McCarthy
 * 
 * 
 * EZRanksLite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * 
 * EZRanksLite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package me.clip.ezrankslite.effects;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import me.clip.ezrankslite.EZRanksLite;

public class EffectsHandler {

	EZRanksLite plugin;

	public EffectsHandler(EZRanksLite instance) {
		plugin = instance;
	}
	
	private List<Color> colors = Arrays.asList(new Color[] { Color.AQUA,
			Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN,
			Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE,
			Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE,
			Color.YELLOW });

	private Color randomColor() {
		int r = new Random().nextInt(colors.size());
		return colors.get(r);
	}

	public void fireworks(Location loc) {
		Location l1 = new Location(loc.getWorld(), loc.getX(),
				(loc.getY() + 2), loc.getZ());
		Firework f1 = l1.getWorld().spawn(l1, Firework.class);
		FireworkMeta f1Meta = f1.getFireworkMeta();
		f1Meta.addEffect(FireworkEffect.builder().withFade(randomColor())
				.withColor(randomColor()).build());
		f1.setFireworkMeta(f1Meta);
		f1.detonate();

		Firework f2 = l1.getWorld().spawn(l1.add(1, 0, 0), Firework.class);
		FireworkMeta f2meta = f2.getFireworkMeta();
		f2meta.addEffect(FireworkEffect.builder().withFade(randomColor())
				.withColor(randomColor()).build());
		f2.setFireworkMeta(f2meta);
		f2.detonate();

		Firework f3 = l1.getWorld().spawn(l1.add(1, 1, 0), Firework.class);
		FireworkMeta f3meta = f3.getFireworkMeta();
		f3meta.addEffect(FireworkEffect.builder().withFade(randomColor())
				.withColor(randomColor()).build());
		f3.setFireworkMeta(f3meta);
		f3.detonate();

		Location l4 = new Location(loc.getWorld(), (loc.getX() - 1),
				(loc.getY() + 1), (loc.getZ()));
		Firework f4 = l4.getWorld().spawn(l4, Firework.class);
		FireworkMeta f4meta = f4.getFireworkMeta();
		f4meta.addEffect(FireworkEffect.builder().withFade(randomColor())
				.withColor(randomColor()).build());
		f4.setFireworkMeta(f4meta);
		f4.detonate();

		Location l5 = new Location(loc.getWorld(), (loc.getX()),
				(loc.getY() + 1), (loc.getZ() - 1));
		Firework f5 = l5.getWorld().spawn(l5, Firework.class);
		FireworkMeta f5meta = f5.getFireworkMeta();
		f5meta.addEffect(FireworkEffect.builder().withFade(randomColor())
				.withColor(randomColor()).build());
		f5.setFireworkMeta(f5meta);
		f5.detonate();
	}

	public void potionBreak(Location l) {
		l.getWorld().playEffect(l, Effect.POTION_BREAK, 0);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.POTION_BREAK, 0);
	}

	public void ender(Location l) {
		l.getWorld().playEffect(l, Effect.ENDER_SIGNAL, 0);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.ENDER_SIGNAL, 0);
	}
	
	public void flames(Location l) {
		l.getWorld().playEffect(l, Effect.MOBSPAWNER_FLAMES, 0);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.MOBSPAWNER_FLAMES, 0);
	}

	public void smoke(Location l) {
		l.getWorld().playEffect(l, Effect.SMOKE, 0);
		l.getWorld().playEffect(l, Effect.SMOKE, 1);
		l.getWorld().playEffect(l, Effect.SMOKE, 2);
		l.getWorld().playEffect(l, Effect.SMOKE, 3);
		l.getWorld().playEffect(l, Effect.SMOKE, 4);
		l.getWorld().playEffect(l, Effect.SMOKE, 5);
		l.getWorld().playEffect(l, Effect.SMOKE, 6);
		l.getWorld().playEffect(l, Effect.SMOKE, 7);
		l.getWorld().playEffect(l, Effect.SMOKE, 8);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.SMOKE, 0);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.SMOKE, 1);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.SMOKE, 2);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.SMOKE, 3);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.SMOKE, 4);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.SMOKE, 5);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.SMOKE, 6);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.SMOKE, 7);
		l.getWorld().playEffect(l.add(0, 1, 0), Effect.SMOKE, 8);
	}

	public void explosion(Location l) {
		l.getWorld().createExplosion(l, 0);
	}

}