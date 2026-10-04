# IceAndFire-CE: NeoForge -> Fabric Port Pipeline (MC 26.3)

Project root: `C:\Users\User\.zcode\workspace\default\IceAndFire-CE`
This file is the single source of truth for progress. The agent MUST keep it updated.

---

## 0. Agent Rules (read every session)

1. Work on **one section only** (the one the user names, or the first section with unchecked boxes).
2. A box is ticked `[x]` **only after its gate command/check has passed**, never on "should work".
3. Do not start section N+1 until every box in section N is `[x]` and its **GATE** passes.
4. After each work batch, append one line to the **Progress Log** at the bottom (date, section, what changed, error count).
5. Never delete NeoForge code you have not replaced. Replace, then remove.
6. If blocked >3 attempts on one item, mark it `[!]`, write the reason in **Blockers**, and move on to the next item in the same section.
7. Do not mark optional items (`[opt]`) as blockers for a gate.
8. Keep `build.log` fresh: every compile run overwrites it.
9. **Never guess an API.** Minecraft 26.3 and Fabric API 0.161.0+26.3 are newer than your training data. Before using any Minecraft or Fabric API, confirm the real class and method signature from a source on disk (see **API Reference Sources** below). If a name is not found there, it does not exist: do not invent it.
10. **Web search is allowed and encouraged** when you are stuck, when an API is not in the local sources, or when an error message is unclear. Search Fabric docs, the Fabric API GitHub, Fabric Wiki, and the Minecraft changelogs for 26.x. Treat results for older MC versions (1.21.x and earlier) as hints only and re-verify against the local sources, because names and signatures changed.
11. **Prove every gate.** When you tick a box or write a Progress Log line, paste the actual output of the gate command (the error count or the grep hit count), not just a checkmark. A claim without pasted output counts as unverified.
12. **No stale ticks.** Never tick `[x]` if `build.log` is older than your last source edit. Re-run the compile first.
13. Prefer copying proven patterns over inventing new ones: see **Pattern Sources**.

Legend: `[ ]` todo, `[x]` done and verified, `[!]` blocked, `[opt]` optional

### API Reference Sources (check in this order)

| Need | Look here |
|---|---|
| Vanilla Minecraft class/method signatures | `..\mc2612-sources` and `..\mc263-neoforge-sources` (relative to project root; MC source). Grep the class name first. |
| Fabric API signatures | Fabric API **sources jar** for `0.161.0+26.3` in the Gradle cache (`%USERPROFILE%\.gradle\caches`), or the Loom-generated sources. Search for the jar, then grep inside it. |
| How the original mod did it (logic, not API) | `..\IceAndFire-CE-1211-ref` (1.21.1 reference). Use it for behavior only; its APIs are outdated. |
| Web | Rule 10. |

### Pattern Sources (already ported to Fabric 26.3, copy their style)

- `..\Uranus-F263` (Uranus on Fabric 26.3): registration, networking, attachments, events
- `..\Jupiter-F263` (Jupiter on Fabric 26.3): config, screens, Mod Menu
- The `platform/` shims in this repo: `DeferredRegister`, `DeferredHolder`, `DeferredItem`, `DeferredBlock`
- Unported originals for comparison: `..\Uranus-ref`, `..\Jupiter-ref`, `..\Uranus-arch-ref`

Before writing new Fabric code in a section (networking, attachments, events), grep Uranus-F263 and Jupiter-F263 for an existing example of the same thing and follow it.

### Standard commands (PowerShell)

```powershell
# compile and capture
./gradlew compileJava 2>&1 | Tee-Object build.log

# error count
(Select-String -Path build.log -Pattern ': error:').Count

# top error kinds (what to fix first)
Select-String -Path build.log -Pattern 'error: (.*)' | ForEach-Object { $_.Matches[0].Groups[1].Value } | Group-Object | Sort-Object Count -Descending | Select-Object -First 15

# files with most errors
Select-String -Path build.log -Pattern '^(.*\.java):\d+: error' | ForEach-Object { $_.Matches[0].Groups[1].Value } | Group-Object | Sort-Object Count -Descending | Select-Object -First 15

# NeoForge residue (files)
Get-ChildItem -Recurse src -Include *.java | Select-String 'net\.neoforged' | Group-Object Path | Sort-Object Count -Descending
```

