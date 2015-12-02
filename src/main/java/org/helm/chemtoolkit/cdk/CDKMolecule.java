/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.helm.chemtoolkit.cdk;

import java.util.Map;

import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.IBondBase;
import org.helm.chemtoolkit.IChemObjectBase;
import org.helm.chemtoolkit.AbstractMolecule;
import org.openscience.cdk.aromaticity.Kekulization;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * @author chistyakov
 *
 */
public class CDKMolecule extends AbstractMolecule implements IAtomBase {

	protected IAtomContainer molecule;

	protected int rGroup;

	public CDKMolecule(IAtomContainer molecule) {
		this.molecule = molecule;
		this.rGroup = 0;

	}

	public CDKMolecule(IAtomContainer molecule, int rGroup) {
		this.molecule = molecule;
		this.rGroup = rGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.AbstractMolecule#getRgroups()
	 */
	@Override
	public Map<String, IAtomBase> getRgroups() throws CTKException {
		dearomatize();

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
	 * @see
	 * org.helm.chemtoolkit.AbstractMolecule#removeINode(org.helm.chemtoolkit.
	 * IAtomBase)
	 */
	@Override
	public void removeINode(IAtomBase node) {
		if (node instanceof CDKAtom) {
			molecule.removeAtom(((CDKAtom) node).atom);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.AbstractMolecule#getIAtomArray()
	 */
	@Override
	public IAtomBase[] getIAtomArray() {
		CDKAtom[] array = new CDKAtom[molecule.getAtomCount()];
		for (int i = 0; i < array.length; i++) {
			array[i] = new CDKAtom(molecule.getAtom(i));
		}
		return array;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.AbstractMolecule#addIBase(org.helm.chemtoolkit.
	 * IChemObjectBase)
	 */
	@Override
	public void addIBase(IChemObjectBase object) {
		if (object instanceof CDKMolecule) {
			molecule.add(((CDKMolecule) object).molecule);
		} else if (object instanceof CDKAtom) {
			molecule.addAtom(((CDKAtom) object).atom);
		} else if (object instanceof CDKBond) {
			molecule.addBond(((CDKBond) object).bond);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.AbstractMolecule#getIBondArray()
	 */
	@Override
	public IBondBase[] getIBondArray() {
		CDKBond[] array = new CDKBond[molecule.getBondCount()];
		for (int i = 0; i < array.length; i++) {
			array[i] = new CDKBond(molecule.getBond(i));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IAtomBase#getIBondCount()
	 */
	@Override
	public int getIBondCount() {
		return molecule.getBondCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IAtomBase#getIBond(int)
	 */
	@Override
	public IBondBase getIBond(int arg0) {

		return new CDKBond(molecule.getBond(arg0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IAtomBase#getRgroup()
	 */
	@Override
	public int getRgroup() {
		return rGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IAtomBase#getIAtno()
	 */
	@Override
	public int getIAtno() {

		return molecule.getAtom(0).getAtomicNumber();
	}


}
