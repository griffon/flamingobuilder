package griffon.builder.flamingo.factory

import griffon.builder.flamingo.impl.MutableRibbonContextualTaskGroup
import org.pushingpixels.flamingo.api.ribbon.JRibbon
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup

import java.awt.Color

/**
 * @author Andres Almiray
 */
class RibbonContextualTaskGroupFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value instanceof RibbonContextualTaskGroup) {
            return value
        }

        if (value instanceof String || value instanceof GString) {
            value = value.toString()
        } else if (attributes.containsKey('title')) {
            value = value.toString()
        } else {
            throw new RuntimeException("In $name either value is be a String or title: must be defined.")
        }
        Color color = attributes.remove('hueColor')
        if (!color) {
            throw new RuntimeException("In $name a value for hueColor: must be defined.")
        }

        return new MutableRibbonContextualTaskGroup(value, color)
    }

    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof JRibbon || parent instanceof JRibbonFrame) {
            def ribbon = parent instanceof JRibbon ? parent : parent.ribbon
            parent.addRibbonContextualTaskGroup(child)
        }
    }
}