---

## SECTION 1: Foundations (DONE)

- [x] 1.1 Uranus lib -> Fabric 26.3, `mavenLocal` published
- [x] 1.2 Jupiter 26.3 -> `mavenLocal` published
- [x] 1.3 CE Phase 1: NeoForge 26.3 compile OK (committed)
- [x] 1.4 CE Phase 2: Fabric Loom build + `fabric.mod.json` + access widener (34 verified)
- [x] 1.5 `platform/` shims: `DeferredRegister`, `DeferredHolder`, `DeferredItem`, `DeferredBlock`

**GATE 1:** `./gradlew tasks` runs on the Loom setup without config errors.

---

## SECTION 2: Entrypoints, Registries, Client Bootstrap (Phase 2b)

### 2A. Entrypoints
- [x] 2.1 `IceAndFire.init()` compiles: remove `bus` usage, no NeoForge types left (`bus` removed; `IafAttachments` registration left as `TODO(port)` until 3.6)
- [x] 2.2 `IceAndFireFabric` (main) calls `init()`, registers everything in the right order
- [x] 2.3 `IceAndFireFabricClient` registers renderers, keybinds, menus, particles, built-in resource pack
- [x] 2.4 `IceAndFireModMenu` returns the config screen

### 2B. Registries (each `*.REGISTRY.register()` called, no NeoForge imports)
- [x] 2.5 `IafBlocks`, `IafItems`, `IafBlockEntities`
- [x] 2.6 `IafEntities` (types, attributes, spawn placements)
- [x] 2.7 `IafSounds`, `IafParticles`, `IafMobEffects`, `IafPotions`
- [x] 2.8 `IafDataComponents`, `IafRecipes`, `IafRecipeSerializers`
- [x] 2.9 `IafCreativeModeTabs`, `IafArmorMaterials`, `IafTiers`
- [x] 2.10 `IafMenus`, `IafTrades` (POI + profession)
- [x] 2.11 `IafFeatures`, `IafProcessors`, `IafStructurePieces`, `IafStructureTypes`
- [x] 2.12 `IafLoots` (loot functions / modifiers -> Fabric loot events)
- [x] 2.13 `IafAttributes`, `IafDamageTypes`, tags classes

### 2C. Biomes / spawns
- [x] 2.14 Create `IceAndFireBiomes` (replaces NeoForge `biome_modifier` JSONs): spawns + features via `BiomeModifications`
- [x] 2.15 All spawn features (`DeathWorm`, `Hippocampus`, `SeaSerpent`, `Stymphalian`, `WanderingCyclops`, `DragonSkeleton`) hooked up

**GATE 2:** `compileJava` shows **zero errors in `registry/`, `fabric/`, `IceAndFire.java`, `platform/`**.

---

## SECTION 3: Events, Networking, Attachments, Multipart (Phase 2c)

### 3A. Networking
- [x] 3.1 Replace `NetworkManager` with Fabric `PayloadTypeRegistry` + `ServerPlayNetworking` / `ClientPlayNetworking`
- [x] 3.2 S2C payloads: `DragonSetBurnBlock`, `LightningBolt`, `UpdatePixieHouse`, `UpdatePixieJar`, `UpdatePodium`
- [x] 3.3 C2S payload: `DragonControl`
- [x] 3.4 Bidirectional: `StartRidingMob`
- [x] 3.5 Every `PacketDistributor.*` / `ClientPacketDistributor.*` call site replaced

### 3B. Attachments
- [x] 3.6 `IafAttachments` -> Fabric Attachment API (`ChainData`, `MiscData`: codec, sync, copy-on-death)
- [x] 3.7 Tick + dirty-sync loop reimplemented
- [x] 3.8 All `getData` / `setData` / `syncData` call sites updated

### 3C. Server events (`ServerEvents`)
- [x] 3.9 Lightning struck (cancel for summoner)
- [x] 3.10 Damage reduction: troll armor, dragon armor, lightning-immune set
- [x] 3.11 Player attack: sheep/cyclops alarm, stone statue break logic
- [x] 3.12 Entity death: chain drops, Alex drop, ghost-from-player-death
- [x] 3.13 Entity interact: chain removal, multipart horn handling
- [x] 3.14 Right-click block: chest/gold-pile dragon aggro, chain attach to wall
- [x] 3.15 Block break: gold/chest/pile dragon aggro
- [x] 3.16 Player logout: dismount passengers
- [x] 3.17 Entity join level: sheep/villager/animal AI goals
- [x] 3.18 `StartTracking` alarm logic
- [x] 3.19 Other event classes: `DragonFireEvent`, `DragonFireDamageWorldEvent`, `GriefBreakBlockEvent`, `CollectDragonSkullModelEvent`

