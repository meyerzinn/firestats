# firestats
Using Firebase, we created a live stats plugin.

This plugin for Bukkit/Spigot is one of a series of FirePlugins. Our plugins are on fire! Basically, we wrote some plugins such as FireChat (and more to be released soon) to show off the power of Firebase as a serious database. Firebase excells in Live Data, a futuristic concept where i = i, i does not equal some previous unsynced version of i, and all implementations of i change if i is chaned in any implementation. In web development, we use a term called "two-way data binding," which is where a variable in the DOM is reflected in the code. Changing it programatically or through an input changed it globally. Firebase adds "three-way data binding," where not only is it bound programatically and in the DOM, but also in a database, so that any client that updates it updates it for every client. We used that to our advantage in making this and other plugins.

## What does it do?

FireStats is a PVP stats tracker with no in-game interface. Instead, it streams data to a web-based interface, which is how players could access it. Many stats tracking plugins have this feature, but FireStats (thanks to Firebase) is fully atomic (meaning there cannot be a [race condition](https://en.wikipedia.org/wiki/Race_condition)) and doesn't have to resync every minute. It's advantages:

* Atomic. You can run it on as many servers as you want and a player's stats will be consistent.
* Fast. FireStats is likely the only stats plugin that can claim that within .5 seconds of a kill, the player's stats are reflected in the database. Another .5 seconds later and everyone viewing it online will see the change.
* Light. FireStats is comprised of three main classes. Most stats plugin have three classes just for the databases. It has an extremely low overhead because all storage is hosted in Firebase. The stats are not stored on the machine at all, so you can scale to thousands of players and not have to bat an eye at the server specs (... at least not for this plugin to function).
* Customizable. It doesn't take an advanced degree to pick up on Firebase and how it works, and once you do, all data is stored in a very obvious way in the server, it's properly labeled, and self explanatory. You should have no trouble finding your way around our data storage. In case you do, just check the wiki for a guide to the storage methods.
* NAPIN'. It stands for "no api necessary" because in order to customize your server you don't need to listen to our API. We even make it easier for you to add modules, such as a scoreboard with the stats (in progress) by throwing a StatsChangedEvent event when someone's stats change. That way your plugin can stay on top of that without overhead. Even without this event, your plugin could listen to Firebase server events. It's really easy, actually.

## Upcoming

We are planning plenty of features, such as:
* A scoreboard module,
* a tab bar module,
* a chat command module,
* a hosted service,
* and if you have any ideas just ask!

## Where do I get it?

Once we finish debugging the plugin, it will be available on BukkitDev. For now, developers are free to compile the code and mess around with it. 

## What about this webfront?

There is a template webfront to show off how simple this really is. It should be live here when I get around to it :P.

## Big thanks.

Huuuuge thanks to TheGrizz, who is helping with the other FirePlugins.
