![](https://github.com/Indie-Toolbox/Indie-Toolbox/blob/main/ToolBoxIcon3_400.png?raw=true)
# Indie-Toolbox
Tool Launcher and list of tools for the Indie Toolbox

## Tool Restrictions

### ![](https://github.com/Indie-Toolbox/Indie-Toolbox/blob/main/ToolBoxIcon3.png?raw=true) Visual Restrictions (If your Tool has a Visual User Interface)
-   Tools must enable changing the theme according to a standardized theme format. (See: [[Theme File Spec]])

-   Some standard action keys should be: `Ctrl+C` for copy, `Ctrl+V` for paste, `Ctrl+X` for cut, `Ctrl+Z` for undo and `Ctrl+Y` for redo - if their underlying functions are used in the program

### ![](https://github.com/Indie-Toolbox/Indie-Toolbox/blob/main/ToolBoxIcon3.png?raw=true) CLI Restrictions
-   CLI must offer a help command, preferably as `--help` and `-?`

-   Tools must be startable via CLI and offer opening files doing so, if the Tool has a related file format (e.g. `tool “path/to/file.ext”`)

-   Opening a tool with `--theme=”path/to/file”` should open the tool with the given theme file

### ![](https://github.com/Indie-Toolbox/Indie-Toolbox/blob/main/ToolBoxIcon3.png?raw=true) Systematic Restrictions
-   Tools must offer a public build/release
-   Tools must be offered/released under MIT license

### ![](https://github.com/Indie-Toolbox/Indie-Toolbox/blob/main/ToolBoxIcon3.png?raw=true) Theme File Spec
-   File consists of `key:value pairs` separated by newlines in any order…. yes. that’s it
    
-   Colors are specified with `0xRRGGBBAA`
    
-   The currently required keys are:  
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

## ![](https://github.com/Indie-Toolbox/Indie-Toolbox/blob/main/ToolBoxIcon3.png?raw=true) Contribution
-   To contribute to this doc, head on over to the [discord](https://discord.gg/GGYgsszjka) and give us your suggestion in [#toolbox-development](https://discord.com/channels/786048655596847106/911662025858506762)
