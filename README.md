Consoled is a CraftBukkit plugin which makes adding commands easy.

Table of Contents
------
1. [Usage](#usage)
    - [writing `commands.yml`](#writing-commandsyml)
    - [writing executors](#writing-command-executors)
    - [registering](#registering)
2. [Compiling](#compiling)
3. [Contributions](#contributions)

Usage
------
1. Import the jar archive.
2. Write [`commands.yml`] and embed it.
3. Write your own command executors.
4. Register your command executors.

### Writing commands.yml
You can specify usages, permissions for commands.<br>
_All properties are optional._

| Property | Description |
| :---: | --- |
| `_usage` | Displayed when the command fails. (executor returns false)<br>Can inherit from the parent. |
| `_perm` | Represents the permission required to execute the command. |
| `_permMessage` | Displayed when the command fails due to the lack of permission.<br>Can inherit from the parent. |
| Others | Other properties are regarded as subcommands. |

#### Example
```yaml
foo:
  _usage: /foo [bar/baz]
  _permMessage: "&cYou don't have enough permission."
  bar:
    _usage: /foo bar [number]
    _perm: foo.bar
  baz:
    _usage: /foo baz <name>
    _perm: foo.baz
```

### Writing command executors
Annotation `@CommandFor(command)` can be used to delegate execution of the command to the method. For subcommands use `.`.<br>
Attached methods should return `boolean` (`true` for success, `false` for failure) and receive `CommandContext` as the first argument.<br>
Annotation `@Optional` can be used to mark the parameter as an optional argument. **It should not be primitive.**

#### Example
```java
public class ExampleExecutor {
    @CommandFor("foo.bar")
    public boolean executeBar(CommandContext ctx, int number) {
        // omitted
    }
    @CommandFor("foo.baz")
    public boolean executeBaz(CommandContext ctx, @Optional String name) {
        // omitted
    }   
}
```

### Registering
Consoled uses [Services API](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/plugin/ServicesManager.html) to provide its functions.<br>
Following code gets a `CommandService` instance to register commands to.
```java
var registration = Bukkit.getServiceManager().getRegistration(CommandService.class);
CommandService service = registration.getProvider();
```
Then you can register all commands in your `plugin.yml` and executors with:
```java
ExamplePlugin plugin;
ExampleExecutor executor;
service.register(plugin);
service.registerExecutor(plugin, executor);
```

Compiling
------
### Windows
`gradlew jar`
### Linux, BSD, or Mac OS X
`./gradlew jar`

You can find `consoled-version.jar` in `build/libs`.

Contributions
------
Consoled welcomes all contributions! Feel free to contribute with PRs after enough tests done.
