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
package groovy.util

import javax.swing.SwingUtilities
import java.lang.reflect.Constructor

abstract class GroovySwingTestCase extends GroovyTestCase {
    private static boolean headless;

    /**
     * A boolean indicating if we are running in headless mode.
     * Check this flag if you believe your test may make use of AWT/Swing
     * features, then simply return rather than running your test.
     *
     * @return true if running in headless mode
     */
    static boolean isHeadless() {
        return headless;
    }

    /**
     * Alias for isHeadless().
     *
     * @return true if running in headless mode
     */
    static boolean getHeadless() {
        return isHeadless();
    }

    static void testInEDT(Closure test) {
        Throwable exception = null
        if (headless) {
            return
        }
        SwingUtilities.invokeAndWait {
            try {
                test()
            } catch (Throwable t) {
                exception = t
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    static {
        try {
            final Class jframe = Class.forName("javax.swing.JFrame");
            final Constructor constructor = jframe.getConstructor([String] as Class[]);
            constructor.newInstance(["testing"] as String[]);
            headless = false;
        } catch (Throwable t) {
            // any exception means headless
            headless = true;
        }
    }

}
