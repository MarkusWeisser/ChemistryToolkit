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
package org.helm.chemtoolkit.chemaxon;

import java.util.Map;

import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.AttachmentList;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.IBondBase;
import org.helm.chemtoolkit.IChemObjectBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;

/**
 * @author chistyakov
 *
 */
public class ChemMolecule extends AbstractMolecule {
  private static final Logger LOG = LoggerFactory.getLogger(ChemMolecule.class);

  private Molecule molecule;

  public Molecule getMolecule() {
    return this.molecule;
  }

  public ChemMolecule(Molecule molecule) {
    this.molecule = molecule;

  }

  public ChemMolecule(Molecule molecule, AttachmentList attachments) {
    this.molecule = molecule;
    if (attachments != null) {
      this.attachments =
          attachments.cloneList();
    } else
      this.attachments = new AttachmentList();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#removeINode(org.helm.chemtoolkit. IAtom)
   */
  @Override
  public void removeINode(IAtomBase node) {
    for (MolAtom atom : molecule.getAtomArray()) {
      if (atom.equals(((ChemAtom) node).getMolAtom())) {
        LOG.debug("atom founded in the molecule");
      }
    }
    if (node instanceof ChemAtom) {
      molecule.removeNode(((ChemAtom) node).getMolAtom());
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#getIAtomArray()
   */
  @Override
  public IAtomBase[] getIAtomArray() {
    MolAtom[] parent = molecule.getAtomArray();
    ChemAtom[] target = new ChemAtom[parent.length];
    for (int i = 0; i < target.length; i++) {
      target[i] = new ChemAtom(parent[i]);
    }
    return target;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#addI(org.helm.chemtoolkit. IChemObject)
   */
  @Override
  public void addIBase(IChemObjectBase node) {
    if (node instanceof ChemAtom) {
      molecule.add(((ChemAtom) node).getMolAtom());
    } else if (node instanceof ChemBond) {
      molecule.add(((ChemBond) node).getMolBond());
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#dearomatize()
   */
  @Override
  public void dearomatize() throws CTKException {
    try {
      molecule.dearomatize();
    } catch (IllegalArgumentException e) {
      throw new CTKException(e.getMessage(), e);
    }

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#getRgroups()
   */
  @Override
  public Map<String, IAtomBase> getRgroups() throws CTKException {
    molecule.dearomatize();
    return super.getRgroups();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMolecule#getIBondArray()
   */
  @Override
  public IBondBase[] getIBondArray() {
    MolBond[] parent = molecule.getBondArray();
    ChemBond[] target = new ChemBond[parent.length];
    for (int i = 0; i < target.length; i++) {
      target[i] = new ChemBond(parent[i]);
    }
    return target;

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.helm.chemtoolkit.AbstractMoleculeBase#cloneMolecule()
   */
  @Override
  public AbstractMolecule cloneMolecule() {
    ChemMolecule cloned = new ChemMolecule(molecule.cloneMolecule());

    return cloned;
  }

  /**
   * {@inheritDoc}
   */
  /*
   * @Override public void replaceRGroups(List<AbstractMolecule> groups) { for (int i = 0; i < groups.size(); i++) {
   * ChemMolecule group = (ChemMolecule) groups.get(i); int groupId =
   * getIdFromLabel(group.getAttachments().get(i).getLabel()); MolAtom pseudo = getRGroupAtom(groupId); for (MolAtom
   * item : molecule.getAtomArray()) { if (item.getRgroup() == groupId) {
   * 
   * }
   * 
   * }
   * 
   * }
   * 
   * }
   */

  @Override
  public ChemAtom getRGroupAtom(int groupId, boolean rgatom) {
    MolAtom result = null;
    for (MolAtom atom : molecule.getAtomArray()) {
      if (atom.getRgroup() == groupId) {
        if (rgatom) {
          result = atom;
        } else {
          MolBond bond = atom.getBond(0);
          if (bond.getAtom1().equals(atom)) {
            result = bond.getAtom2();
          } else
            result = bond.getAtom1();
        }
        LOG.debug("rgroup atom=" + result);
      }
    }

    return new ChemAtom(result);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateCoordinates() throws CTKException {
    molecule.clean(2, null);

  }

}
