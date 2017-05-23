package griffon.builder.flamingo.factory

import griffon.builder.flamingo.impl.MutableRibbonTask
import org.pushingpixels.flamingo.api.common.AbstractCommandButton
import org.pushingpixels.flamingo.api.ribbon.JRibbon
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand
import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame
import org.pushingpixels.flamingo.api.ribbon.RibbonElementPriority

import java.awt.event.ActionListener

/**
 * @author Andres Almiray
 */
class RibbonBandFactory extends AbstractFactory {
    static final String DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY = '_delegateProperty:ribbonElementPriority';
    static final String DEFAULT_DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY = 'ribbonElementPriority';

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def newChild = newRibbonBand(builder, name, value, attributes)
        builder.context.ribbonBandFactoryClosure = { FactoryBuilderSupport cBuilder, Object cNode, Map cAttributes ->
            if (builder.current == newChild) inspectChild(cBuilder, cNode, cAttributes)
        }
        builder.addAttributeDelegate(builder.context.ribbonBandFactoryClosure)
        builder.context[DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY] = attributes.remove('ribbonElementPriorityProperty') ?: DEFAULT_DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY
        return newChild
    }

    static void inspectChild(FactoryBuilderSupport builder, Object node, Map attributes) {
        def priority = attributes.remove(builder?.parentContext?.getAt(DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY) ?: DEFAULT_DELEGATE_PROPERTY_RIBBON_ELEMENT_PRIORITY)
        builder.context.put(node, [priority])
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if (child instanceof AbstractCommandButton) {
            def props = builder.context[child] ?: [RibbonElementPriority.LOW]
            switch (props[0]) {
                case RibbonElementPriority.TOP:
                case ~/(?i:top)/:
                    props[0] = RibbonElementPriority.TOP
                    break
                case RibbonElementPriority.MEDIUM:
                case ~/(?i:medium)/:
                    props[0] = RibbonElementPriority.MEDIUM
                    break
                case RibbonElementPriority.LOW:
                case ~/(?i:low)/:
                default:
                    props[0] = RibbonElementPriority.LOW
                    break
            }
            parent.addCommandButton(child, props[0])
        } else if (child instanceof JRibbonComponent) {
            parent.addRibbonComponent(child)
        }
        // TODO RibbonGallery
    }

    void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        super.onNodeCompleted(builder, parent, node)
        builder.removeAttributeDelegate(builder.context.ribbonBandFactoryClosure)
    }

    protected Object newRibbonBand(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if (value instanceof JRibbonBand) {
            return value
        }
        if (value instanceof String || value instanceof GString) {
            value = value.toString()
        } else if (attributes.containsKey("title")) {
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
        builder.context.task = attributes.remove('task')
        return new JRibbonBand(value, icon, expandActionListener)
    }

    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof MutableRibbonTask) {
            parent.addBand(child)
        } else if ((parent instanceof JRibbon || parent instanceof JRibbonFrame) &&
            builder.context.task) {
            def taskName = builder.context.task.toString()
            def ribbon = parent instanceof JRibbon ? parent : parent.ribbon
            int taskCount = ribbon.taskCount
            boolean found
            for (int i = 0; i < taskCount; i++) {
                def task = ribbon.getTask(i)
                if (task.title == taskName && task instanceof MutableRibbonTask) {
                    found = true
                    task.addBand(child)
                }
            }
            if (!found) {
                ribbon.addTask(new MutableRibbonTask(taskName, child))
            }
        }
    }
}