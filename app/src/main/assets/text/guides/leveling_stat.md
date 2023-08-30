This article will explain the mathematics and mechanics of levelling up cats in battle cats. Some elementary algebra will be used to explain points, though conclusions in plain English will also be given. This will be of interest to you if you have ever had questions like:

-   How much HP/damage does a cat gain when I level it?
-   Why does Bahamut not gain as much power between Lv 30 and Lv 40 as other cats?
-   How do attack talent orbs work?
-   Is it worth levelling cats past 45 when it costs two catseyes?
-   Just how important are EoC treasures?

The results here are a combination of common knowledge, datamined information from the game’s code, and mathematical analysis. Information is believed to be correct as of October 17th, 2020, and based on BCEN 9.9.

# Introduction

Every form of every cat in the game has base stats; initial damage per hit and HP values. These are the stats a cat would have at Level 1, with no EoC treasures at all. Note that I said every form of every cat. These values can change from form to form of the same cat, and when a unit’s evolved/true form improves it stats, it is because these values change. For these purposes, different forms of the same cat are hence unrelated, and are as different to one another as unrelated cats.

We will call these base stats _**D**_ and _**H**_, for damage and HP respectively.

When you obtain EoC treasures, or level up your cat, you are increasing its stats above these base values. The higher a cat’s base stats are, the more levelling/treasures increase them - it is a proportional increase. This is why it is more important to level up cats with large stats, rather than those with status effects but poor raw stats.

# Treasures

First let us discuss how much EoC treasures increases stats. Legendary Cat Sword treasures increase damage, and Legendary Cat Shield treasures increases HP.

A level 1 cat without treasures has _D_ damage and _H_ hp.

For x% completion of Legendary Cat Sword, a level 1 cat will have `(1 + 1.5x / 300) × D` damage.

For x% completion of Legendary Cat Shield, a level 1 cat will have `(1 + 1.5x / 300) × H` HP.

What this algebra means is the following:

-   At 100% treasure completion (all of chapter 1 superior treasures), cats will have 1.5 _D_ damage and 1.5 _H_ HP (+50% - one and an half times the stats)
-   At 200% treasure completion (all of chapter 1 & 2 superior treasures), cats will have 2 _D_ damage and 2 _H_ HP (+100% - double stats)
-   At 300% treasure completion (all of chapter 1, 2 & 3 superior treasures), cats will have 2.5 _D_ damage and 2.5 _H_ HP (+150% - two and a half times the stats)

This is a significant difference and these are some of the most important treasures to get very early on in the game as a result. These extra stats continue to multiply your stats after levelling up too. At 200% treasure completion, a cat at any level will have double the HP and damage as it would without any treasures.

Note that Catbot (as well as sites like mygamatoto) show stats of cats with x = 300% treasure for both sword and shield.

...which leads into the next question. How much does levelling up improve your cats?

# Levelling

At low levels, levelling up a cat once increases its stats by 20% of the base stats (+0.2). That is, without treasures for now:

-   Lv 1 : 1 _D_ damage, 1 _H_ HP
-   Lv 2 : 1.2 _D_ damage, 1.2 _H_ HP
-   Lv 3 : 1.4 _D_ damage, 1.4 _H_ HP

and so on.

This means that a Level 6 cat is twice as strong as a level 1 cat, and a level 11 cat is three times as strong as a level 1 cat.

We will now assume full EoC treasures for all following discussion, so all stats are multiplied by 2.5. As a result, the 20% increase with each level is now a `2.5 × 20% = 50%` increase.

-   Lv 1 : 2.5 _D_ damage, 2.5 _H_ HP
-   Lv 2 : 3 _D_ damage, 3 _H_ HP
-   Lv 3 : 3.5 _D_ damage, 3.5 _H_ HP

and so on.

We still observe that a level 6 cat will be twice as strong as a level 1 cat, and a level 11 cat three times, as before.

A general formula for the stats of a cat at level L, would then be:

Level _L_ → `|2.5 + 0.5(L — 1)| × D` damage and `|2.5 + 0.5(L — 1)| × H` HP.

or, with some simplification:

Level _L_ → `(2 + L / 2) × D` damage and `(2 + L / 2) × H` HP.

That is, a Level 30 cat (with full treasures) would have `(2 + 30 / 2) = 17` times its base stats (level 1, no treasures). Level ups due to XP, due to + levels from dupes, and catseye level ups all increase L by one as usual.

# Levelling Curves

