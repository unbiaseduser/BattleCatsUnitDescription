# Introduction

Huge thanks to dkaf for writing the original document on attacks; this condensed version is mostly based on his work. I highly recommend you give it a read if you want to understand everything better.

If you look at any cat or enemy unit in battle, you can see them attack. In fact, the process of attacking is a cycle that repeats itself continuously. This cycle is broken up into several key steps, which are:

-   Foreswing
-   Attack happens
-   Backswing
-   Time between attacks (TBA)

These steps describe how attacking happens visually. However, the system doesn’t work exactly like that.

The proper attack cycle is:

<img src="file:///android_asset/img/guide_support_img/atk_timing/atk_cycle.png" width="100%"/><br>

What is **Attack Cooldown?** It’s the time between the actual attack and the start of the next attack animation. In reality, backswing and TBA don’t dictate the attack cycle. Attack cooldown, on the other hand, is obeyed all the time. Backswing and TBA added together always have a value equal to or greater than the attack cooldown.

>This might be a bit complicated to understand at first; however, for most cat units, this is very simple. This guide will start by describing how the system works most of the time, then move on to special cases.

# How Most Units Attack

We’ll use **Crazed Bahamut** as an example. Here are its stats:

**Foreswing** : 121f (4.03s)  
**Backswing** : 30f (1s)  
**Attack cooldown** : 479f (15.97s)  
**Attack rate** : 600f (20s)

You might have noticed that I omitted TBA, and that’s because it does not actually exist as a stat; We’ll come back to it later.

Now for the actual steps of the attack cycle:

-   Crazed Bahamut starts his attack animation, charging his energy ball. This is his foreswing. If Bahamut gets interrupted during his foreswing, his attack is reset.
-   After this, damage is dealt. This marks the end of Bahamut’s foreswing and the start of his backswing. Most importantly, on this frame, attack cooldown begins counting down.
-   Bahamut is now in TBA (which is really just a fancy term to describe the period of the attack cycle where units are idle and / or move ahead on the battlefield) and stays so until the attack cooldown expires.

Attack cooldown functions independently from backswing. This is why if you interrupt Bahamut during his backswing (or on the very frame damage is dealt), he will instantly go into TBA, skipping the rest of his backswing. Attack cooldown cannot be interrupted once it starts counting down, therefore the attack rate is unchanged even if Bahamut’s animation is interrupted early, as long as damage was dealt.

This is also apparent when looking at the stats. `Foreswing + attack cooldown = attack rate (121f + 479f = 600f)`. Backswing is absent from the equation: as stated previously, backswing works independently from other stats and does not affect any in-game calculations.

This is how most units in the game attack: Bahamut, Eraser, Dragon, Amaterasu, etc. However, as with anything, there are exceptions.

# The Rebound Mechanic

**Maglev Cat** will be our unit example for this next concept.

**Foreswing** : 1f (0.03s)  
**Backswing** : 200f (6.67s)  
**Attack cooldown** : 0f (0s)  
**Attack rate** : 201f (6.7s)

Maglev is an easy example of a unit with what is generally referred to as a rebound mechanic. As you can see from its stats, Maglev has instant foreswing, substantial backswing and an attack cooldown of zero. As explained earlier, when a unit’s animation gets interrupted, it skips backswing and goes straight to the idling phase of the attack cycle, or TBA. However, since Maglev has no delay between its attacks, interrupting his backswing will instantly reset the cycle, and he will rush forward to attack again.

In other words, Maglev’s backswing is longer than its attack cooldown, meaning that the latter does not dictate his attack rate under normal circumstances (but the former does); this makes it so his actual attack interval is variable.

# Multi-hit

We’ll use **Iron Claw X** for this one.

**Foreswing** : 11f / 15f / 35f (0.37s / 0.5s / 1.17s)  
**Backswing** : 26f (0.87s)  
**Attack cooldown** : 145f (4.83s)  
**Attack rate** : 180f (6s)

Multi-hit units function a bit differently, but aren’t very complicated to figure out when you know how attacks work generally. As the name implies, multiple hits are spread out over the length of the attack animation. However, attack cooldown only starts counting down on the last hit. Therefore, if Iron Claw X gets interrupted mid-animation without landing his third and final hit, he is still technically in the foreswing phase, and the attack is reset, regardless of whether he landed his first two hits or not.

>## Bonus: test your knowledge
>Explain what is happening in [this clip](https://youtu.be/VEYqXcLq_E4&t=56s).

# Terminology

**Foreswing / Attack Occurrence**  
Time between the start of the attack animation and the frame on which damage is dealt.

**Attack Cooldown**  
Time between the frame on which damage is dealt and the start of the next attack animation.

**Backswing**  
Time between the frame on which damage is dealt and the end of the attack animation.

**Time Between Attacks (TBA)**  
Time between the end of the attack animation and the start of the next attack animation.

**Attack Rate**  
Time between the frame on which the attack occurs and the frame on which the next attack occurs. Used to calculate theoretical DPS (Damage Per Second).

# Thanks for reading!

 For a more complete terminology guide, make sure to check out [this document](app://sixty.nine/terminology).

You can also read dkaf's original guide on attacks [here](app://sixty.nine/everything_atk). 

# Credits

**Waran-Ess**#9801 (original guide redaction and web conversion) 