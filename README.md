# 🎉 ConfettiExplosions  
Turn destructive explosions into *flashy colors!*  
Replace some of the explosions of the game with vibrant bursts of confetti — still dangerous to players, but harmless to your world.

---

![creeperexploding](https://github.com/user-attachments/assets/ba7db994-14b4-490f-a208-186f8d4a6d02)
![multipleminecartexplosions](https://github.com/user-attachments/assets/ef50ed8c-632a-4ed6-9b89-31a92b10aa72)
![ghastsattack](https://github.com/user-attachments/assets/b140992e-eff5-4a79-bb19-ac07716e6fd7)

---

## 💡 What It Does

ConfettiExplosions is a lightweight Bukkit/Spigot plugin that **prevents block damage** from explosions while keeping the fun (and danger) alive!  
When Creepers, TNT, Fireballs, or Ender Crystals go boom — they’ll unleash **bursts of colorful particles** instead of destroying terrain.

Your buildings stays safe, and your players get fireworks every time something explodes!

---

## 🧨 Features

- 💥 **Explosion Protection:** No more terrain griefing from Explosions.
- 🌈 **Confetti Bursts:** Replace explosions with colorful randomized particle effects.
- ⚔️ **Damage Still Applies:** Explosions still harm nearby entities, keeping gameplay balanced.
- ⚙️ **Configurable:** Toggle specific explosion types in the config.
- 🔄 **Reload Command:** Update settings instantly without restarting.

---

## ⚙️ Installation

1. Download the **ConfettiExplosions** plugin `.jar`.  
2. Drop it into your server’s `plugins` folder.  
3. Start (or restart) your server.  
4. Enjoy safe, fabulous explosions!

---

## 💬 Commands

| Command | Description | Permission |
|----------|-------------|-------------|
| `/confettireload` | Reloads the plugin configuration | `confetti.reload` |

---

## 🧱 Configuration

After running the plugin once, a `config.yml` file will appear.  
You can toggle which explosions are replaced with confetti:

```yaml
# Default config.yml
explosions:
  creeper: true
  tnt: true
  tnt-minecart: true
  fireball: true
  ender-crystal: true
