package griffon.builder.flamingo.factory

import org.pushingpixels.flamingo.api.ribbon.JFlowRibbonBand

import javax.swing.JComponent
import java.awt.event.ActionListener

/**
 * @author Andres Almiray
 */
class FlowRibbonBandFactory extends RibbonBandFactory {
    protected Object newRibbonBand(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (value instanceof JFlowRibbonBand) {
            return value
        }
        if (value instanceof String || value instanceof GString) {
            value = value.toString()
        } else if (attributes.containsKey('title')) {
            value = value.toString()
        } else {
            throw new RuntimeException("In $name either value is be a String or title: must be defined.")
        }

        def icon = FlamingoFactoryUtils.createIcon(builder, name, attributes)
        ActionListener expandActionListener = attributes.remove('expandActionListener')
        def actionPerformed = attributes.remove('actionPerformed')
        if (!expandActionListener && actionPerformed instanceof Closure) {
            expandActionListener = actionPerformed as ActionListener
        }
        builder.context.task = attributes.remove('ask')
        return new JFlowRibbonBand(value, icon, expandActionListener)
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof JComponent) {
            parent.addFlowComponent(child)
        } else {
            super.setChild(builder, parent, child)
        }
    }
}