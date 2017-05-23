/*
 * Copyright 2008-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.builder.flamingo.factory

import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenuEntryFooter

import java.awt.event.ActionListener

/**
 * @author Andres Almiray
 */
class RibbonApplicationMenuEntryFooterFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value instanceof RibbonApplicationMenuEntryFooter) {
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

        return new RibbonApplicationMenuEntryFooter(icon, text, mainActionListener)
    }

    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof RibbonApplicationMenu) {
            parent.addFooterEntry(child)
        }
    }
}
