# SubPlugins
A plugin loader that runs its own plugins without giving direct access to precious server resources.

## Why should I write plugins for this?
 - Natively more secure and catches stray code before it can do any harm
 - Comes with classes and helpers to make writing plugins easier
 - Lots of stuff is doing using tasks and schedules to make the server run better
 
## Design choices
 - The loading process is very similar to Bukkit but with changes to make it more secure and run in the background
 - The structure is kind-of similar to that of Bukkit to make the learning curve a lot less steep
 - Commands are what we call "Hash Commands" (for example instead of /version, #version)
    - Designing commands are easier
        - Tab completing is easier to understand
    - The system is made so plugins have a harder time overwriting the executor set by other commands.    
    