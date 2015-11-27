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
package org.helm.chemtoolkit.chemaxon;

import java.util.HashMap;
import java.util.Map;

import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.IBondBase;
import org.helm.chemtoolkit.IChemObjectBase;
import org.helm.chemtoolkit.IMoleculeBase;

import chemaxon.struc.MolAtom;
import chemaxon.struc.MolBond;
import chemaxon.struc.Molecule;

/**
 * @author chistyakov
 *
 */
public class ChemMolecule implements IMoleculeBase {

	private Molecule molecule;

	public ChemMolecule(Molecule molecule) {
		this.molecule = molecule;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.helm.chemtoolkit.IMolecule#removeINode(org.helm.chemtoolkit.IAtom)
	 */
	@Override
	public void removeINode(IAtomBase node) {
		if (node instanceof ChemAtom) {
			molecule.removeNode(((ChemAtom) node).getMolAtom());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IMolecule#getIAtomArray()
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
	 * @see
	 * org.helm.chemtoolkit.IMolecule#addI(org.helm.chemtoolkit.IChemObject)
	 */
	@Override
	public void addI(IChemObjectBase node) {
		if (node instanceof ChemAtom) {
			molecule.add(((ChemAtom) node).getMolAtom());
		} else if (node instanceof ChemBond) {
			molecule.add(((ChemBond) node).getMolBond());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IMolecule#dearomatize()
	 */
	@Override
	public void dearomatize() {
		molecule.dearomatize();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IMolecule#getRgroups()
	 */
	@Override
	public Map<String, IAtomBase> getRgroups() {
		molecule.dearomatize();
		Map<String, IAtomBase> rgroupMap = new HashMap<String, IAtomBase>();

		IAtomBase[] atoms = getIAtomArray();
		for (int i = 0; i < atoms.length; i++) {
			int rId = atoms[i].getRgroup();
			// found R group
			if (rId > 0) {
				rgroupMap.put("R" + rId, atoms[i]);
			}
		}

		return rgroupMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IMolecule#getIBondArray()
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

	@Override
	public IMoleculeBase clone() {
		ChemMolecule cloned = new ChemMolecule(molecule);
		return cloned;
	}

}