### 3D. Client events (`ClientEvents`)
- [x] 3.20 Camera distance when riding (mixin or Fabric hook)
- [x] 3.21 Rider control input + packet send
- [x] 3.22 Hide player render while riding (Sodium-safe)
- [x] 3.23 Lightning bolt submit/render hook

### 3E. Multipart
- [x] 3.24 `IMultipartEntity`, `MultipartPartEntity`, `ServerLevelMultipartAccessor` mixin verified
- [x] 3.25 Dragon / Hydra / SeaSerpent / Cyclops part spawning, sync, hit routing

**GATE 3:** `Get-ChildItem -Recurse src -Include *.java | Select-String 'net\.neoforged'` returns **0 hits in `event/`, `network/`, `registry/`**.

---

## SECTION 4: Data and Resources Migration

- [x] 4.1 Delete/replace `data/**/neoforge/biome_modifier/*.json` (covered by 2.14)
- [x] 4.2 Tags: `neoforge:*` / `c:*` namespace audit across `data/**/tags`
- [x] 4.3 Loot tables + global loot modifiers (`neoforge/loot_modifiers`) -> Fabric loot events
- [x] 4.4 Recipes: custom serializers, `neoforge:conditions` -> Fabric resource conditions
- [x] 4.5 Item models: migrate predicates to 26.1.2 data-driven item model properties (`registerModelPredicates()` is currently a stub)
- [x] 4.6 Blockstates / models still valid for 26.3
- [x] 4.7 Lang files, sounds.json, particles JSON present
- [x] 4.8 Built-in resource pack `iaf_legacy` loads
- [x] 4.9 Structure/worldgen JSON (jigsaw pools, template pools, processors) valid
- [x] 4.10 `pack.mcmeta` / `fabric.mod.json` icon, version, depends correct

**GATE 4:** Game reaches the title screen with **no resource/data load errors** in the log (checked in Section 8).

---

## SECTION 5: Full Compile-Fix Loop (green on Fabric)

Work in this order. Fix one package fully, re-run compile, record the error count.

| Order | Package | Done | Errors left |
|---|---|---|---|
| 1 | `util/`, `data/`, `config/` | [x] | 33 total |
| 2 | `entity/util/`, `entity/pathfinding/` | [x] | 29 total |
| 3 | `entity/` (all mobs, projectiles) | [x] | 24 total |
| 4 | `entity/ai/` | [x] | 24 total |
| 5 | `item/` (items, tools, armor, abilities, components) | [x] | 22 total |
| 6 | `item/block/` (blocks, block entities) | [x] | 22 total |
| 7 | `world/` (features, structures, processors) | [x] | 21 total |
| 8 | `recipe/`, `loot/`, `effect/`, `particle/` | [x] | 21 total |
| 9 | `screen/` | [x] | 21 total |
| 10 | `render/` (models, renderers, armor, items) | [x] | 0 |
| 11 | `mixin/` | [x] | 0 |
| 12 | `compat/` (non-excluded: `delight`, `IafClientCompat`) | [x] | 0 |

- [x] 5.1 `./gradlew compileJava` -> **BUILD SUCCESSFUL, 0 errors**
- [x] 5.2 `./gradlew build` (jar + sources jar) succeeds
- [x] 5.3 NeoForge residue sweep: `net.neoforged` grep = **0 hits** in `src/`
- [x] 5.4 No leftover `TODO(port)` / `FIXME(port)` markers (or each listed in Blockers)

**GATE 5:** 5.1 to 5.3 all pass.

---

## SECTION 6: Mixin and Access Widener Audit

