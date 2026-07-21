# Ultimate Bosses

Addon for **Ultimate Ages** — Minecraft 1.21.1 / NeoForge 21.1.235.

## 📦 Build

```
./gradlew build
```
Output jar lands in `build/libs/`. Run `./gradlew runClient` / `runServer` to test locally.

Dependencies pulled automatically: GeckoLib 4.4.9 (required), JEI (optional, compile-only for now), Curios (optional, compile-only). Add the GeckoLib/JEI/CurseMaven repos to your own environment if Gradle can't resolve them from this sandbox's restricted network.

## ✅ Implemented (Phase 1)

- **Project scaffold**: Gradle/NeoForge 1.21.1 MDK, `neoforge.mods.toml`, config system covering every option requested (damage, health, effects/drops, spawn chances, Blood Moon, equipment bonuses, ritual, commands).
- **Boss framework** (`entity/boss/AbstractUBBoss`): shared base for all bosses, player-model rendering hook (`UBBossRenderer` + `getSkinLocation()`), GeckoLib animation controllers (idle/walk/attack) wired up for Fresh-Animations-style pose blending.
- **Shared AI kit** (`entity/boss/ai`): `WardenSonicBoomGoal`, `EvokerFangsAttackGoal`, `IllusionerCloneGoal` — reused by Cult Owner and all 7 community bosses; Cult Lovato uses sonic boom only, per spec.
- **All named bosses**: Chill, Miguel, Pikachu Empresario, Cult Lovato, Cult Owner, Herobrine (5 variants), and all 7 community bosses (SrNadien, JoanFo, Goten, Troe, Mushashi, ArrivedBog, Matheu) with configurable damage/health/effects/drops/death messages.
- **Herobrine altar ritual** (`world/HerobrineAltarDetector`): gold-block ring + netherrack + redstone-torch corners + fire, right-click with the configured summon item (default Cursed Diamond) → lightning + random variant spawn. Fully config-gated.
- **Cult boss natural spawns** (`world/CultIglooSpawnHandler`): igloo detection in Cherry Grove (Cult Lovato) / Swamp (Cult Owner) with configurable roll chance.
- **Illusioner natural spawns** (`world/IllusionerNaturalSpawnHandler`): configurable chance, any Overworld biome, night-only.
- **Shulker renewability** (`world/ShulkerRenewalHandler`): Shulker Shell + Endermite → Shulker.
- **Blood Moon** (`event/BloodMoonHandler`): every N nights (default 5, configurable), +30% (configurable) to health/damage/speed/armor for all hostile mobs, each stat individually toggleable.
- **Equipment bonuses** (`event/EquipmentBonusHandler`): any Netherite gear → Speed IV + Haste IV; any Stone gear → Slowness IV + Health Boost, both toggleable/tunable, refreshed continuously so they vanish the instant gear is removed.
- **Commands** (`command/UBossCommand`): `/uboss player tpboss`, `/uboss player tpddim <dimension>`, `/uboss spawn <boss>` (tab-completed ids), permission level configurable (default 4).
- **Nether-in-Overworld structures**: `data/ultimatebosses/worldgen/structure_set/nether_in_overworld.json` places Fortress/Bastion/Nether Fossil pieces in the Overworld via `random_spread` at a very wide spacing to approximate extreme rarity.
- **Infected mobs** (`world/InfectedMobsHandler`): minimal version — small chance on natural spawn for Villager/Animal/Wolf/Bat to turn hostile + glow (stand-in for white eyes) — see Phase 2 notes below.

## 🚧 Phase 2 (not yet implemented, scaffolding/notes only)

- **Herobrine variant AI depth**: Mage teleport-on-target and Builder structure/sign generation are stubbed; only base goals + one signature move per variant are wired.
- **Infected mob visuals**: needs a real white-eye overlay render layer per species instead of the glow-tag stand-in; hostile behaviour should be a persistent Goal, not re-rolled on entity join.
- **Survivor bases + Herobrine-relic trading NPCs**: not started.
- **JEI category** (bosses/rituals/drops/biomes/structures, Herobrine altar diagram): dependency wired as compile-only; category/recipe classes not written yet.
- **Auto-generated wiki, CurseForge summary/WYSIWYG description, changelog, install guide, dev guide, "add a new boss" guide, config/command reference**: none generated yet — this README covers install/build/config/commands as an interim reference.
- **Exact 0.01% Nether-structure-in-Overworld probability**: the current `structure_set` uses spacing/separation (vanilla's only rarity knob for structure sets) rather than a literal runtime percentage; the `netherStructureOverworldChancePercent` config value is reserved for a future code-driven placement pass if exact-percentage behaviour is required.

## 🗂️ Skins

Drop your `.png` skins into `src/main/resources/skins/` using the exact ids below (placeholders already reserve every slot):
`chill`, `miguel`, `pikachu_empresario`, `cult_lovato`, `cult_owner`, `herobrine`, `herobrine_warrior`, `herobrine_mage`, `herobrine_builder`, `herobrine_spy`, `herobrine_stalker`, `srnadien`, `joanfo`, `goten`, `troe`, `mushashi`, `arrivedbog`, `matheu`.

## ⚙️ Config

`config/ultimatebosses-common.toml` (generated on first run) — sections: `boss_damage`, `boss_health`, `spawn_chances`, `blood_moon`, `equipment_bonuses`, `rituals`, `commands`, `drops`.

## 🎮 Commands

```
/uboss player tpboss                 # teleport to nearest Ultimate Bosses boss
/uboss player tpddim <dimension>     # teleport to a dimension
/uboss spawn <boss>                  # spawn a boss by id (tab-completed)
```
Permission level: 4 (configurable via `commands.permissionLevel`).
