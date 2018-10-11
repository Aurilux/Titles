# Titles
Inspired by iChun's Hats, Titles allows player to display titles earned by completing advancements

# For Modmakers
### Step 1
If you want to add your own titles, send an IMC message with the key "register-titles" and the value being a string that contains all the resource locations of the advancements delineated by a ";".
```
FMLInterModComms.sendMessage("titles", "register-titles", "testmod:root; testmod:adv1; testmod:adv2");
```

### Step 2
Then you need to add certain entries to your .lang file(s). Using vanilla's advancements as an example, Titles adds the title "the Craftsman" to Minecraft's root advancement called "Minecraft". That advancement's resource location is: minecraft:story/root. In my .lang file I have:
```
title.minecraft.story.root=the Craftsman
```