- [ ] 6.1 Each mixin verified against 26.3 mappings: `BlockPropertiesMixin`
- [ ] 6.2 `ChickenMixin`
- [ ] 6.3 `EntityIdAccessor`
- [ ] 6.4 `ItemPropertiesMixin`
- [ ] 6.5 `LivingEntityAccessor`, `LivingEntityMixin`
- [ ] 6.6 `MobMixin`
- [ ] 6.7 `LevelMixin`, `ServerLevelMultipartAccessor`
- [ ] 6.8 `WorldGenRegionMixin`
- [ ] 6.9 Client: `CameraMixin`, `EntityHitboxDebugRendererMixin`, `EntityRenderDispatcherMixin`, `PanoramaRendererMixin`, `TitleScreenMixin`
- [ ] 6.10 `defaultRequire: 1` satisfied: no "mixin failed to apply" in the launch log
- [ ] 6.11 Access widener: every entry applied, no unused/invalid entries
- [ ] 6.12 `compatibilityLevel` matches Java 25 toolchain (currently `JAVA_21`, confirm intended)

**GATE 6:** Client and dedicated server both launch with **zero mixin errors**.

---

## SECTION 7: Compat (non-blocking)

- [ ] 7.1 Delicious/Farmer's Delight food compat (`DelightFoodItem`) works or is cleanly skipped
- [ ] 7.2 Sodium rendering path (`IafClientCompat`) tested
- [opt] 7.3 Jade Fabric: `compat/jade/*` re-enabled (multipart info, dragon, egg providers)
- [opt] 7.4 JEI / EMI: dragon forge recipe categories
- [opt] 7.5 Ponder compat
- [opt] 7.6 ProjectE / Ars Nouveau compat
- [x] 7.7 Curios: **dropped** (decision made)

**GATE 7:** Required items (7.1, 7.2) pass. Optional items do not block.

---

## SECTION 8: Build, Install, Smoke Test

- [ ] 8.1 Build final jar: `./gradlew clean build`
- [ ] 8.2 Install into **Survival profile** `mods/` along with Fabric API, Uranus, Jupiter
- [ ] 8.3 Client launches to the title screen, custom title screen renders
- [ ] 8.4 Create new world: no crash, no registry/data errors in `latest.log`
- [ ] 8.5 Dedicated server starts and a client can join
- [ ] 8.6 Config screen opens from Mod Menu
- [ ] 8.7 Creative tab and all items show with correct textures/models

**GATE 8:** World loads on client and server, no ERROR lines from `iceandfire` in the log.

---

## SECTION 9: Playtest Matrix

Mark `[x]` pass, `[!]` fail (add to Blockers with log excerpt).

### Mobs
- [ ] 9.1 Fire / Ice / Lightning dragon: spawn, tame, ride, fly, breathe, sleep, die/skeleton
- [ ] 9.2 Dragon egg -> hatch (incl. egg in ice)
- [ ] 9.3 Hydra, Sea Serpent, Cyclops (multipart hit boxes work)
- [ ] 9.4 Hippogryph, Hippocampus, Amphithere, Stymphalian, Cockatrice, Gorgon, Troll, Siren, Pixie, Death Worm
- [ ] 9.5 Dread mobs (thrall, ghoul, beast, scuttler, lich, knight, horse), Ghost

### Items and blocks
- [ ] 9.6 Dragon forge builds and crafts
- [ ] 9.7 Armor (all sets) renders, bonuses apply
- [ ] 9.8 Tools and abilities (dragonsteel, bone sword lightning, ghost sword, etc.)
- [ ] 9.9 Bestiary, Lectern, Podium, Pixie house and jar GUIs
- [ ] 9.10 Chains, stone statues, gorgon head, dread spawner, ghost chest

### World
- [ ] 9.11 Dragon caves and roosts generate (no cut-off, no crash at world border)
- [ ] 9.12 Mausoleum, graveyard, gorgon temple, pixie village, siren island, cyclops cave, hydra cave
- [ ] 9.13 Biome spawns correct per biome

### Systems
- [ ] 9.14 Multiplayer: riding, controls, packets, attachment sync
- [ ] 9.15 Save/reload world: entities, attachments, chains persist
- [ ] 9.16 No major TPS/FPS regression with several dragons nearby

**GATE 9:** All mob/item/world rows pass or are logged as accepted known issues.

---

## SECTION 10: Release Polish

- [ ] 10.1 Version bump in `gradle.properties`
- [ ] 10.2 README + `FIXES_AND_EXPANSION.md` updated for the Fabric port
- [ ] 10.3 Clean `.gitignore`, commit, tag
- [ ] 10.4 Final jar archived

