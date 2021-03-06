/**
 * ***************************************************************************** Copyright C 2015, The Pistoia Alliance
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *****************************************************************************
 */
package org.helm.chemtoolkit;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code ManipulatorFactory} TODO comment me
 * 
 * @author <a href="mailto:chistyakov@quattro-research.com">Dmitry Chistyakov</a>
 * @version $Id$
 */
public class ManipulatorFactory {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(ManipulatorFactory.class);

  public static AbstractChemistryManipulator buildManipulator(String className) throws ClassNotFoundException,
      NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    AbstractChemistryManipulator manipulator = null;

    if (className != null) {
      Class<?> clazz = Class.forName(className);
      manipulator = (AbstractChemistryManipulator) clazz.getConstructor().newInstance();
    }
    return manipulator;
  }

}
