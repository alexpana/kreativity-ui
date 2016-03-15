[![Build Status](https://travis-ci.org/alexpana/kreativity-ui.svg?branch=master)](https://travis-ci.org/alexpana/kreativity-ui)

Kreativity UI (under heavy development)
=======================================

UI framework built on top of libgdx. It focuses on elegant design and extensibility. Inspired by Qt. 

Frequently Asked Questions
==========================
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