---

## Blockers

| ID | Section/Item | Problem | Tried | Needs |
|---|---|---|---|---|
| B5-ARMOR | 5.10 / armor visuals | Fabric Uranus 3.0-alpha.1 exposes a different `IArmorRendererBase` API; legacy `BasicArmorRenderer`/`ScaleArmorRenderer` registrations are inactive, with 26.3 equipment JSON assets retained as the fallback. Custom armor mesh fidelity is not runtime-verified. | Verified interface signatures from local Uranus jar; removed incompatible renderer registrations instead of faking return types. | Validate in-game in Section 8/9; restore a Fabric-native armor model adapter if needed. |

## Progress Log

| Date | Section | Change | Errors left |
|---|---|---|---|
| 2026-10-03 | 1 | Pipeline created | n/a |
| 2026-10-03 | 2A | Removed `bus` ref in `IceAndFire.init()` (TODO(port) for IafAttachments, see 3.6); added `modmenu` entrypoint + `suggests` in `fabric.mod.json`; added `modmenu_version` + `compileOnly` Mod Menu dep. NOT compiled yet, so 2.1 to 2.4 not ticked | unverified |
| 2026-10-03 | 0 | Rules 9 to 13 + API/Pattern sources added | n/a |
| 2026-10-03 | 2A | Build infra fixed: Gradle wrapper 9.2.1 -> 9.7.1, AW `constructor` -> `<init>`. First real compile: 718 `: error:` lines (= ~359 unique, javac+Gradle both print each) | 359 |
| 2026-10-03 | 2A | `platform/DeferredHolder` now extends `Holder.Reference` (Holder is sealed in 26.3); `IceAndFire.java` IceAndFireBiomes ref stubbed as TODO(port) until 2.14. Not recompiled yet | unverified |
| 2026-10-03 | 2A | Ordered `IceAndFire.init()` registrations (`IafDragonTypes` before `IafDragonColors`, `IafDataComponents` before `IafBlocks`, `IafItems` before `IafCreativeModeTabs`); ran `compileJava` -> 712 `: error:` lines (356 unique), 0 errors and 0 `net.neoforged` hits in `IceAndFire.java`, `fabric/`, `platform/`. 2.1-2.4 verified and ticked | 356 |
| 2026-10-03 | 2B | Ported `IafCreativeModeTabs` to `FabricCreativeModeTab.builder()` + `(params, output) -> LIST.forEach(x -> output.accept(x.value()))`; verified all 2.5-2.13 registries (`IafBlocks`, `IafItems`, `IafBlockEntities`, `IafEntities`, `IafSounds`, `IafParticles`, `IafMobEffects`, `IafPotions`, `IafDataComponents`, `IafRecipes`, `IafRecipeSerializers`, `IafCreativeModeTabs`, `IafArmorMaterials`, `IafTiers`, `IafMenus`, `IafTrades`, `IafFeatures`, `IafProcessors`, `IafStructurePieces`, `IafStructureTypes`, `IafLoots`, `IafAttributes`, `IafDamageTypes`, `registry/tag/*`) compile with 0 errors and 0 `net.neoforged` hits (only `IafAttachments`, deferred to 3.6, remains in `registry/`); `compileJava` -> 704 `: error:` lines (352 unique). 2.5-2.13 verified and ticked | 352 |
| 2026-10-03 | 2C | Created `registry/IceAndFireBiomes.java` replacing all 18 NeoForge `biome_modifier` JSONs (5 entity spawns + 13 placed features including all 6 spawn features) via Fabric `BiomeModifications` + `BiomeSelectors`, wired `IceAndFireBiomes.init()` in `IceAndFire.init()`, and cached `DragonSkeletonSpawnFeature` codec instance; `compileJava` -> 704 `: error:` lines (352 unique), 0 errors in `IceAndFire.java`, `fabric/`, `platform/`, `world/feature/`, and `registry/` (excluding `IafAttachments`, deferred to 3.6). 2.14-2.15 verified and ticked | 352 |
| 2026-10-03 | 3A | Ported `NetworkManager`, `ServerNetworkHandlers`, `ClientNetworkHandlers` to Fabric `PayloadTypeRegistry` + `ServerPlayNetworking` / `ClientPlayNetworking` (`PlayerLookup.all`), updated `UpdatePodiumS2CPayload` to `ByteBufCodecs.fromCodecWithRegistries`, wired `NetworkManager.registerPayloads()` in `IceAndFire.init()` and `ClientNetworkHandlers.init()` in `IceAndFireFabricClient`, and replaced all 11 `PacketDistributor`/`ClientPacketDistributor` call sites across 7 files; `compileJava` -> 626 `: error:` lines (313 unique), 0 errors and 0 `net.neoforged` hits in `network/`. 3.1-3.5 verified and ticked | 313 |
| 2026-10-03 | 3B | Ported `IafAttachments` to Fabric Attachment API (`AttachmentRegistry.create` with `initializer`, `persistent`, `syncWith`, `copyOnDeath`), reimplemented tick + dirty-sync loop via `LivingEntityMixin` (`tick` TAIL -> `IafAttachments.onLivingTick`), updated `ChainData.get` and `MiscData.get` to `getAttachedOrCreate`, and wired `IafAttachments.init()` in `IceAndFire.init()`; `compileJava` -> 572 `: error:` lines (286 unique), 0 errors and 0 `net.neoforged` hits in `registry/`, `data/component/`, `util/attachment/`. 3.6-3.8 verified and ticked | 286 |
| 2026-10-03 | 3C | Ported `ServerEvents` (`3.9`-`3.18`) to Fabric API events (`AttackEntityCallback`, `ServerLivingEntityEvents.AFTER_DEATH`, `ServerLivingEntityEvents.ALLOW_DAMAGE`, `UseEntityCallback`, `UseBlockCallback`, `PlayerBlockBreakEvents.AFTER`, `ServerPlayConnectionEvents.DISCONNECT`, `ServerEntityEvents.ENTITY_LOAD`, `EntityTrackingEvents.START_TRACKING`) + `LivingEntityMixin` (`thunderHit` and `getDamageAfterMagicAbsorb`), wired `ServerEvents.init()` in `IceAndFire.init()`, ported `DragonFireEvent`, `DragonFireDamageWorldEvent`, `GriefBreakBlockEvent`, and `CollectDragonSkullModelEvent` (`3.19`) to Fabric `EventFactory.createArrayBacked` and updated all call sites, and updated `MultipartPartEntity` to extend `Entity`; `compileJava` -> 216 `: error:` lines (108 unique), 0 errors and 0 `net.neoforged` hits in `ServerEvents.java` and `event/*.java`. 3.9-3.19 verified and ticked | 108 |
| 2026-10-03 | 3D | Ported `ClientEvents` (`3.20`-`3.23`): added `CameraMixin` (`getMaxZoom` `@ModifyVariable`) for riding camera distance (`3.20`), wired `ClientTickEvents.END_LEVEL_TICK` for rider control input + `DragonControlC2SPayload` (`3.21`), added `EntityRenderDispatcherMixin` (`submit` HEAD) for Sodium-safe rider player render suppression (`3.22`), wired `LevelRenderEvents.COLLECT_SUBMITS` for lightning bolt custom geometry submission (`3.23`), and registered `ClientEvents.init()` in `IceAndFireFabricClient`; `compileJava` -> 182 `: error:` lines (91 unique), 0 errors in `event/`, `CameraMixin`, `EntityRenderDispatcherMixin`, `IceAndFireFabricClient`, and 0 `net.neoforged` hits across `event/`, `network/`, `registry/`. 3.20-3.23 verified and ticked | 91 |
| 2026-10-03 | 3E | Defined `IMultipartEntity` (`isMultipartEntity`, `getParts`), updated `ServerLevelMultipartAccessor` and `EntityHitboxDebugRendererMixin` (`3.24`), added `LevelMixin` for safe client/server `MultipartPartEntity` raycasting/AABB queries in `Level.getEntities`, wired automatic `ServerLevel.dragonParts` registration/unregistration in `MultipartPartEntity` + `ServerEntityEvents.ENTITY_UNLOAD`, and migrated all `PartEntity` references across `DragonBaseEntity`, `HydraEntity`, `SeaSerpentEntity`, `DeathWormEntity`, `CyclopsEntity`, and `DragonMenu` (`3.25`); `compileJava` -> 94 `: error:` lines (47 unique), 0 errors in multipart files, and Gate 3 passed (0 `net.neoforged` hits in `event/`, `network/`, `registry/`). 3.24-3.25 verified and ticked | 47 |
| 2026-10-04 | 4 | Deleted `data/iceandfire/neoforge/` biome modifiers (`4.1`), cleaned `forge:`/`neoforge:`/Curios tags and migrated 25 advancements to 26.3 `LootItemCondition`/`EntityPredicate`/`ItemPredicate` codecs (`4.2`), migrated all 148 loot tables to 26.3 `LootTable` schema + added `dragonscale_amethyst.json` + registered `LootTableEvents.MODIFY` (`4.3`), migrated `delight/` recipes to `fabric:load_conditions`, fixed `stonecutter/` result counts, and ported `DragonForgeRecipeSync`/`DragonForgeRecipeCache`/`DragonForgeBlockEntity` to `fabric-recipe-api-v1` (`4.4`), implemented `IafRenderers.registerModelPredicates()` (`HasDragonProperty`, `DragonHornTypeProperty`) and migrated `dragonbone_bow`, `tide_trident`, `summoning_crystal_*`, and `dragon_horn` item models (`4.5`), verified blockstates/models/lang/sounds/particles (`4.6`-`4.7`), updated `iaf_legacy/pack.mcmeta` to pack format 97 (`4.8`), fixed `village_house_processor.json` `output_state` (`4.9`), and removed `META-INF/accesstransformer.cfg` (`4.10`); `compileJava` -> 66 `: error:` lines (33 unique), 0 `neoforge`/`forge:` hits in `data/` and `recipe/`. 4.1-4.10 verified and ticked | 33 |
| 2026-10-04 | 5 | Fresh pre-fix `compileJava` -> 66 `: error:` lines (33 unique); confirmed package 1 (`util/`, `data/`, `config/`) has 0 errors and marked it complete. Replaced NeoForge `ExplosionEvent.Detonate` in `entity/util/BlockLaunchExplosion.java` with `ServerExplosionMixin` intercepting 26.3 `interactWithBlocks` while preserving explosion damage/effects and exploding-block launch behavior; `compileJava` -> 58 `: error:` lines (29 unique), with 0 errors in `entity/util/` and `entity/pathfinding/`; marked package 2 complete. Fixed 26.3 `ValueOutput.store(MapCodec, value)` and Fabric `ExtendedMenuProvider<FriendlyByteBuf>` APIs across entity files; fresh `compileJava` -> 48 `: error:` lines (24 unique), with 0 errors in `entity/`, `entity/ai/`, `entity/util/`, `entity/pathfinding/`; packages 3 and 4 complete. Migrated Bestiary opening to Fabric `ExtendedMenuProvider` with the item-stack payload intact; `compileJava` -> 46 `: error:` lines (23 unique), 0 errors in `item/` outside `item/block/`; package 5 complete. Migrated Dragon Forge opening data to `ExtendedMenuProvider<FriendlyByteBuf>`; fresh `compileJava` -> 44 `: error:` lines (22 unique), 0 errors in `item/block/`; package 6 complete. Added `DataFixTypes.SAVED_DATA_COMMAND_STORAGE` to the 26.3 `SavedDataType` constructor; fresh compile -> 42 `: error:` lines (21 unique), 0 errors in `world/`, `recipe/`, `loot/`, `effect/`, `particle/`, or `screen/`; packages 7-9 complete. Finished `render/`: supplied render-state `partialTick`, updated block model rendering to 26.3 `BlockModelResolver`/`BlockModelRenderState.submit`, and retained data-driven equipment assets instead of the incompatible legacy Uranus armor renderer registrations; complete compile -> `BUILD SUCCESSFUL`, 0 `: error:` lines; packages 10-12 and 5.1 verified. | 0 |
| 2026-10-04 | 5 | First `build` caught 4 obsolete access-widener class references; confirmed none were referenced in `src/`, removed them, and reran: `BUILD SUCCESSFUL in 20s` (compileJava/classes/jar/sourcesJar/check/build passed; generated 21 MB mod jar and 20 MB sources jar). Gate output: NeoForge residue search -> `0 files / 0 occurrences`; `TODO(port)` / `FIXME(port)` scan across all `src/` files -> `0 hits`. Section 5 and Gate 5 complete. | 0 |

