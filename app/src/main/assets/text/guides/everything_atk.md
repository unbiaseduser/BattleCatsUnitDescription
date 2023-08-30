# Disclaimer

>**This resource is several years old and does not represent current knowledge on the game.** This is most apparent in the section that covers movement speed.<br><br>If you see incomplete or inaccurate information, please do not ignore it. I do not know enough about internal mechanics to rectify this myself, but if you do (or know someone who does) and are able to re-write some of the sections, you can send them my way through Discord @Waran-Ess#9801.<br><br>Note that I will not re-write these sections for you. If you have the knowledge, you can write them yourself.

# Introduction

In the game, everything either attacks, or gets attacked, it's pretty much the whole gameplay. It may seem very straightforward, but to completely understand things like phenomena, or how cats are rated, it is very important to understand the fundamentals of attacking.

There are some things missing in here, hence "almost". Some of them are known, and it's possible there are some things that are still not discovered.

# Attack Cycle

Look at any one of your cats in battle. You can see them attack. In fact, the process of attacking is a cycle, that repeats itself continuously. All attacks follow the same rules, and all parts of the cycle is constant in each individual cat. They are part of the stats of the cat/form of the cat, just like health and damage, except that higher levels don’t change it. It also applies for enemies.

Attack animation is the animation that units make to execute their attack. This is actually two phases in one: the time between the start of the attack animation and the actual attack occurring is what is commonly referred to as **Attack Animation**, so this term can refer to both things. When we want to talk strictly in numerical terms though, that is when attack animation denotes the start of the attack animation and the actual attack occurring.

The second phase of the attack animation is the time between the actual attack occurring and the end of the attack animation. This is something NOT recorded by Spica nor the Wiki as a stat. This phase is commonly referred to as the **Backswing**.

Now, at one point Ponos had simply been using one formula for backswing, which is `(Time Between Attacks – 1f)`, rather than making it an arbitrary stat. It seems that cats which use this formula don't behave like units with arbitrary backswing stats. This is most obvious with Dragon Cat. According to Jones, he couldn’t find where this stat is listed in the game data.

The third phase is the time between the end of an attack animation and the start of the subsequent attack animation. In this phase, units will be idle or moving forward when there is space. When the time is up, they will attack as soon as something comes into range. This phase is called the **Time Between Attacks**.

Another stat recorded regarding the attack cycle is the time between an actual attack occurring, and the next actual attack occurring, called the **Attack Rate**. This is a convenient number for calculating DPS in theory.

This is a cycle basically, it goes in the order:

Attack Animation → Actual Attack → Backswing → Time Between Attacks → Attack Animation → Actual Attack → Backswing → Time Between Attacks → Attack Animation → so on and so forth.

That's what can be obviously observed, but the truth is that the system doesn't totally function like that. You can use common sense to try and understand what you see, but it is not correct when you look at the stats themselves.

The proper attack cycle is:

<img src="file:///android_asset/img/guide_support_img/atk_timing/atk_cycle.png" width="100%"/><br>

What's this **attack cooldown**? Simply, its the time between the actual attack, and the start of the next attack animation. In reality, it is not the backswing or the time between attacks that dictate the attack cycle. That's why they actually do not exist as stats. Attack cooldown, however, is obeyed all the time, same as with attack animation. Then, under this, the backswing and time between attacks are fluid with each other. Their total value just needs to be equal to or more than the attack cooldown.

The stat that Spica records is actually an "attack interval" stat, not time between attacks. Attack cooldown is derived from `(2 × attack interval – 1f)`.

The mechanism is as such: after "actual attack", the cat is in "backswing", and after it is done, will be in "time between attacks". The attack cooldown is fixed. Thus, backswing can be interrupted or end on time, the time between attacks will simply be `(attack cooldown - actual backswing duration)`. That's one way to look at it, as mentioned though, attack cooldown is the real core of the behavior. So, the formula used should be JulietCat's:

`Attack rate = Attack Animation + max(Backswing, 2 × Attack Cooldown – 1)`

This formula applies to all cats. Dragon Cat's and Bahamut Cat's attack cycle makes sense now. It works for 0f time between attacks cats, since attack cooldown is also zero. The third group of units this applies to are units in between: they have substantial backswing, but their attack cooldown is not zero. The initial explanation also fails to explain their behavior when knockback is involved.

