/*--
 *
 * @(#) CDKStereoElement.java
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
package org.helm.chemtoolkit.cdk;

import org.helm.chemtoolkit.IStereoElementBase;
import org.openscience.cdk.interfaces.IStereoElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * {@code CDKStereoElement} TODO comment me
 * 
 * @author <a href="mailto:chistyakov@quattro-research.com">Dmitry Chistyakov</a>
 * @version $Id$
 */
public class CDKStereoElement implements IStereoElementBase {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(CDKStereoElement.class);

  private IStereoElement stereoElement;

  @Override
  public IStereoElement getStereoElement() {
    return this.stereoElement;
  }

  public CDKStereoElement(IStereoElement stereoElement) {
    this.stereoElement = stereoElement;
  }

}
