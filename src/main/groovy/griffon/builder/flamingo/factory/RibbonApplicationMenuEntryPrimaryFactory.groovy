package griffon.builder.flamingo.factory

import org.pushingpixels.flamingo.api.common.JCommandButton
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryPrimary

import java.awt.event.ActionListener

/**
 * @author Andres Almiray
 */
class RibbonApplicationMenuEntryPrimaryFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value instanceof RibbonApplicationMenuEntryPrimary) {
            return value
        }

        if (value instanceof String || value instanceof GString) {
            value = value.toString()
        } else if (attributes.containsKey('text')) {
            value = value.toString()
        } else {
            throw new RuntimeException("In $name either value is be a String or text: must be defined.")
        }

        def icon = FlamingoFactoryUtils.createIcon(builder, name, attributes)
        ActionListener mainActionListener = attributes.remove('mainActionListener')
        def actionPerformed = attributes.remove('actionPerformed')
        if (!mainActionListener && actionPerformed instanceof Closure) {
            mainActionListener = actionPerformed as ActionListener
        }
        FlamingoFactoryUtils.translateFlamingoConstant("entryKind", JCommandButton.CommandButtonKind, attributes)
        JCommandButton.CommandButtonKind entryKind = attributes.remove("entryKind")
        if (!entryKind) throw new RuntimeException("In $name a value for entryKind: must be defined.")

        return new RibbonApplicationMenuEntryPrimary(icon, text, mainActionListener, entryKind)
    }

    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof RibbonApplicationMenu) {
            parent.addMenuEntry(child)
        }
    }
}