package griffon.builder.flamingo.factory

import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent

import javax.swing.JComponent

/**
 * @author Andres Almiray
 */
class RibbonComponentFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value instanceof JRibbonComponent) {
            return value
        }

        if (value == null && attributes.containsKey('component')) {
            value = attributes.remove('component')
        }
        if (!(value instanceof JComponent)) {
            throw new RuntimeException("In $name either value or component: must be a JComponent")
        }
        def icon = FlamingoFactoryUtils.createIcon(builder, name, '', attributes)
        def caption = attributes.remove('caption')
        return icon && caption ? new JRibbonComponent(value, icon, caption) : new JRibbonComponent(value)
    }
}