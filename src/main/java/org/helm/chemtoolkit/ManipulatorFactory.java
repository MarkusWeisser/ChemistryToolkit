/*--
 *
 * @(#) ManipulatorFactory.java
 *
 * Copyright 2015 by Roche Diagnostics GmbH,
 * Nonnenwald 2, DE-82377 Penzberg, Germany
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Roche Diagnostics GmbH ("Confidential Information"). You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Roche Diagnostics GmbH.
 *
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

  public enum ManipulatorType {
    MARVIN, CDK
  }

  public static AbstractChemistryManipulator buildManipulator(ManipulatorType type) throws ClassNotFoundException,
      NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    AbstractChemistryManipulator manipulator = null;
    String className = null;
    switch (type) {
    case MARVIN:
      className = "org.helm.chemtoolkit.chemaxon.ChemaxonManipulator";
      break;
    case CDK:
      className = "org.helm.chemtoolkit.cdk.CDKManipulator";
      break;
    default:
      break;
    }
    if (className != null) {
      Class<?> clazz = Class.forName(className);
      manipulator = (AbstractChemistryManipulator) clazz.getConstructor().newInstance();
    }
    return manipulator;
  }

}
