[![Build Status](https://travis-ci.org/alexpana/kreativity-ui.svg?branch=master)](https://travis-ci.org/alexpana/kreativity-ui)

# Kreativity UI (under heavy development)

UI framework built on top of libgdx. It focuses on elegant design and extensibility. Inspired by Qt. 

![Screenshot showcasing available widgets](https://raw.githubusercontent.com/alexpana/kreativity-ui/master/screenshot.png)

## Frequently Asked Questions
**Q: What is this?**

A: This is a UI framework written in java on top of libgdx (opengl). It's similar to swing or javafx but works with libgdx.


**Q: Why not use swing / javafx?**

A: These UI frameworks are mature and complete, but they work with the native resources of the operating system. They
manage the entire window and have internal renderers on top of the operating system to display the UI. **Kreativity-Ui**
is built to work inside an opengl context, where swing / javafx components cannot be used.


**Q: Why not scene2d / other similar frameworks?**

A: This framework was built to be very similar to existing frameworks (swing, qt, etc.). This means that layouts and
new components can be created very easily and work as expected. New widgets are added with every release and we currently
have ~300 unit tests. Plans include support for tree views, tables and tabbed panels, which aren't supported by any other
framework, and cannot be easily implemented in those frameworks.


**Q: Why prefix all classes with Kr prefix? That's so yesterday.**

A: It's lame, I know. Nobody uses prefixes anymore. Or do they? Whether for legacy reasons or otherwise Qt, Unreal Engine,
java's Swing all use prefixes. The reason I'm using them is because of autocomplete. If I didn't use a prefix, my trusty 
IDE would fill my autocomplete list with components from other frameworks and hinder my development by just a tiny bit 
(just like it does with List because libgdx also implements a list class). So yeah, that's the reason.

## Change Log

#### [Unreleased]
- Added support for animations
- Added support for widget opacity
- Added opacity animation
- Added delay + opacity animation for tooltips
- Custom tooltips can be forced to show despite the currently hovered widget.

#### [0.2.0] - 2016-03-27
- Migrated to libgdx 1.9.1 and backend lwjgl3
- Added support for system cursors (arrow, hand, beam, crosshair, h/w resize)
- Moved resource files inside jar for easier distribution
- Added support for widget visibility
- Added support for rendering rounded rectangles (radius from 2 to 6)
- Added overlay panel to canvas. This panel is used to host popups and tooltips.
- The update method is now properly called for every widget in the scene
- Added setters and getters for the background and foreground. Restructured the styles.
- Added card layout which only displays one of the child widgets, resized to match the parent.
- Added support for tooltips
- Abstracted libgdx, so different backends can be implemented
- Added KrTimer and support for registering update listeners

#### [0.1.0] - 2016-03-20
- Added KrListView together with a Qt inspired abstract item model and selection model
- Added model support for all edit widgets
- Added style for KrSplitPanel
- Added KrStackLayout
- Added KrOrientation which replaces specialized orientation enums
- Added support for double click events
- Added ctrl / alt / shift down flags to mouse events
