package griffon.builder.flamingo.factory

import griffon.builder.flamingo.impl.MutableRibbonContextualTaskGroup
import griffon.builder.flamingo.impl.MutableRibbonTask
import org.pushingpixels.flamingo.api.ribbon.JRibbon
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame
import org.pushingpixels.flamingo.api.ribbon.RibbonTask

/**
 * @author Andres Almiray
 */
class RibbonTaskFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value instanceof RibbonTask) {
            return value
        }

        if (value instanceof String || value instanceof GString) {
            value = value.toString()
        } else if (attributes.containsKey('title')) {
            value = value.toString()
        } else {
            throw new RuntimeException("In $name either value is be a String or title: must be defined.")
        }
        builder.context.selected = attributes.remove('selected')

        return new MutableRibbonTask(value)
    }

    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof JRibbon || parent instanceof JRibbonFrame) {
            def ribbon = parent instanceof JRibbon ? parent : parent.ribbon
            ribbon.addTask(child)
            if (builder.context.selected) ribbon.setSelectedTask(child)
        } else if (parent instanceof MutableRibbonContextualTaskGroup) {
            parent.addTask(child)
        }
    }

//     void setChild(FactoryBuilderSupport build, Object parent, Object child) {
//       switch(child) {
//          case JRibbonBand:
//             break
//          case RibbonContextualTaskGroup:
//             parent.setRibbonContextualTaskGroup(child)
//             break
//       }
//    }
}