package griffon.builder.flamingo.factory

import org.pushingpixels.flamingo.api.ribbon.JRibbon
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu

/**
 * @author Andres Almiray
 */
class RibbonApplicationMenuFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value instanceof RibbonApplicationMenu) {
            return value
        }

        return new RibbonApplicationMenu()
    }

    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof JRibbon || parent instanceof JRibbonFrame) {
            def ribbon = parent instanceof JRibbon ? parent : parent.ribbon
            ribbon.setApplicationMenu(child)
        }
    }
}