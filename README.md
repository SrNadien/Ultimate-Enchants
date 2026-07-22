# Ultimate Enchants

**Ultimate Enchants** expands Minecraft's enchanting system with a carefully balanced collection of powerful, configurable enchantments designed to enhance combat, exploration, mining, survival, and progression while staying true to the vanilla experience.

Every enchantment is designed to feel like a natural extension of Minecraft, giving players new ways to customize their gear without overwhelming the game's progression.

## Features

* **30+ unique enchantments** for every major equipment type.
* Fully configurable through separate **client** and **server** configuration files.
* Multiplayer-friendly and highly customizable.
* Optional improvements to several vanilla enchantments.
* Designed for **NeoForge 1.21.1**.

---

# Enchantments

## Universal

### Soulbound

Keeps the enchanted item in the player's inventory after death instead of dropping it.

---

## Armor

### Magic Protection

Reduces damage taken from magical attacks such as potions, magic projectiles, and other magical sources.

---

## Chestplate

### Displacement

Has a chance to randomly teleport nearby attackers away after they hit you.

### Flaming Rebuke

Knocks attackers back while setting them on fire.

### Chilling Rebuke

Knocks attackers back while slowing them and applying Weakness.

### Reach

Increases the distance from which the wearer can interact with blocks and entities.

### Vitality

Increases the wearer's maximum health.

---

## Helmet

### Air Affinity

Removes the mining speed penalty while swimming or not standing on solid ground.

### Gourmand

Makes food restore more hunger and saturation.

### Insight

Increases the amount of experience gained from most sources while wearing the helmet.

---

## Weapons

### Cavalier

Deals increased damage while riding mounts such as horses.

### Ender Disruption

Deals bonus damage to Endermen and other End-type mobs while reducing their ability to teleport.

### Frost Aspect

Applies Slowness and Weakness to enemies with every successful hit.

### Leech

Restores health whenever you kill a hostile or passive mob.

### Instigating

Deals double damage to enemies that are at full health.

### Magic Edge

Converts part of your weapon's damage into magic damage while slightly increasing total damage.

### Outlaw

Deals increased damage to Villagers and Iron Golems.

### Vigilante

Deals increased damage to Illagers and Ravagers.

### Vorpal

Has a chance to perform devastating critical strikes and can occasionally decapitate certain mobs.

---

## Tools

### Excavating

Mines additional blocks around the targeted block, allowing faster excavation of large areas.

---

## Bows

### Hunter's Bounty

Animals killed with the bow have a chance to drop additional loot.

### Quick Draw

Reduces the time required to fully charge a bow.

### Trueshot

Increases arrow velocity, accuracy, and allows arrows to pierce through targets.

### Volley

Fires multiple arrows with each shot while consuming only a single arrow.

---

## Fishing Rods

### Angler's Bounty

Provides a chance to catch additional items while fishing.

### Pilfering

Can steal armor from hooked mobs or players.

---

## Shields

### Bulwark

Prevents the wielder from being knocked back while blocking.

### Phalanx

Allows faster movement while actively using a shield.

---

## Curses

### Curse of Foolishness

Prevents the player from gaining experience while using the cursed item.

### Curse of Mercy

Prevents weapons from dealing the final lethal hit, always leaving targets with at least a small amount of health.

---

# Vanilla Enchantment Overrides

Ultimate Enchants can optionally improve several vanilla enchantments by expanding their compatibility and functionality.

### Expanded Compatibility

Many vanilla enchantments can be applied to additional equipment. For example:

* Protection enchantments can be applied to Horse Armor.
* Many sword-exclusive enchantments can also be used on Axes.

### Preservation (Optional Mending Replacement)

When enabled, **Mending** is replaced with **Preservation**.

Instead of repairing items automatically with experience, items can be repaired indefinitely in an Anvil without increasing repair costs. This removes the anvil penalty while keeping item maintenance balanced through manual repairs.

### Enhanced Frost Walker

When enabled, **Frost Walker** also functions on Horse Armor.

An optional configuration allows Frost Walker to solidify lava into **Glossed Magma**, allowing both players and horses to safely cross lava lakes.

---

# Configuration

* **Client:** `config/ultimateenchants-client.toml`
* **Server:** `config/ultimateenchants-server.toml`

Every enchantment and override can be individually configured, making Ultimate Enchants suitable for vanilla-style survival, multiplayer servers, and large modpacks alike.
