/*--
 *
 * @(#) IStereoElement.java
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

/**
 * {@code IStereoElement} The container for a stereo element in the molecule.
 * 
 * @author <a href="mailto:chistyakov@quattro-research.com">Dmitry Chistyakov</a>
 * @version $Id$
 */
public interface IStereoElementBase extends IChemObjectBase {

  public Object getStereoElement();

}