**Multihit** has some extra things to talk about, as they act quite differently. The attacks happen at some specified time as well. The main difference is which attack timing follows the "attack animation" characteristics. In fact, it is the last attack that follows the behavior, i.e. if the attack animation is interrupted before the last attack, the cat will attack again, regardless of whether it has done the first, second or no attack. Attack cooldown starts from the last attack as well.

>People have other names for each part of the attack cycle. It was initially thought it was best to standardize the terms used, but as long as people know what you're talking about, there's no issue. All these parts of the attack cycle can be observed, so using what you've seen in battle is a good way to guess what people are talking about.

Now, these parts of the attack cycle all last for some duration, and are innate stats in all cats and enemies. There are some desirable standards in these stats depending on the cat being analyzed. The desirable standard also changes depending on the enemies or stages that the cat is used against.

-   Generalist ubers prefer to have short attack animation. This minimizes the chances they miss due to the enemy that they are about to hit being knocked back, or they don't attack at all due to being knocked back. A partial exception, you'll see later.
-   Excessively long attack rate is typically bad. It equates to hard hits with long time in between, which means they can get overwhelmed by rushing enemies before they get the next hit in. On the other hand, sometimes hard hits are very preferable, like for breaking barriers, and long attack rate can allow the cat to be stacked. Sometimes it doesn't matter. More recent content usually involves rushes, so that's why long attack rate is more undesirable these days.
-   Long backswing generally is good or doesn't hurt to have. It may be bad against enemy LD attackers as the cat may linger in the LD range. It may also sometimes reduce their presence in the battle if they also have bad speed, as they might fall behind easily

Some examples can help you understand these ideas:

-   On the topic of attack animation, you've probably raged at Bahamut winding up his attack, only to be killed before he finishes the attack animation.
-   On the topic of attack rate, Bahamut vs Ururun is a good example. It matters a lot more against enemies with many knockbacks, notably Black enemy spam. Yet, Bahamut stacking is very useful for ensuring victory in some early game stages.
-   For backswing, the classic case for long backswing being good are Awakened Bahamut/Yukimura/Maglev. These units are strong at rushing, and when knocked back, can attack again instantly. This lets them output a lot of damage in a shorter time. The disadvantage is best seen in the Awakened Bahamut vs Mr Mole matchup. If Mr Mole attacks immediately after being knocked back by Awakened Bahamut, he will hit Awakened Bahamut who had just completed his backswing.

