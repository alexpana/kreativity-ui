Performance guidelines
----------------------

Although java is pretty fast at allocating memory, in a highly interactive, real-time system
where each frame counts, minor GCs can hurt. At 60 frames per second (0.016ms / frame), one
minor GC of 0.007ms takes 42% of the frame. If objects are constantly allocated each frame,
for each widget, the number of allocations grows very fast.

The current allocation speed target is: 1k/s (meaning we allocate 1kb of garbage each second).
This should ensure we're only getting minor GCs every ~10 minutes.

### Allocation rules

- Never, ever, ever allocate objects in the `update` methods.
- When absolutely necessary, use an object pool to allocate objects in the `update` methods,
and release them before returning from the method. Using fields for temporary values is
encouraged.
- Don't use lambdas in the `update` methods. They generate A LOT of junk.
- Don't iterate using for each in the `update` methods (avoids the allocation of iterators).
- When arrays are needed create one array, and empty it each frame.
Emptying the array doesn't change its capacity, which avoids allocations.
- Overload methods which require data objects with methods that take as arguments
all the fields of the data object. The first method should delegate to the second.
Always prefer to use the second version.
- Cache results as much as possible (font metrics, formatted text, etc.)
- When returning object values, consider adding a mutable parameter of the return type, and
update the parameter inside the method. The method can return the mutated parameter instead
of creating a new object.