># Disclaimer
>Some of the information contained in this guide may not be accurate (specifically anything that is marked in red). If you are able to confirm the accuracy of these bits of information, please inform a Trusted+ user on the r/battlecats Discord.

# General

## Meatshield

A cat unit that is meant to hinder enemy progression through spammability (ex. Macho and Wall Cat)

**Also known as:** MS

## Peon

Refers to weak enemies such as Snache and Those Guys, but can also be used to refer to stronger, non-boss enemies, such as Angelic Gory or Shadow Boxer K.

## Gacha Unit

A unit that is obtained from a Rare Cat Capsule.

## Plus Level

The number of gacha duplicates that have been used on a cat.

**Also known as:** + level

## Hypermaxing

Raising a cat unit to level 40 through the use of catseyes.

## Ultramaxing

Raising a cat unit to level 50 through the use of catseyes.

## Boosting

Raising a cat unit to a very high level through the use of plus levels.  
*This term is frequently used interchangeably with hyper/ultramaxing.

## Cat Unit Rarities

-   **N:** Normal
-   **EX:** Special
-   **R:** Rare
-   **SR:** Super Rare
-   **UR:** Uber Super Rare
-   **LR:** Legend Rare

## Advent

A stage of Deadly difficulty where the player must fight a unique boss to obtain a Rare/Super Rare unit, called advent drop or advent cat. Examples include Realm of Carnage, No Plan A and Parade of the Dead.

## Units

### Distance Units (DU)

Distance on the battlefield.

**Also known as:** pixels (misleading), range units

### Hit Points (HP)

Self explanatory. Every entity on the battlefield has hit points and dies when 0 is reached. When one of the bases reaches 0 HP, you either lose or win and the stage is over.

### Frame

1/30th of a second. Battle Cats runs at 30 frames per second, also referred to as 30 fps. This is the smallest time interval possible, and is used when referring to precise timings, such as unit or enemy attack animations.

# Basic Stats

## Movement Speed

Movement speed towards the opposing base when unobstructed in 2 DU/f. Can be 0 for enemy units in some rare cases.

## Damage

HP taken from the target on a given attack occurrence.

## Single Target

Mutually exclusive to Area Attack, the unit only hits the closest enemy in range when attacking.

## Area Attack

Mutually exclusive to Single Target, the unit hits every enemy in range when attacking.

## Trait

A (usually visually represented) trait of an enemy that can give them certain abilities and makes them vulnerable to trait-targeting abilities. Reserved ONLY to enemies, cat units cannot have traits.

**Also known as:** color, type

## Ability Types

