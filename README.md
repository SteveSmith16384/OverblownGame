# OVERBLOWN

This is a split-screen multiplayer FPS with physics.  Originally this was an Overwatch spoof I created as an attempt to get my kids to play one of my games, so I copied ideas from their favourite games, including Overwatch and Piggy (/Roblox).  However, I've slowly added more and more game modes to it, so there's a bit of everything.

A release can be downloaded from https://stephensmith.itch.io/overblown

Gameplay video: https://www.youtube.com/watch?v=3LQdwPbTgmQ

For further instructions, see the download page at https://stephensmith.itch.io/overblown


## Features
* Multiple heroes with different abilities and ultimates.
* Multiple game modes
* Multiple maps


## Controls

### Mouse/keyboard
* W, A, S, D - Forwards, backwards, strafe left, strafe right
* LMB - Shoot
* RMB - Secondary ability
* Space - Jump
* R - Reload
* U - Ultimate
* F1 - Toggle full-screen
* F2 - Toggle windowed full-screen (useful for recording video).
* Esc - Exit


### Tested with PS4 controllers
* R1 - Shoot
* L1 - Secondary Ability
* X - Jump
* Square - Reload
* Triangle - Ultimate


## Modifying the Game
You can modify a few game settings by editing the file "game.cfg".


## Map Editor
The map editor is still in development, and will only work properly by running the game from source (so the data files can be written back).

To edit the available texures, manually edit the map json file.

### Map Editor Controls
* 1 - Save map
* 2 - "Settle" position of selected entity
* 3 - Show our position
* 4 - Select All Blocks mode
* 5 - Show physics debug
* 8 - Add dynamic model - You need to edit the code to specify the filename
* 9 - Add static model - You need to edit the code to specify the filename
* 0 - Reset rotation
* B - Size mode
* C - Clone current block
* G - Enable physics/gravity
* M - Mass mode - Use arrow keys to change mass
* N - New block - then bress B, C or E for Box, Cylinder or Sphere.
* P - Position Mode
* R - Rotation Mode
* T - Texture mode
* U - Undo move
* X - Remove block - press shift to confirm!
* Arrows & Pg Up/Pg Dn: Adjust model size/position/rotation/mass/texture depending on mode
* Shift - only adjust one side of object
* Ctrl - Move in very small increments


## Notes for other Developers
* Development branch is the cutting edge but possibly buggy/broken.  Master is the most stable but out of date.
* Gradle is a real pain to work with.  However, if you have trouble loading this project, I used Gradle v4.10.3.
* The file Settings.java contains various settings that determine what game mode the game starts in.


## Become a Patron!
Well, it's worth a try: https://www.patreon.com/bePatron?u=3406199


## Licence
This project uses the MIT licence.  See LICENCE.txt.


## Credits
* Design and programming Stephen Carlyle-Smith (stephen.carlylesmith@googlemail.com, @stephencsmith)
* Post-processing library: https://github.com/crashinvaders/gdx-vfx
* Controller code: https://github.com/electronstudio/sdl2gdx/
* Vox loading code from https://github.com/Lignum/JVox

### Assets Credits
* Music by Emma Andersson, taken from https://opengameart.org/content/megasong
* Alien model by Quaternius (https://www.patreon.com/quaternius)
* Racer blink sfx by Iwan Gabovitch, taken from https://opengameart.org/content/racing-speed-boost-sound
* Shooting sfx by Michel Baradari, taken from https://opengameart.org/content/4-projectile-launches
* Bowling Ball jump sound taken from https://opengameart.org/content/jump-sounds
* Racer's bomb beep taken from https://opengameart.org/content/7-space-sounds
* Cackle taken from https://opengameart.org/content/so-thats-coming-along
* Explosions and clang taken from https://opengameart.org/content/metal-clang-explosions-zing
* Death sounds taken from https://opengameart.org/content/grunts-male-death-and-pain
* Health collected sfx taken from https://opengameart.org/content/picked-coin-echo-2
* Voxel textures taken from https://opengameart.org/content/16-%C3%97-16-pixel-art-textures