These stats can be found on the Spica for cats. For enemies, /u/JulietCat is maintaining the aboutList on a Reddit wiki page that you can find the link to in the FAQ under "Enemy attack timings?" Or [just click this](https://www.reddit.com/r/battlecats/wiki/stats/enemy_timings).

>### Learning checkpoint
>Explain qualitatively why, in The Path of Kung Fu, when Bahamut and Ururun are deployed, Bahamut may die to Dancer Cat before Ururun even though he has more range, and the factor that causes this scenario to be more frequent than expected. Assume the lineup and setup to be something old school like this + Sniper Cat item.

<img src="file:///android_asset/img/guide_support_img/everything_atk/old_school.png" width="100%"/><br>

That's about all for this section, the next two sections are much simpler.

# Types of Attack

Attacks can hit enemies in different manners. The two main types, which were the only two types initially, are single target and area attack.

**Area attack** hits everything that is within the range of the attack. Damage and abilities like damage multipliers are calculated for each individual enemy, but chance for ability activation (e.g. wave, freeze) is only rolled once.

**Single target** is actually is a bit more complex. There can be many enemies in the range of the attack. It will always hit the enemy closest to the attacker. However, sometimes there can be more than one enemy in the same position, so there can be more than one target. In that case, it appears who gets hit is random.

>Adding to the scenario, let's say that there are many single target attackers, and they all are attacking a stack of enemies in the same position. Something interesting is that it is possible that the attackers pick the same enemy to hit. So if, say, there are 3 attackers attacking at the same time, they can hit 1, 2 or 3 enemies at once. This is most notable when fighting multiple synced Capies.

More modes of attacking have popped up since. Shockwave isn't a mode of attacking, but it is a medium of attacking that has mechanics surrounding it too. You can refer to [dkaf's shockwave guide](https://www.reddit.com/r/battlecats/comments/6le9pi/tutorial_how_shockwaves_work/) , or [Lucas IV’s video guide](https://youtu.be/plytTYr24x0).

**Long distance** introduced a new type of attacking, that incorporates single target and area attack into it. The attack happens in some specific area beyond the cat. In that way, it can now be imagined that normal, non-LD attacks hits from 0 range up to the attack range of the cat, while LD attacks hits the specified range.

You can read up on Long Distance Mechanics in [this very old but very gold post by /u/ALadyInLuck](https://www.reddit.com/r/battlecats/comments/44ek92/the_long_distance_mechanic_the_guiding_hand_of/). Here is also [a more recent video by Lucas IV](https://youtu.be/Z-osLFU6G58).

We have **Omnistrike** as well. Borrowing from the concept from LD, Omnistrike is an attack that starts from a specified negative range i.e. behind the attacker, and hits up to some specified range in front of the cat. Unlike LD attackers, attackers with Omnistrike stop when the opposing base is at their farthest range. This is probably why they made the distinction between LD and Omnistrike, which didn't exist before even though there were units that had negative LD range, notably Daboo.

>Omnistrike will always be Area Attack, but LD can be single target, and it will apply the rules of single target on what enemy it hits.

# Damage, Damage-Altering Abilities and DPS

The **damage** a cat or enemy does is fixed at a base level. A cat's damage is decided by its lvl1 damage, and every level increases the damage by some percentage of the lvl1 damage. An enemy's damage is decided as a base stat. Enemies can be given strength magnifications to increase their damage proportionately.

There are **damage-altering abilities**, affecting both for damage dealt and damage taken.

Cat-exclusive abilities are Strong Against, Massive Damage and Resistant Against. Their effect may be buffed by the Fruit treasures found in ITF and CotC. They may also be buffed by Cat Combos. The Fruit treasure buffs are addition/subtractions and are applied first, while Cat Combo buffs are multipliers. You can find these multipliers (plus some that aren't here) in [the ItF Guide](https://thanksfeanor.pythonanywhere.com/guides/documents/itf.html#treasures), or just read them below.

-   Strong against increases the damage done to, and reduces damage taken from the given enemy type. Initially, damage done will be 1.5×, and damage taken will be 0.5×. With full treasures, damage done is 1.8×, and damage taken will be 0.4×. Cat Combos apply a multiplier to the damage done, and divides the damage taken with the same factor.
-   Massive damage purely increases damage dealt, but by a larger factor. It is initially a 3× multiplier. Treasures increase the multiplier by 1, to ×4 if no combos are in effect. Cat Combos apply a multiplier to the damage done.
-   Resistant Against purely reduces damage taken, but by a larger factor. It is initially a 0.25× factor, which improves to 0.2× with full treasures. It helps to put this in the other perspective, which is that against that type, it goes from having 4× the health to 5×. Cat combos buffs the ability by dividing the factor, same as with Strong Against.

>### Anecdote: the rise of the Whales, and the Strong Wombo Combo
>There was a point in time where Crazed Whale was a popular unit as a cheap source of damage as well as a damage sponge, that is a bonus when used against Reds. However, Island Cat did not get the same love. He was quite rarely used even for Reds, only being used in very Red focused stages like Last Gang, Pig Swill and Red Alert. The understanding was that Island was not worth his cost. Crazed Whale not only shared the stats of a lvl 40 Island just like his Crazed counterparts, but was also cheaper by ~39%.<br><br>We've had more threatening Red enemies since, and it all started with Sea Polluter. Against Berserkory, the only better choices are Cat Machine TF, and Octopus+ iCat which won't always work. Capy is also sometimes dispatched quickly with double Whales. Yet, the first time we see the power of Strong Against was against the classic Red threat in Benzene Field.<br><br>First of all, Benzene Field is one of the most unique and creative stages in the game. It makes use of enemy limit and a stack of weak Faces to create something like a reverse fuse. When you kill them, you'll be met with a wave of Bores. When busted gacha TFs didn't exist, we were still relying on mainly offensive tactics for Reds, and we had no reliable non-uber area Red CC unit. It was basically game over.<br><br>Here was the first strategy that was spread (taken from Wuffa’s vid)

<img src="file:///android_asset/img/guide_support_img/everything_atk/wufga1.png" width="100%"/><br>

>It was not totally reliable. Having higher level units doesn't even necessarily help, since that accelerates the rate that damage is done to the Faces. The strategy stacked King Dragons which in hindsight is very odd.<br><br>It's unknown when exactly, but soon people noticed the Strong Wombo Combo. Here is the lineup used by Wuffa for 3 stars.

<img src="file:///android_asset/img/guide_support_img/everything_atk/wufga2.png" width="100%"/><br>

>Much more solid and reliable. The combos don’t cause more damage to be done to the Faces which makes it all the better. It helps that Awakened Bahamut existed, so the first Rain D could be killed very quickly.

## Other abilities

Some other abilities are seen in both cats and enemies:

-   Metal trait reduces damage taken to 1. It does not matter whether the cat has Strong Against or Massive Damage, the damage will be 1.
-   The continuation of this is Critical Hit, which is the only offensive counter to Metal. The first thing it does is to double the damage dealt to the enemy, then secondly, ignore metal trait.
-   Savage Blow is similar to Critical Hits, though the damage multiplier is different. The game's code tells us that the multiplier can be customized, though every unit with is as of JP 10.6 uses a 3× multiplier. Savage Blow also does not bypass the Metal trait.
-   Shockwave mechanics were covered earlier. The attack must hit something before the chance is rolled. Shockwaves can essentially double the damage dealt to the unit that was hit by the original attack, provided they are not knocked back by damage.
-   Base destroyer affects only bases. It multiplies damage dealt by 4.
-   Strengthen has two stats involved. It increases the damage dealt by the unit, after its health reaches a certain percentage. The multiplier to damage dealt and the health threshold are arbitrary.
-   Weaken also has two stats involved. It reduces the damage dealt by the unit by a certain factor, by a certain duration. Weaken does not remove Strengthen from a unit, and vice versa.
-   Curse is an ability most commonly found in enemies, though a handful of cats now have it as well. They nullify abilities in a certain manner. For this topic, just know that Strong Against, Massive Damage and Resistant all stop functioning when Curse is applied on the cat.

## DPS (Damage Per Second)

DPS is a stat that is commonly cited for judging cats meant for offensive purposes, and for certain cats, for the exact opposite. It is simply calculated by dividing damage dealt by attack rate. The assumption is that the unit is attacking as soon as possible, is not interrupted, and the attack is hitting something all the time.

Sometimes, we may also consider damage altering effects and use it as the DPS of the cat under the conditions that the effects are active. It's part of what the cat can do, so we factor it in the assessment of the cat. This is referred to as **effective DPS**.

DPS does not paint the whole picture though; one example was brought up earlier where units with long backswing and no time between attacks can attack at a rate faster than their attack rate. Sometimes a cat isn’t used for their DPS, so the stat is not relevant. Sometimes the assumption doesn’t hold true often enough that the DPS value itself is questionable.

# Attack Range and Movement Speed

## Range

Attack range is another major point of discussion for judging a cat's use. If the cat is incapable of attacking a lot of important enemies, it'd be unimpressive, and attack range is probably the most common stat that decides this.

Attack range not only determines how far away the cat detects enemies, and will stop moving and start attacking, for non-LD/Omnistrike units, it also determines how far up the attack hits to.

We generally like to split attack ranges into different tiers. What tier range is acceptable for a cat depends on its purpose. Very fast cats that are mostly intended for rush-attacking, stalling or suicide-bombing can all work with low range. Again, Maglev, Yukimura and Awakened Bahamut exemplify this. Tanky cats can also work with low range, in fact they need it if they want to do that job with most of their life span. Examples are Zamboney, Cat Machine and Kai.

There is a range tier we call "mid-range", which is generally from 200+ to 350 (sometimes a bit higher for ubers). This range can outrange most rushy enemies. Mid-rangers tend to be stronger at their role as attackers or crowd controllers than cats in other range tiers against these low ranged enemies. You can skip briefly to the next section to get an idea why. The few exceptions are due to very highly specialised cats.

Above 350 range can be considered high range, but it can be broken up into smaller categories, and there are important distinctions too. 400 range is well known to be Dragon Cat range. 450 range is well known to be Bahamut range, or Sloth/Master A range (Master A really has 451 range). Above 500 can be seen as a very high range, but 600 is where it gets very attractive, just like Aphrodite. Then there are extremely high ranges that very few cats have, which notably are Nerd (1200), Cosmo (850) and certain Lugas.

>### Anecdote: Why Flying Cat sucks
>The Flying Cat may have supreme DPS, but has a measly 170 range, which severely limits what enemies he can use his high area DPS against. This disappointing reality can be demonstrated in Black Premonition, when compared with Paris Cat.<br><br>Notably, SBK, Gory Black and Dark Otter have 158, 155 and 190 range respectively. Even if there was no Dark Otter and Bun Bun Black yet, Flying Cat won't be able to leverage his ranged area DPS well. When the stack of Black enemies stagger their attack, they push little by little, but very surely will at a fast rate. Flying Cat will hit the front unit, but by the time the Blacks push enough that multiple are in Flying Cat's range, they most likely will hit Flying Cat already. Paris Cat won't have that problem, and can proceed to leverage on her area attack. Some other early-mid game examples where this problem exists is Fish Hell and Star Ocean.<br><br>Flying Cat's usage is thus limited. His best use is against the likes of One Horns, like Cat Trial. A more recent use of him is in Celestial Seas, in a difficult but extremely effective strategy. You can see it in action [here](https://youtu.be/P4ZF_smknM8), as well as in a different stage [here](https://youtu.be/KicsWQzV3-c).<br><br>Crazed UFO alleviates this problem by having bonus range, but sometimes it's still frustrating. Then Cameraman and Castaway popped up, and ever since we've rarely seen the the two Bird variants used ever again. Cameraman and Castaway not only had higher range that was much more comfortable to use, they also came with higher survivability, and cheaper cost in the case of Cameraman. They became the go-to options for high DPS area attackers.

## Movement Speed

Speed is a stat that we actually know the least about right now. It's expressed as a single whole number. We do not know how it translates to distance moved per frame, the closest we've gotten was Kaxzer getting at a multiplier of about 16.66666... for conversion to distance travelled by second, or 0.55555... distance per frame. Note that the distance unit is the same as with attack range.

Is there such a thing as a fractional unit of distance in the game? We're not sure.

Speed has an interesting implication on range. See this classic example:  
Li'l Nyandam has 551 range while Camelle has 550 range, yet sometimes Li'l Nyandam will fail to outrange Camelle, walking to Camelle's range and getting hit.  
It's only a theory, but we believe this theory is likely to be true. This phenomenon is caused by how the game runs.

First and foremost, the game runs at 30 frames per second. As mentioned earlier, speed can be converted to distance travelled per frame. We believe that units will always move that distance per frame, if they are able to move, i.e. as long as there is no enemy in their attack range.

Applying this in that example, Li'l Nyandam has a speed of 5, which equals 2 or 3 distance per frame. If he was at 552 or 553 range from Camelle, he will move up and in the next frame, he may be 549 or 550 range away from Camelle, and will get hit.

# Knockback Mechanics and Endurance

Knockback is an interesting effect in battle. It's become more unusable in recent times due to stage design, but it's pretty unique, thanks to the mechanics of it. These mechanics apply to all forms of knockback. They have played a part in many different strategies for a long time. To name a few, increasing the time your cats can hit Nyandam, Sleipnir saving Awakened Bahamut from Clionel, maximising Maglev's damage in Floor 39, and ultimate clip cheesing of different stages.

The big one is that the hitbox of units under the effect of knockback do not exist. That means units can move through them, and units that are in the midst of being knocked back cannot be hit by attacks.

Knockback interrupts the attack cycle. Units who are knocked back in the middle of their attack animation, will have to start up their attack animation again. Units in backswing will end their backswing early, and may attack sooner if their attack cooldown has already ended. This is most prevalent in cats with 0f attack cooldown.

Sources of knockback include ability knockback, damage knockback (also known as DKB or hitback), Cat Cannon + Sniper Cat knockback (also known as flinch) and boss wave knockback. There's also Cat God knockback.

Here are some extra points on these other sources of knockback:

-   Cat Cannon and Sniper Cat knockback distance was estimated to be about 100 range, tested a long time ago by Kaxzer. JonesHtog aka SilumanTomcat did recent tests, and estimated damage knockback to be 325~350 distance, and ability knockback to be 155~160 distance. Video of his tests [here](https://youtu.be/64-z_stthuQ).
-   Each damage knockback occurs at equal intervals of max health. Damage can be large enough to skip damage thresholds, and it will still be one knockback.
-   In relation to ultimate cheesing or some other strategies, it may be good to know what distance and duration of the knockbacks are, though we know generally little about it. We don't need to know that much, it is enough to know that the cats with the highest speed stat are capable of clipping through most hitboxes in the game.
-   Sometimes, we may use knockback for cats to move in closer to the enemy line, which may allow them to hit more enemies, rather than just the ones in front. The speed required of the cat to do this is quite high.

>Related to damage knockback, sometimes it is inevitable that a long range attacker cannot be protected well enough and gets hit. Something that determines how long such cats survive under these circumstances is their number of knockbacks. The more knockbacks the better, if all knockbacks are utilized, the cat will live for a long time, and constantly reset the distance between it and the enemies.<br><br>Conversely, sometimes we may face enemies that simply outrange the cats we have, and we may be interested to know which cats can withstand the damage and hit the enemy before or without being knocked back. The fewer knockbacks, the better.<br><br>The stat that is common between these two opposite scenarios is **endurance** (formerly known as knockback resistance). It is obtained by dividing the cat's health by its number of damage knockbacks. It is the minimum amount of damage the cat takes before being knocked back.<br><br>In the first scenario, the application is a lot less profound. It does help to look at it to judge whether the cat is really able to make full use of all its knockbacks, or the damage typically done will exceed damage thresholds.<br><br>For the second scenario, the application is much more important. It is one of the big determinants for whether the cat can hit an enemy that is inevitably hitting them. The other big factor is speed, which tells us how many hits the cat needs to take to get to attacking the enemy. The 3 types of enemies where we care about this are very long ranged ones (Camelle and Master A), LD attackers (Tackey and Dolphina) and occasionally shockwave attackers (Berserkory).

# Assessing Cats: Theory vs Practice

Really the best way to judge whether a cat is capable of hitting an enemy, or vice versa, is to send them against each other in the game itself. Theory looks nice and all, but testing in-game is always more credible.

Still, it's not wise to judge a cat if you've only seen it in very few stages. You can't just say "Balrog is unusable" when you send him out in Bun Bun stages or other rush stages. That is when looking at the numbers help inform your judgement, and understand where and how to use a cat.

Both theory and practice can change your perspective when you look at one, look at the other, then the first one again. One cat whose potential was understood this way was D'artanyan.

Sometimes cats are good not for their stats, but for cat combos. Skelecat is terrible as a cat, but he forms two parts of the cat combos in the iconic Biohazard + Bony Bone Research combo setup.

# Tradeoffs in Stats

Within games, there's this concept called balance, that game developers sometimes strive for. Sometimes.

Balance basically means the units have some chances, the more units that are usable the better. This makes the game feel more fair to play as not everyone may have the same cats.

When cats have different spread in stats, balance is done by doing tradeoffs in stats. One example is Dragon vs Titan Cat. Titan has much better health, high damage and faster attack rate, but has much lower range than Dragon Cat. The result is that both cats have their own situations where they can work, rather than that one cat is strictly better than the other.

Tradeoffs can be seen in abilities too. Comparing Sanzo and Necrodancer:

|Cat|Sanzo|Necro-Dancer|
|:---:|:---:|:---:|
|**Area Attack**|<u>Yes</u>|No|
|**Range**|250|<u>300</u>|
|**Attack Rate**|<u>29f</u>|54f|
|**Ability Chance**|20%|20%|
|**Ability Duration**|<u>144f</u>|108f|

They are both used mainly for their crowd control, so health and damage don't matter much, perhaps only in few situations health might matter. Not only are the parameters of their ability factors, attack range, type of attack and attack rate are also points of consideration, so the different aspects of attacking all have some influence on a cat's performance in every role.

Generally, the most complicated cats to compare are the Ubers. They are usually high cost attackers, so how well they act as attackers matter a lot. They may have abilities for crowd control, so that comes into play along with whether their attack-related stats allows them to use it well. That's why many of the heated discussions about the game are about them.

# Afterword

Even in the times when the game was at its simplest, one concept covered here was very important to understand, which is attack range. People who didn't know about it will never have beaten Chapter 3 Iriomote, now known as Moon.

In fact, this simplest concept of matchups in range was the greatest source of fun in the early days. Dragon vs Bun Bun, Bahamut vs Le'Boin, but sometimes using Titan for Master A and Whale for Nyandam.

Newer mechanics add more ways to approach the battle, but it's not really possible to make a completely new stage that requires completely new methods of beating it.

I hope you have a good idea of at least the basics now. In a game like Battle Cats, there is rarely much complexity, but at times you may be called to think on a deeper level.

>Here's a final question for you.  
[What is happening in this clip?](https://youtu.be/RdrksyfuPag?t=53s)  
[Or this one?](https://youtu.be/VEYqXcLq_E4&t=56s)

# Thanks for reading!

Like was mentioned earlier, there exists [a condensed version](app://sixty.nine/atk_timing_condensed) of this guide, which focuses on attack timings.

You may also want to refer to the [terminology guide](app://sixty.nine/terminology).

# Credits

**dkafsgdhh**#0809 (original guide redaction)

**Waran-Ess**#9801 (minor edits and web conversion)