# OVERBLOWN

An Overwatch clone split-screen multiplayer FPS with physics.  I created this as an attempt to get my kids to play one of my games, so I've copied ideas from their favourite games, including Overwatch and Piggy (/Roblox).


Gameplay videos: https://www.youtube.com/watch?v=l5r8nlscKwI&list=PLbGkfhhJ5G3-74KwOzu8khhYF_4Zb0pPF


## Features
* Multiple heroes with different abilities and ultimates.
* Multiple game modes


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


## Map Editor
The map editor is still in development.  The map editor will always edit the file "core/assets/maps/map_editor.json", so rename whatever map file you want to edit to that name.

To edit the texures used, manually edit the map json file.

### Map Editor Controls
* 1 - Save map
* 2 - "Settle" position of selected entity
* 3 - Show our position
* 5 - Show physics debug
* 0 - Reset rotation
* B - Size mode
* C - Clone current block
* M - Mass mode - Use arrow keys to change mass
* N - New block - then bress B, C or E for Box, Cylinder or Sphere.
* P - Position Mode
* R - Rotation Mode
* G - Enable physics/gravity
* U - Undo move
* X - Remove block - press shift to confirm!
* Arrows & Pg Up/Pg Dn: Adjust model size/position/rotation/mass/texture depending on mode


## Notes for other Developers
* Development branch is the cutting edge but possibly broken branch.  Master is the most stable but out of date.
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
* Music by Emma Andersson, taken from https://opengameart.org/content/megasong
* Voice talent by my kids.
* Alien model by Quaternius (https://www.patreon.com/quaternius)
* Racer blink sfx by Iwan Gabovitch, taken from https://opengameart.org/content/racing-speed-boost-sound
* Shooting sfx by Michel Baradari, taken from https://opengameart.org/content/4-projectile-launches
* Bowling Ball jump sound taken from https://opengameart.org/content/jump-sounds
* Racer's bomb beep taken from https://opengameart.org/content/7-space-sounds
8 Cackle taken from https://opengameart.org/content/so-thats-coming-along