Read the section on actual abilities [here](#abilities).

### Passive Targeting Abilities

Abilities the unit applies when it is affected by an attack from a certain enemy trait (e.g. [Resistant Against](#resistant), [Strong Against](#strongagainst)), merely by existing. Exclusive to cat units.

### Passive Universal Abilities

Abilities the unit applies when it is affected by an attack from any type of unit (e.g. any [immunities](#immunity), [Metal trait](#metal)), merely by existing.

### Active Targeting Abilities

Abilities the unit only applies when it hits an attack against a target trait (e.g. [Massive Damage](#massivedamage), [Slow](#slow)).

### Active Universal Abilities

Abilities the unit only applies when it hits an attack against any enemy (e.g [wave](#wave), [Critical hit](#criticalhit), [Barrier Breaker](#barrierbreaker), [ZKill](#zombiekiller), [double cash](#doublecash)).

### Status Effects

Temporary effects applied to an enemy by a unit's active abilities.

**Also known as:** proc (**P**rogrammed **R**andom **OC**curence)

### Crowd Control

Used to refer to units that inflict a status effect (crowd control units, or also sometimes CC units).

### Damage Knockback (Hitback)

Large backwards [displacement](#displacement) over 345 DU lasting (how many?) f that occurs when a unit takes a specified threshold of damage. Temporarily grants invincibility and removes the unit from the battlefield, allowing fast units to run past it. Every unit has a number greater or equal to 1, 1 being the minimum as every unit gets knocked back upon death. Their distribution is linear, meaning that every KB occurs at the same interval (5,000 HP and 5 KBs results in 1 KB every 1,000 damage taken)

### Endurance

Damage taken by a specific unit at a specific level before KB occurs (5,000 HP and 5 KB results in 1,000 Endurance)

**Also known as:** resilience, knockback resistance

### Standing Range

The distance in DU towards an enemy at which a unit stops moving and starts attacking .

**Also known as:** range

### Damage Per Second (DPS)

The average damage per second a unit deals against a stationary enemy when uninterrupted.  
Calculated using: `Base damage / (Attack Frequency / 30)`

# Advanced Stats

## Base Damage

The damage the unit deals on a given attack hit without [abilities](#abilities).

**Also known as:** raw damage

## Base HP

The HP of a given unit against any enemy, ignoring abilities.

## Effective Damage

Damage against a specified target with respect to active abilities.

## Effective HP

HP against a specified target with respect to passive abilities.

## Effective DPS

DPS with respect to abilities. Effective DPS can be influenced by Trait targeting abilities (e.g. Massive Damage, Strong Against, etc.) or general abilities (e.g. Critical Hit, Savage Blow, Wave, Surge). Effective DPS can also be influenced by accuracy (amount of attacks that actually hit their target), although accuracy can only be estimated and never reliably measured.

## Range

### Long Range / Long Distance / LD

Units with this ability can attack farther than their [standing range](#standingrange) (with some exceptions), but have a [blind spot](#blindspot) in their range to compensate. For a more thorough explanation of the LD mechanic (as well as omni-strike), see [this video](https://youtu.be/Z-osLFU6G58).

See also [Piercing Range](#piercingrange), [Inner Range](#innerrange).

### Omni-Strike

Units with this ability can hit enemies behind them as well as in front. Sometimes it also comes with [piercing range](#piercingrange), in which case it essentially functions as Long Range without the blind spot.

### Maximum Range

Maximum distance at which an attack can hit. Usually equal to [standing range](#standingrange). If a unit has LD or Omni-strike this often exceeds standing range (e.g. Aphrodite, Cat God), but is never lower (e.g. Hacker Cat, Ragnarok).

**Also known as:** reach

Not to be confused with [piercing range](#piercingrange).

### Minimum Range

Minimum distance at which an attack can hit. Usually -320, accounting for the hitbox of the unit. If the unit has LD, it can be above that (in some cases above even the standing range, e.g. Golfer Cat) creating a blind spot. If the unit has Omni-strike, it is usually ([but not always](https://cdn.discordapp.com/attachments/678400499522732051/790010475998871562/video0.mov)) lower than -320, hitting behind it.

See also [inner range](#innerrange).

### Blind Spot

Area which an LD unit cannot hit with its regular attack.

### Base Targeting Range

_(still got no good name, thought about calling it demolition range but that may also be confusing, so something along those lines because base is also confusing)_

The distance at which a unit stops to attack the enemy base. Usually this is equal to [Standing Range](#standingrange). For LD units, this is equal to [minimum range](#minimumrange), for Omni-strike Units, this is equal to [maximum range](#maximumrange).

### Piercing Range

How far ahead of itself a unit can hit relative to its [standing range](#standingrange). Can exceed the standing range (Aphro) or be equal / zero (Hacker) but is never less. It is obtained by subtracting the unit’s standing range from its maximum reach.  
Here’s an example: Radiant Aphrodite has a standing range of 600, and hits enemy units in range 450~850 in front of her, meaning she has a piercing range of **250** (850 – 600) and an [Inner Range](#innerrange) of **150** (600 – 450).

Not to be confused with [Piercing Attack](#piercingattack).

### Inner Range

How close to itself an LD unit can hit enemies (can be zero, or lower, like golfer). It is obtained by subtracting the unit’s minimum reach from its standing range. See the above explanation for an example.

See also [Piercing Range](#piercingrange), [Long Range](#longrange), [minimum range](#minimumrange).

### Piercing Attack

Used to refer to units that can hit farther than their standing range, through the use of either Long Range attacks, Omni-strike, Wave attacks, or Surge attacks.

Not to be confused with [piercing range](#piercingrange).

### Stepping Stone

A frontline enemy that is used as a target by cat units with piercing attack to allow them to hit the enemy backline.

### Frontline

Enemies/Cats that are at the front of the battle.

### Backline

Enemies that have a high standing range and stay behind the frontline; usually used to punish the player should they push the enemy frontline too far back. The term Backliner is also commonly used to refer to these enemies, and also more rarely used to refer to cat units that have a high standing range.

## Attack Timings

### Foreswing

Animation of an attack before the hit occurs. Can be instant (e.g. Maglev Cat) or extremely long (e.g. Crazed Bahamut, Enemy Filibuster). If the foreswing animation is interrupted by means of [displacement](#displacement), the unit will start its foreswing again as soon as it finds a target within range.

### Backswing

The part of the animation that comes after the hit. Can be very short or extremely long (e.g. Awakened Bahamut, Bullet Train, Green Shell).

**Note:** while this is sometimes referred to as "rebound", the word rebound itself is also used to designate something else, that being a full-backswing unit (such as Maglev or Yukimura) being interrupted during its backswing and attacking again immediately.

### Attack Cooldown

Time between finishing a foreswing animation (= landing an attack) and starting the next one. Immediately starts counting down after an attack lands and stops the unit from attacking until finished. Allows for walking towards the enemies should they get pushed back, otherwise, the unit will display an idle animation. If the unit’s backswing is longer than the attack cooldown, the unit will immediately start attacking again after the backswing finishes. If the backswing is interrupted and the attack cd is close to 0f, the unit can attack much faster than its attack frequency (e.g. A.Bahamut vs Sleipnir) possibly leading to drastically increased DPS. Does not ever stop counting down even while the unit is [displaced](#displacement) or [frozen](#freeze).

Not to be confused with Time Between Attacks.

### Attack Frequency

Time between the frame on which the attack occurs and the frame on which the next attack occurs. Used to calculate theoretical [DPS](#damageperseconddps).

**Also known as:** attack interval

# Advanced Mechanics

## Clipping

Units moving into enemy units because they are currently being [displaced](#displacement). Can be beneficial or devastating depending on the situation. Often happens without consequences with slow units, e.g. Meatshields after using the cat cannon walking a bit into the enemy. Fast units can however cover large distances in a short time and sometimes intentionally or unintentionally completely pass some units and end up behind them.

## Speed Clipping

Units moving up a tiny distance farther than they should because of the way movement speed is calculated. Is more noticeable on fast units, as they cover larger intervals of distance in one frame, and only detect collisions after moving, enabling them to clip up to half their movement speed into the enemy in DU. Can cause odd interactions between units with very similar but not equal ranges hitting each other when they shouldn't in theory, e.g. Li’l Nyandam and Camelle Variants.

## Boss

Bosses are enemy units that cause a boss knockback when leaving the enemy base and cannot be moved behind the enemy base by any means. They can, however, spawn far back enough for non-[piercing](#piercingrange) units to hit the base so long as the boss never has the chance to move ahead on the field.

## Boss Knockback

Very large backwards [displacement](#displacement) stretching (how many?) DU over 47f that occurs one frame after a stage boss is spawned, affecting every cat unit currently on the battlefield, and as such ignores units currently being displaced by any means.

**Also known as:** boss wave knockback

## Displacement

Any form of movement that isn't caused by a unit’s regular forward movement. Removes the unit’s hitbox from the battlefield over its duration, making it unhittable and invincible, excluding Holy Blast against burrowed Zombies. Also disables targeting it, allowing units to walk past it. Includes any form of knockback, warp, and burrow. Displacement resets the animation state of the targeted unit to a displacement animation; either warp, underground or knocked back, resulting in interruption of any attack animations that may have been active (foreswing and backswing).

## Flinch

Very short displacement spanning (how many?) DU over 11f caused by the sniper cat powerup. Can be timed by toggling sniper at the right moment, as it shoots once every ~10 seconds provided it has a target other than the enemy base.  
The normal cat cannon also inflicts this type of knockback.

**Also known as:** sniper knockback, cannon knockback

# Abilities

## Uptime

The duration of a unit’s [status effect](#statuseffects) ability relative to its attack rate. For example, if a unit attacks every 10 seconds and slows enemies for 5 seconds, its ability uptime is 50%.

## Massive Damage

Deals 3× ~ 4× damage against the [target enemy type](#trait), depending on treasures. Treasures don't exist for White, Relic and all event traits. Exclusive to cat units.

## Insane Damage

Deals 5× ~ 6× damage against the target enemy type, depending on treasures. Treasures don't exist for White, Relic and all event traits. Exclusive to cat units.

## Strong Against

Takes 0.5× ~ 0.4× damage and deals 1.5× ~ 1.8× damage against the target trait, depending on treasures. Treasures don't exist for White, Relic and all event traits. Exclusive to cat units.

## Resistant

Takes 1/4th or 1/5th damage from target type depending on treasures. Treasures don't exist for White, Relic and all event traits. Exclusive to cat units.

## Insanely Tough

Takes 1/6th or 1/7th damage from target type depending on treasures. Treasures don't exist for White, Relic and all event traits. Exclusive to cat units.

## Slow

Reduces an enemy's movement speed to 0.5 for a specified amount of time.

## Freeze

Stops any animation the enemy is currently in for a specified amount of time.

## Weaken

Reduces the affected unit’s damage by a specified percentage for a specified amount of time.

## Strengthen

Increases the unit’s damage by a specified amount after going below a specified HP threshold percentage.

## Knockback

[Displacement](#displacement) that moves the enemy back (how many?) DU over 12f.

## Critical hit

Deals double damage. Stacks with other abilities. Deals full 2× damage against Metal, instead of the usual 1 point of damage.

## Attack Only

Only attacks and hits the target trait and enemy bases, but does not walk past other enemies. Exclusive to cat units. Should the unit's trait targeting be taken away by means of [curse](#curse), it will only attack bases until the curse runs out.

**Also known as:** target only

## Base Destroyer

Deals 4× damage vs bases.

## Metal

Takes only 1 damage from every attack except [critical hits](#criticalhit). All enemy units with the metal trait have this ability.

## Burrow

An ability exclusive to (some) zombies. Once they encounter a cat unit in their standing range, they will disappear underground and resurface after advancing a given distance (which is different for every zombie enemy).

Some zombie enemies can burrow more than once.

Burrowing can be interrupted if the zombie is knocked back during its pre-burrowing animation.

## Revive

An ability exclusive to (almost) all zombies. Once these enemies lose all their health, they leave a corpse behind and revive with a certain percentage of their original HP after a given duration (those two numbers are different for every zombie enemy). Zombie enemies can revive either once or infinitely.

## Zombie Killer

Units with this ability prevent zombie enemies from reviving. The ability only activates when they land the last hit. If another unit that doesn’t have the ability lands the final blow on a zombie on the same frame as a zombie killer hits it, the ability will still apply.

This ability is passive, and therefore guaranteed to activate on every hit.

**Also known as:** zombie kill, z-kill

## Barrier

An ability that is exclusive to enemies, commonly found in Starred Aliens.

Barriers have a predetermined breaking point: as soon as a cat unit deals damage in a single hit that meets or exceeds this breaking point, the barrier disappears and does not regenerate. The hit that destroys the barrier does not deal additional damage to the enemy. Any damage lower than the breaking point is absorbed and does not count toward the total damage needed.

An enemy with an active barrier cannot be afflicted with status effects. Other abilities can still apply, including trait-specific damage multipliers.

## Barrier Breaker

Units with this ability can break enemy barriers without needing to exceed their breaking point. If the ability triggers on a barrier, the barrier itself is ignored and the full damage of the attack is dealt to the enemy.

This ability is chance-based.

## Warp

An ability exclusive to enemies. Cat units hit by it will be sent forwards or backwards by a specified range for a specified amount of time, depending on the enemy. If a cat unit is [knocked back](#damageknockbackhitback) by the damage from the warping attack, it will not be warped.

## Curse

An ability formerly exclusive to enemies, commonly found in Relics. This effect nullifies [all abilities that target specific enemy traits](#activetargetingabilities). [Non-targeting abilities](#activeuniversalabilities) are not disabled.

## Double Cash

Grants double the money reward upon killing an enemy (has to be the killing blow). Exclusive to cat units for obvious reasons.

**Also known as:** bounty, double bounty

## Survive

Instead of dying when reaching 0 HP, units with this ability regain 1 HP and reposition with a [hitback](#damageknockback) once.

**Also known as:** survive lethal strike

## Savage Blow

Increases damage by a specified multiplier on activation. Although this multiplier can technically be anything, every savage blow unit so far uses a 3× multiplier.

## Immunity

Immunity to a specified active ability. Does not exist for damage multipliers.

## Resistance

Reduces the effects of enemy abilities by a specified percentage. Does not exist for damage multipliers.

## Wave Blocker

Interrupts any wave chains passing the unit’s hitbox and negates the damage from the wave that hits it. Does not negate the damage from the attack that created the wave.

## Wave

Ability triggered on a successful hit with a given proc chance and a given level. Each individual wave deals damage and applies effects in an area. For cat units, the first wave starts at 332.5 DU away from the caster, for enemy units at 466.5 DU. Every wave level past level 1 adds another wave instance of 200 DU to the end of the last one, extending the progression. The waves don't all appear simultaneously, but spawn 4f apart. The first wave spawns 3f after the attack hits. The damage is not instant, but applies after 8f. A wave can transport other abilities as well (copying the result of the original attack instead of rerolling the chances for each individual wave). Wave immune units ignore both damage and procs from the wave, but not the attack that triggered it. Wave chains can be interrupted by units with the wave blocker ability, who negate and ignore the damage and effect of the wave level that hits them and every wave in the chain that would follow, for every allied unit.

**Also known as:** shockwave

## Mini Wave

Identical to the wave ability, but deals 20% of the original attack's damage and has a different animation.

## Surge

Ability triggered on a successful hit with a given proc chance and a given level. Each individual surge deals damage and applies effects in an area, both repeating a given number of times determined by the surge's level. Surge has 375 DU width, with 125 DU being piercing range and 250 DU being inner range, with the Surge's spawn point as reference. The surge's spawn point is chosen randomly from its spawning area, which is handled similarly to LD, having both a minimum range and a maximum range which limits it. The surge spawns after 15f of the attack hitting, lingering an extra 20f for each surge level. Surge can transport other ability procs as well (copying the result of the original attack instead of rerolling the chances for each individual surge level). Surge immune units ignore both damage and procs from the surge, but not the attack that triggered it.

## Suicide

Units with this ability disappear after attacking once. If the [foreswing](#foreswing) is interrupted the unit will survive and attack again, if the [backswing](#backswing) is interrupted it will disappear after the displacement.

## Multi-Hit

Units with this ability will attack multiple times in one animation. This implies that there are animation phases in between foreswing and backswing. Timings can be vastly different. If the animation is interrupted by displacement before the last hit lands, in other words before the backswing starts, attack cooldown will not start and the attack animation will start from the beginning, repeating every hit. Every individual hit is independent, meaning it can have its own damage value (e.g. Awakened Bahamut with 3 attacks: 85,000, 3,400, 5,100) and abilities (e.g. Crazed Moneko, only last hit crits). The maximum number of multihits is 3.

# Credits

**TheXientist**#7112 (compiling most of the terms and their definitions)

**Waran-Ess**#9801 (minor edits and web conversion)