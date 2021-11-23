# Indie-Toolbox
Tool Launcher and List of tools for the Indie Toolbox

## Tool Restrictions

### Visual Restrictions (If your Tool has a Visual User Interface)
-   Tools must enable changing the theme according to a standardized theme format. (See: [[Theme File Spec]])

-   Some standard action keys should be: `Ctrl+C` for copy, `Ctrl+V` for paste, `Ctrl+X` for cut, `Ctrl+Z` for undo and `Ctrl+Y` for redo - if their underlying functions are used in the program

### CLI Restrictions
-   CLI must offer a help command, preferable as `--help` and `-?`

-   Tools must be startable via CLI and offer opening files doing so, if the Tool has a related file format (e.g. `tool “path/to/file.ext”`)

### Systematic Restrictions
-   Tools must offer a public build/release
-   Tools must be offered/released under MIT license

### Theme File Spec
-   File Consists of key: value pairs separated by newlines…. yes. that’s it
    
-   Colors specified with `0xRRGGBBAA`
    
-   The Currently Specified Keys are:  
```yaml
background1: 0xRRGGBBAA
background2: 0xRRGGBBAA
background3: 0xRRGGBBAA
foreground1: 0xRRGGBBAA
foreground2: 0xRRGGBBAA
foreground3: 0xRRGGBBAA
accent1: 0xRRGGBBAA
accent2: 0xRRGGBBAA
accent3: 0xRRGGBBAA

font: fontname
font-size: size
```
-   Those 9 colors are required by definition and can be expected as a tool developer.
    
-   If your tool requires more colors you can ofc require them for your specific tool, though you should tell the user about it and name it like so `background[number]` - for foreground and accent accordingly.

## Contribution
-   To contribute to this doc, head on over to the [discord](https://discord.gg/GGYgsszjka) and give us your suggestion in [#toolbox-development](https://discord.com/channels/786048655596847106/911662025858506762)
