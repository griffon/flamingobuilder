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

import org.pushingpixels.flamingo.api.common.RichTooltip
import org.pushingpixels.flamingo.api.ribbon.JRibbon
import org.pushingpixels.flamingo.api.ribbon.JRibbonBand
import org.pushingpixels.flamingo.api.ribbon.RibbonApplicationMenu
import org.pushingpixels.flamingo.api.ribbon.RibbonContextualTaskGroup
import org.pushingpixels.flamingo.api.ribbon.RibbonTask

import java.awt.Component

/**
 * @author Andres Almiray
 */
class RibbonFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (FactoryBuilderSupport.checkValueIsType(value, name, JRibbon)) {
            return value
        }
        return new JRibbon()
    }

    void setChild(FactoryBuilderSupport build, Object parent, Object child) {
        switch (child) {
            case RibbonTask:
            case JRibbonBand:
            case RibbonContextualTaskGroup:
            case RibbonApplicationMenu:
                // handled by other factories
                break
            case RichTooltip:
                parent.setApplicationMenuRichTooltip(child)
                break
            case Component:
                parent.addTaskbarComponent(child)
                break
        }
    }
}