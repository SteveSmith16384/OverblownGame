# OVERBLOWN

An Overwatch clone split-screen multiplayer FPS with physics.  Designed and programmed by Stephen Carlyle-Smith (stephen.carlylesmith@googlemail.com).

Gameplay videos: https://www.youtube.com/watch?v=l5r8nlscKwI&list=PLbGkfhhJ5G3-74KwOzu8khhYF_4Zb0pPF


## Development Notes

* In Settings.java, set RELEASE_MODE = true to disable all debugging & testing settings.


## Heroes

* Phartah - Has a rocket launcher and jetpac.
* Boomfist - Can punch enemies away
* Bowling Ball - Can roll around knocking enemies away.

## Game Modes
* Factory - Death Match
* Village - Control Point


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
The map editor is still in development.  To use it, edit the file Settings.java and set "USE_MAP_EDITOR" to "true".  The map editor will always edit the file "core/assets/maps/map_editor.json", so rename whatever map file you want to edit to that name.


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


## Credits
* Design and programming Stephen Carlyle-Smith (stephen.carlylesmith@googlemail.com, @stephencsmith)
* Alien model by Quaternius (https://www.patreon.com/quaternius)
* Music by Emma Andersson, taken from https://opengameart.org/content/megasong
* Post-processing library: https://github.com/crashinvaders/gdx-vfx
* Voice talent by my kids.
* Racer blink sfx by Iwan Gabovitch (qubodup@gmail.com) taken from https://opengameart.org/content/racing-speed-boost-sound
* Shooting sfx by Michel Baradari taken from https://opengameart.org/content/4-projectile-launches
* Bowling Ball jump sound taken from https://opengameart.org/content/jump-sounds
* Racer's bomb beep taken from https://opengameart.org/content/7-space-sounds