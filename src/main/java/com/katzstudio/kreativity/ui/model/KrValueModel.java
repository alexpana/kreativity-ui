package com.katzstudio.kreativity.ui.model;

/**
 * Generic model used by Kreativity components. It contains callbacks for edit begin / end so implementations
 * can implement undo / redo while skipping intermediate changes (such as dragging a spinner or entering characters
 * in a text field).
 */
public interface KrValueModel<T> {

    /**
     * Returns the current value of the model.
     *
     * @return the model's value
     */
    T getValue();

    /**
     * Sets the value of the model. This can be an ephemeral intermediate change.
     *
     * @param value the new value
     */
    void setValue(T value);

    /**
     * Called before the ui component starts making ephemeral changes to the model.
     */
    void ephemeralChangesBegin();

    /**
     * Called after the ui component stops making ephemeral changes to the model.
     */
    void ephemeralChangesEnd();

    class KrAbstractValueModel<T> implements KrValueModel<T> {

        public T value;

        public KrAbstractValueModel(T initialValue) {
            value = initialValue;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public void ephemeralChangesBegin() {
        }

        @Override
        public void ephemeralChangesEnd() {
        }
    }
}
