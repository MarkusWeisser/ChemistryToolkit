/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
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
 ******************************************************************************/
package org.helm.chemtoolkit.cdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.AttachmentList;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.IBondBase;
import org.helm.chemtoolkit.IChemObjectBase;
import org.openscience.cdk.aromaticity.Kekulization;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chistyakov
 *
 */
public class CDKMolecule extends AbstractMolecule {
  private static final Logger LOG = LoggerFactory.getLogger(CDKMolecule.class);

  protected IAtomContainer molecule;

  protected List<IAtomBase> atomArray;

  public CDKMolecule(IAtomContainer molecule) {
    this(molecule, new AttachmentList());

  }

  public CDKMolecule(IAtomContainer molecule, AttachmentList attachments) {
    this.molecule = molecule;
    this.attachments = attachments;
    atomArray = new ArrayList<>();
    for (IAtom atom : molecule.atoms()) {
      int rGroupId = 0;
      // IAtom atom = molecule.getAtom(i);
      if (atom instanceof IPseudoAtom) {
        atom.setSymbol("R");
        rGroupId = AbstractMolecule.getIdFromLabel(((IPseudoAtom) atom).getLabel());
      }
      List<IBond> bonds = molecule.getConnectedBondsList(atom);
      atomArray.add(new CDKAtom(atom, rGroupId, bonds));
    }

  }

  /*
   * public CDKMolecule(IAtomContainer molecule, int rGroup) { this.molecule = molecule; this.rGroup = rGroup; }
   */

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#getRgroups()
   */
  @Override
  public Map<String, IAtomBase> getRgroups() throws CTKException {
    // dearomatize();

    return super.getRgroups();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#dearomatize()
   */

  @Override
  public void dearomatize() throws CTKException {

    try {
      Kekulization.kekulize(molecule);
    } catch (CDKException e) {
      throw new CTKException(e.getMessage(), e);
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#removeINode(org.helm.chemtoolkit. IAtomBase)
   */
  @Override
  public void removeINode(IAtomBase node) throws CTKException {
    if (node instanceof CDKAtom) {

      molecule.removeAtomAndConnectedElectronContainers(((CDKAtom) node).atom);

      for (int i = 0; i < atomArray.size(); i++) {
        if (((CDKAtom) atomArray.get(i)).compare(node)) {
          atomArray.remove(i);
          break;
        }
      }

    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#getIAtomArray()
   */
  @Override
  public List<IAtomBase> getIAtomArray() {
// List<IAtomBase> atomArray = new ArrayList<>();
// for (IAtom atom : molecule.atoms()) {
// int rGroupId = 0;
//// IAtom atom = molecule.getAtom(i);
// if (atom instanceof IPseudoAtom) {
// atom.setSymbol("R");
//// LOG.debug("label=" + ((IPseudoAtom) atom).getLabel());
// rGroupId = AbstractMolecule.getIdFromLabel(((IPseudoAtom) atom).getLabel());
// }
// List<IBond> bonds = molecule.getConnectedBondsList(atom);
// atomArray.add(new CDKAtom(atom, rGroupId, bonds));
// }
    return atomArray;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#addIBase(org.helm.chemtoolkit. IChemObjectBase)
   */
  @Override
  public void addIBase(IChemObjectBase object) {
    if (object instanceof CDKMolecule) {
      molecule.add(((CDKMolecule) object).molecule);
    } else if (object instanceof CDKAtom) {
      // ((CDKAtom) object).atom.setFlag(CDKConstants.VISITED, true);

      molecule.addAtom(((CDKAtom) object).atom);
    } else if (object instanceof CDKBond) {
      // ((CDKBond) object).bond.setFlag(CDKConstants.VISITED, true);
      molecule.addBond(((CDKBond) object).bond);
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#getIBondArray()
   */
  @Override
  public List<IBondBase> getIBondArray() {
    List<IBondBase> array = new ArrayList<>();

    for (IBond item : molecule.bonds()) {
      array.add(new CDKBond(item));
    }
    return array;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#cloneMolecule()
   */
  @Override
  public AbstractMolecule cloneMolecule() throws CTKException {
    CDKMolecule cloned = null;
    try {
      cloned = new CDKMolecule(molecule.clone());
    } catch (CloneNotSupportedException e) {
      throw new CTKException(e.getMessage(), e);
    }
    return cloned;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws CTKException
   */
  @Override
  public void generateCoordinates() throws CTKException {

    StructureDiagramGenerator sdg = new StructureDiagramGenerator();
    sdg.setMolecule(molecule);
    try {
      sdg.generateCoordinates();
    } catch (CDKException e) {
      throw new CTKException("unable generate coordinates", e);
    }
    molecule = sdg.getMolecule();

  }

  /**
   * @return
   */
  @Override
  public IAtomContainer getMolecule() {
    return molecule;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws CTKException
   */
  @Override
  public void changeAtomLabel(int index, int toIndex) throws CTKException {
    for (IAtomBase atom : getIAtomArray()) {
      if (atom.getMolAtom() instanceof IPseudoAtom) {
        int currIndex = AbstractMolecule.getIdFromLabel(((IPseudoAtom) atom.getMolAtom()).getLabel());
        if (currIndex == index)
          atom.setRgroup(toIndex);
      }
    }

  }

}