This is not the end of the story, however. At higher levels, the amount of stats gained becomes smaller (or in one special case, higher!)

For example, if we look at the damage Glorious Amaterasu ( _D_ = 1000 ) does at different levels, we observe the following:

-   Level 30 : 17000 (17 _D_)
-   Level 40 : 22000 (22 _D_)
-   Level 50 : 27000 (27 _D_)
-   Level 60 : 32000 (32 _D_)
-   Level 70 : 34500 (34.5 _D_)

You may notice that the increase between 60-70 is half the size of the increase from 30-40, or 40-50, or 50-60. Yes, after a certain point, if you are lucky enough to have a +20 uber, Amaterasu starts to get less benefit from levels. However, this doesn’t just affect ubers, and can affect almost all cats to some extent, including many that you will realistically get to such levels. This is called a Levelling Curve.  
There are three main types of Levelling Curves in the game:

## Type A : Normal and Special Cats

Gain normal increase (+0.5 _D_/+0.5 _H_) per level up to lv 60  
then half thereafter (+0.25 _D_/+0.25 _H_)

## Type B : Super Rares, Ubers and Legend Rares

Gain normal increase (+0.5 _D_/+0.5 _H_) per level up to lv 60  
then half (+0.25 _D_/+0.25 _H_) up until lv 80  
then a quarter thereafter (+0.125 _D_/+0.125 _H_)

## Type C : Rares

Gain normal increase (+0.5 _D_/+0.5 _H_) per level up to lv 70  
then half (+0.25 _D_/+0.25 _H_) up until lv 90  
then a quarter thereafter (+0.125 _D_/+0.125 _H_)


There are three exceptions to these rules.

## Bahamut Cat

Gain normal increase (+0.5 _D_/+0.5 _H_)) per level up to lv 30  
then half thereafter (+0.25 _D_/+0.25 _H_)

## Crazed Cats

Gain normal increase (+0.5 _D_/+0.5 _H_) per level up to lv 20  
then half thereafter (+0.25 _D_/+0.25 _H_)

## P2W Gacha Cat

Gains normal increase (+0.5 _D_/+0.5 _H_) per level up to lv 20  
then 3× (+1.5 _D_/+1.5 _H_) up to lv 30  
then 6× (+3 _D_/+3 _H_) up to lv 40  
then 9× (+6 _D_/+6 _H_) up to lv 50

