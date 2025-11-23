# 🟩 **Red Light, Green Light – Minigame Plugin**

A fully featured **“Red Light, Green Light”** minigame inspired by *Squid Game*.  
Perfect for fun events, minigame servers, or themed competitions.

---

## 🎮 **How the Game Works**

- When the round starts, all players must **run during Green Light**.
- When **Red Light** appears, moving can cause instant elimination.
- The goal is to reach the **finish line** before the timer runs out.
- Optional **Guard Mode**: selected players receive an *OP Bow* that eliminates in one shot.
- Visual effects, sounds, and titles enhance the experience.

---

## ✨ **Main Features**

- ✔️ Full game state system: STARTING → PLAYING → RESET
- ✔️ Green Light / Red Light movement control
- ✔️ OP Bow for guards (one-shot kill)
- ✔️ Automatic barrier creation & removal at game start
- ✔️ Titles, sounds, actionbar messages
- ✔️ Fireworks at the finish line for players who complete the course
- ✔️ Simple and clear configuration through `config.yml`

---

## 🔧 **Commands**

### `/guardas`
Adds or removes players from the Guard list.  
Use before the game starts.

---

## 📦 **Installation**

1. Download the `.jar` file
2. Place it inside the plugins folder:
   ```
   /plugins/
   ```
3. Restart the server
4. Set the wall positions (start barrier) in the configuration file
5. Start a game and have fun 🎉

---

## 🗂️ **Configuration**

Inside `config.yml`:

```yaml
positions:
  walls:
   - "pos1"
   - "pos2"
```

---

## 👑 **Requirements**

- **Spigot / Paper 1.21.1+**
- **WorldEdit** (used for building/removing the starting wall)

---


## 📸 **Showcase Video**

https://youtu.be/NgQfSgLgVmA

