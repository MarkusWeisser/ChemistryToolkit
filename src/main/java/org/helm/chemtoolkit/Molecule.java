/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.helm.chemtoolkit;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;

/**
 * Molecule
 * 
 * @author
 */
public class Molecule {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(Molecule.class);

  public String canonicalSMILES;


  /**
   * constructs with a given canonical SMILES a molecule
   * 
   * @param canSMILES
   */
  public Molecule(String canSMILES) {
    this.canonicalSMILES = canSMILES;
    System.out.println("TODO-> check if Molecule can be built");
  }

  /**
   * dearomatize molecule
   * 
   * @return true if success, else false
   */
  public boolean dearomatize() {
    return true;
  }

  /* string-> R1 or R2 */
  /**
   * contains from the given canonical SMILES all Rgroups
   * 
   * @return
   */
  public Map<String, MolAtom> getRgroups() {
    return null;
  }

  public MolAtom[] getAtomArray() {
    return null;
  }

  public MolAtom[] getBondArray() {
    return null;
  }



  public void implicitizeHydrogens() {

  }

  public void addExplicitHydrogens() {

  }

  public void clean() {

  }

  public String toFormat() {
    return null;
  }

}
