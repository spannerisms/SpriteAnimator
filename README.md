# SpriteAnimator

[SpriteAnimator](https://github.com/fatmanspanda/SpriteAnimator/wiki/Sprite-Animator) is a program designed to help debug the appearance of a sprite as it's being created. It uses information based off of Link's animations in game to recreate every animation with an imported sprite. It is downloadable as a runnable `JAR` file from [the super project's release page](https://github.com/fatmanspanda/ALttPNG/releases).

## Loading sprites
* `ZSPR` - Uses the preferred sprite format to create the necessary images.
  * SpriteAnimator 1.13 and newer includes a folder of every sprite live on the randomizer site.
* `SFC` - Extracts sprite and palette data from a ROM file.
* `PNG` - Uses an assembled image for previews. When using `PNG` files, the application will not attempt to apply mail or glove palette changes.

## Display features

Along with animating sprites, SpriteAnimator also includes various features to control the display:
* Backgrounds to test how a sprite looks in various areas.
  * Character sprites can be moved against the background by clicking or dragging your mouse pointer. (v1.5+)
* The ability to view animations with a sword or shield of any level.
* The ability to view character sprites in any of the 4 mail palettes.
* The ability to toggle shadows.
* The ability to toggle sprites that don't fall under the above categories. (*Note: Swag duck cannot be turned off.*)
* The ability to toggle neutral poses (default standing position) to assess how smoothly a sprite moves into an animation.
* The ability to zoom in on sprites, up to 500%.
* The ability to speed up or slow down animations.
* The ability to view animations step-by-step, which also includes a table of what sprite sheet cells are used and how they are transformed.
* The ability to play an animation exactly once.

## Other features

* Sprite look up chart
  * Includes 3 display options:
    1. Vanilla Link
    1. Named indices
    1. Green mail of currently loaded sprite
* Animated GIF export
  * *Note: the GIF specification only allows for a minimum delay time of 10ms; as such, exported GIF files will run at incorrect speeds.*
* Tracker images export for [Crossproduct's tracker](https://github.com/crossproduct42/alttprandohelper/releases)
* Collage export, showing every animation step once.

## Terminology used in this project
Some terms we need to use are overloaded (they can mean multiple things). To avoid confusion, here's a brief glossary of what means what when we say it. Not everything is perfectly written, so feel free to ask questions if something doesn't seem to mean what you think.
* **Frame** - A repaint cycle of the SNES.
  * *The SNES runs at 60 **frames** per second.*
* **Step** - A distinct section of a full animation. Steps last for a variable number of frames. *Note: The same animation can have a variable number of steps, as identical steps will be merged when possible; see notes below.*
  * ***Step 1** of the hammer animation shows Link holding the hammer in the air for 3 frames.*
* **Sprite** - A specific entity or subentity's graphic.
  * *Link's attack animation includes a **sprite** for the sword.*
  * *Link himself is normally composed of 2 **sprites**: a head and a body.*
* **Cell** - A specific subentity of a full character sprite, defined by strict bounds.
  * *Link's right-facing head is the first **cell** of his sprite sheet.*
* **Index** (*pl. indices*) - An alphanumerical name for a location on the character sprite sheet. *see: [Mike's chart](http://alttp.mymm1.com/sprites/sheets/?sprite=link&skin=green)*
  * *Link's dungeon map icon is **index** K7 on his sprite sheet.*

## Notes about animation decisions
* SpriteAnimator tries its best to emulate the timing of the SNES (60 FPS); however, it is slightly slower with its repaint cycles. 1/60 of a second is approximately 16.66ms. The closest this program can achieve without massive overhead is 17ms.
* Most left-facing animations are simply a mirror of the right-facing animation, and as such have been omitted.
* Every bug net animation uses the same poses, but in a different order. As such, only the right-facing swing was included.
* Miscellaneous sprites were included to make sure held items could be held as best as possible; however, other effects (such as dust clouds and sparkles) were omitted to decrease the workload in creating a finished product.
* For end-user control, consecutive identical animation steps are merged. For example: when neutral poses are off, attack right has 9 steps with a sword and 7 steps without. This is because in step pairs [1,2] and [8,9], Link does not change his pose, but the sword still moves.
* The character sprite in the zap animation disappears every other frame. This is to emulate Link's invincibility flicker, which the animation is never seen without.
* Ether is a very long animation because there are palette swaps every 4 frames in Link's final pose.
* For categorization, neutral poses are not considered.

## Animation oddities that are not errors
The following apparent errors are problems directly with the game itself, and not this program. They are best seen with the vanilla Link sprite. Unless otherwise stated, animation steps referenced have no equipment displayed and no neutral poses (Sword level: Off; Shield level: Off; Misc. sprites: Off; Neutral: Off).
* Stair-climbing can vary by location. The stair-climbing animations in this program were researched from Blind's hut in Kakariko Village. (Oddity discovered by Osaka)
* The bunny walk animations seem fast. Movement is apparently faster when bunny form is activated by a spinner. (Oddity discovered by Glan)
* Hookshot right has no shield, even though hookshot up and hookshot down do.
* In attack right, consecutive steps 5, 6, 7 have alternating overlap between Link's arm and Link's head.
* In attack up, Link's head goes particularly high above his body on step 4.
* Quake does not have the smoothest movement in the jump and fall.
* The sword on the final step (43) of quake is cut off. This was probably done in vanilla to make it appear to be in the ground.
* The sprites for the shovel are very small.
* Index `AB7`, a normally blank cell, appears on steps 5 and 10 of tree pull.
* Link's head becomes detached from his body (empty space visible) in the following animations:
  * Attack (step 3)
  * Swim (step 4)
  * Grab down (steps 2, 3, 4)
  * Push down (all steps)
  * Ether (steps 10–47)

## Known bugs/issues
* Very rarely, the animation process will enter a hyperspeed mode. The problem appears to be a thread conflict that comes down to frame-perfect timing, making it difficult to reproduce. This error is fixed by pressing the reset button.
* Tooltips have a lifetime of only 596 hours, after which you must move your mouse out-then-in to refresh them.

## Animation data
The bulk of the groundwork for this program was researched by Mike Trethewey and TWRoxas, available [here](http://alttp.mymm1.com/sprites/includes/animations.txt). From there, individual animations were adjusted and researched frame-by-frame, with some help from RyuTech.

As of v1.5, animation data is stored in a `json` format, free for anyone to use. It is only asked that you credit the research time devoted here for that data.

JSON file:
* [/resources/AnimationData.json](https://github.com/fatmanspanda/SpriteAnimator/blob/master/src/main/resources/AnimationData.json)

Definitions:
* Key `row` in `sprite` array objects: [/Database/SheetRow.java](https://github.com/fatmanspanda/SpriteAnimator/tree/master/src/main/java/animator/database/SheetRow.java)
* Key `size` in `sprite` array objects: [/Database/DrawSize.java](https://github.com/fatmanspanda/SpriteAnimator/tree/master/src/main/java/animator/database/DrawSize.java)
* Key `trans` in `sprite` array objects: [/Database/Transformation.java](https://github.com/fatmanspanda/SpriteAnimator/tree/master/src/main/java/animator/database/Transformation.java)
* Key `shadow` in `steps` objects: [/Database/Shadow.java](https://github.com/fatmanspanda/SpriteAnimator/tree/master/src/main/java/animator/database/Shadow.java)

Resources:
* Item sprites: [/resources/images/equipment.png](https://github.com/fatmanspanda/SpriteAnimator/blob/master/src/main/resources/images/equipment.png)
* Zap palette colors: `static final byte[][] ZAP_PALETTE` (signed bytes) in [SpriteManipulator/SpriteManipulator.java](https://github.com/fatmanspanda/SpriteManipulator/blob/master/src/main/java/spritemanipulator/SpriteManipulator.java)

# Dependencies
* [Sean Leary's `org.json` library](https://github.com/stleary/JSON-java)
* [Dragon66's `AnimatedGIFWriter`](https://github.com/dragon66/animated-gif-writer/blob/master/src/com/github/dragon66/AnimatedGIFWriter.java)
