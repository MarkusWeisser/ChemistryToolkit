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

import org.helm.chemtoolkit.IAtom;
import org.helm.chemtoolkit.IChemObject;
import org.helm.chemtoolkit.IMolecule;

import chemaxon.struc.MolAtom;
import chemaxon.struc.Molecule;

/**
 * @author chistyakov
 *
 */
public class ChemMolecule implements IMolecule {

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
	public void removeINode(IAtom node) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IMolecule#getIAtomArray()
	 */
	@Override
	public IAtom[] getIAtomArray() {
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
	public void addI(IChemObject node) {
		if (node instanceof IAtom) {
			ChemAtom atom = (ChemAtom) node;
			molecule.add(atom.getMolAtom());
		}

	}

}