See [the following post](https://discord.com/channels/355179033018892289/355181039947350016/750010051917381713) for a breakdown of the discovery and calculation of this.

---

The main takeaway of this is that levelling stuff past a certain level (usually 60) gives diminishing returns. For rares, super rares, ubers and legend rares, a further soft cap at 80 or 90 for rares exists.

The stat formula in equation is only valid for cats in the first part of their levelling curve. It is possible to write down more general formulae which account for the levelling curve, but this adds no further intuitive understanding and only makes the algebra more cumbersome, so will be omitted here. In future calculations in this document it will be assumed that _L_ is sufficiently low that the levelling curve does not need to be accounted for, so we can use the simple equation above.

Some notes on this:

-   Special cats (other than Li’l Cats, Bikkuriman specials, and Bahamut) are capped at 40/50 and won’t ever feel this curve.
-   Non-gacha rares and super rares (advent drops, etc.) are capped at 50 and won’t even notice either, except Crazeds who start to feel this earliest at Lv 20.
-   Ubers will most likely not reach more than +10 for most people, but if they do, the returns are diminishing after that point.
-   Legend rares are capped at level 59 and do not ever feel their levelling curve.

# Attack Orbs

Attack Talent Orbs also operate in terms of base stats. An S rank orb adds +5 _D_ damage to your attacks vs the relevant trait. (A = 4, B = 3, and so on). An increase in damage by 5_D_ is quite large, comparable to ten level ups at low levels. Basically, it is equivalent to powering up a Lv 30 cat to Lv 40.

A generalisation of the equation to include an orb of level _O_ would be:

Level _L_ → `([2 + L / 2] + O) × D` damage,

where _O_ = 0 for no orb, _O_ = 1 for a D-orb, and so on until _O_ = 5 for an S-orb.

Because of levelling curves, however, it is not always just “plus 10 levels”. Orbs always add the same amount of stats, but additional levels become less valuable as you level up more. It is hence also the equivalent of 70 to 90 on a rare, or 60 to 80 on a super rare, as the orb still adds the same amount of stats (5 _D_) but the level ups are worth less (0.25 _D_, 1/20 of the orb) than at lower levels. Indeed, very high levels, an S-Rank attack orb is worth 40 levels (0.125 _D_ = 1/40 of an S orb). A level 90 rare with an S rank orb has the attack power of a level 130 without, and a level 80 super rare becomes as strong as a level 120 with no orb. Ubers are unlikely to reach this level even for the most hardcore (legitimate) players so are not considered here, but they follow the same curve as super rares, as mentioned above.

Defense orbs do not directly work in terms of base stats, but instead based on a % reduction in damage, and are beyond the scope of this work.

Damage multipliers like Massive Damage do not stack with orbs, and only affect the part of damage which comes from levels. That is, for a damage multiplier _M_,

Level _L_ → `([2 + L / 2] M + O) × D` damage.

# Catseye Efficiency

Beyond level 30, it costs 1 catseye per level to power up a cat. Beyond level 45, it costs 2 catseyes per level instead.

The first thing to note, is that if you were to spend 10 catseyes, you could do either of the following power ups:

-   Lv 30 to Lv 40 (+5 _D_, +5 _H_) = 0.5 _D_/0.5 _H_ per catseye
-   Lv 45 to Lv 50 (+2.5 _D_, +2.5 _H_) = 0.25 _D_/0.25 _H_ per catseye

It is hence inefficient to use catseyes on powering up cats from Lv 45 to Lv 50, when there are still stat focused cats at lower levels, as the stat gain per catseye diminishes by half.

However, this is further complicated by rares and super rares with enough + levels to reach their levelling curve.

Consider a Level 30+25 rare. We could then do the following upgrades with rare catseyes, paying attention to the levelling curve C rules.

-   Lv 30+25 to Lv 35+25 (+2.5 _D_, +2.5 _H_) for 5 catseyes = 0.5 _D_/0.5 _H_ per catseye
-   Lv 35+25 to Lv 40+25 (+2.5 _D_, +2.5 _H_) for 5 catseyes = 0.5 _D_/0.5 _H_ per catseye
-   Lv 40+25 to Lv 45+25 (+2.5 _D_, +2.5 _H_) for 5 catseyes = 0.5 _D_/0.5 _H_ per catseye
-   Lv 45+25 to Lv 50+25 (+1.25 _D_, +1.25 _H_) for 10 catseyes = 0.125 _D_/0.125 _H_ per catseye

Notice that the last 5 power ups hit the levelling curve and add only half the stats, at the same time the catseye cost doubles, therefore the catseyes becomes only one quarter as valuable as before. The lesson to learn here is that boosting with catseyes can very quickly become very cost-inefficient with gacha rares/supers with many + levels. Be careful when deciding if it is truly worth it; for something with immense base stats like Can Can, it surely still is. For something more moderate, it might be worth considering if something less boosted is worth the investment.

# NP vs Plus Levels at high boost

Similarly, it is commonly advised to sell duplicate cats for NP if they are not stat-oriented units like Psychocat or Stilts. Meanwhile, it is worth adding a + level to attackers or high HP units like Cameraman or Ramen. This remains the case. However, there are also “borderline” cases where it could go either way. Cats like Vaulter with some respectable stats but also often used as a proc unit, for example. The levelling curve might be important to consider here, as the stat value of + levels eventually drops, but the NP returns remain constant.

Consider a Level 30 super rare with ok stats. You could

-   Use for +0.5 _D_ and +0.5 _H_
-   Sell for 15NP

Now consider a Level 50+10 super rare of the same species. You could

-   Use for +0.25 _D_ and +0.25 _H_ (half the gain)
-   Sell for 15NP (the same gain)

If you were unsure before, know that at this point the gain is halved if you choose to use vs sell. If user rank is your main concern, or if you don’t need NP, this will likely not concern you. If you are interested in performance vs cost, however, this may be worth keeping in mind.

Uber rares are worth a special mention. It is widely recommended to sell ubers for NP as they give a massive 50NP. If you are the type of player to + your stronger ubers, though, note again that at Lv 50+10, further dupes will give half the benefit, and the 50NP should now look more appealing.

As a general rule, I’d say the mere 5NP for a rare is never a game changer, and you can continue to + stat-based rares indefinitely, or at least up until the second soft cap at Level 90, comfortably. Super rares and ubers with their higher NP value are worth slightly more careful concern, however.

# The Importance of Treasures

In a previous section we derived the base stat multiplier of a low level (before the curve) cat at level _L_ with full treasures: We found the damage of a cat with full treasure at low Level _L_. We can also show that without any treasures, a cat at low level (before the curve) _K_, this is:

Level _K_ → `(0.8 + K / 5) × D` damage and `(0.8 + K / 5) × H` HP.

We can then ask the question, at what level _K_ does a treasureless cat need to be at, to be equal in stats to a level _L_ cat with treasures?

This is the solution of

`2 + L / 2 = 0.8 + K / 5`

or

`K = 6 + 2.5 L`

So a treasured cat with _L_ = 4, is equivalent to a treasureless cat at _K_ = 6 + (2.5 × 4) = 16. An Eraser at Level 4 with treasure has 2400 HP. An Eraser at Level 16 with no treasure also has 2400 HP.

At higher levels, this gets more complicated due to the levelling curve, but here is a graph showing the value of _K_ on the x-axis and the corresponding value of _L_ on the y-axis for rare (green), normal (blue) and uber (orange) cats.

<img src="file:///android_asset/img/guide_support_img/levelling/stat_graph1.png" width="100%"/><br>

To interpret this, draw a line from the level of the treasureless cat on the x axis up to the plotted graph, then draw a line across to the y axis to see what level that is equivalent to with treasures.

If we look at a Lv 100 normal cat with no treasure, we see it’s a bit weaker than a Lv 30 normal with treasures. A level 130 rare cat with no treasure is between lv 33 and lv 34 with treasures.

A level 120 treasureless uber doesn’t even reach lv 30 treasured stats.

Don’t neglect treasures!

# Crazed vs Normal stat ratio

It is commonly stated that _"manics at a given level have roughly the same stats as basics at double the level"_. Knowing what we now know about levelling curves, this merits some further investigation.

Let us consider King Dragon ( _D_ = 400 ) vs Manic King Dragon ( _D_ = 880 ). As a function of level (x-axis), the damage per hit of these cats with full treasure (y-axis) then looks like this.

<img src="file:///android_asset/img/guide_support_img/levelling/stat_graph2.png" width="100%"/><br>

Now, let us look at the ratio of these damage values (manic/normal, y axis) vs the adjusted level (x-axis) such that x = 40 means Lv 40 manic and Lv 80 normal (double) and so on.

<img src="file:///android_asset/img/guide_support_img/levelling/stat_graph3.png" width="100%"/><br>

We see an interesting curve. At low levels, the manic is actually far better than the double-level normal.

This is because at low levels, a big percentage of the damage is from the initial level 1 value of 2.5 _D_, and the normal cat only has a few extra levels on the manic. At higher levels, the difference becomes smaller as the normal takes a bigger lead in level, starting to balance out the huge initial stat difference.

At x = 20 (lv 20 manics, lv 40 normals), the normals start to close the gap even more as the crazed hit their levelling curve soft cap.

This trend continues until x = 30 (lv 30 manics, lv 60 normals) by which point, normal King Dragon (12,800 damage) has even taken a slight lead over Manic King Dragon (12,760 damage).

After this however, the soft cap for normals kicks in, and the manics take a slight lead again, but the ratio stays mostly flat until x = 50 where MKD has 17,160 damage, and normal KD has 16,800 damage.

Essentially, it is true at high levels that manics are comparable to double level normals. At lower levels, they are better than double level normals, and for a brief moment around Lv 30 manics/Lv 60 normals, you might even see the normals pull ahead a tiny bit.

>The exact shape of this curve varies slightly for each normal-manic pair. If you are interested, try to recreate this for Manic Eraser vs Eraser HP, or some other pair of your liking. Do all manics get slightly worse than double-level normals around x = 30, or are some of them always better?

# Conclusions

Levelling up cats is one of the most central and important parts of the game, but it is rarely understood in detail. A simple "higher is better" interpretation works for most purposes, but for your curiosity, or to optimize your late game decisions about where to invest your resources, a deeper mathematical treatment as detailed above may prove useful.

Corrections, questions and suggestions for future topics to cover are welcome can be pinged or DM’d to the original author (@ThanksFëanor#3087) at any time, or he can be found on [the r/battlecats discord server](https://discord.gg/battlecats).

# Thanks for reading!

Like was mentioned earlier, there exists a condensed version of this guide, which focuses on attack timings.

You may also want to refer to the terminology guide.

# Credits

**dkafsgdhh**#0809 (original guide redaction)

**Waran-Ess**#9801 (minor edits and web conversion)