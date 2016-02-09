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
package org.helm.chemistrytoolkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.helm.chemtoolkit.AbstractChemistryManipulator;
import org.helm.chemtoolkit.AbstractChemistryManipulator.OutputType;
import org.helm.chemtoolkit.AbstractChemistryManipulator.StType;
import org.helm.chemtoolkit.AbstractMolecule;
import org.helm.chemtoolkit.Attachment;
import org.helm.chemtoolkit.AttachmentList;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.MoleculeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * {@code TestBase} TODO comment me
 * 
 * @author <a href="mailto:chistyakov@quattro-research.com">Dmitry Chistyakov</a>
 * @version $Id$
 */
public abstract class TestBase {

  /** The Logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(TestBase.class);

  protected AbstractChemistryManipulator manipulator;

  protected String testResult;

  public void validateSMILESTest() throws CTKException {
    boolean res = false;
    String smiles = "[*]N1CC[C@H]1C([*])=O |r,$_R1;;;;;;_R2;$|";
    res = manipulator.validateSMILES(smiles);
    Assert.assertEquals(res, true);
  }

  public void getMoleculeInfoTest() throws CTKException, IOException {
    String smiles = "CCOC1=C(C=C(C=C1)S(=O)(=O)N1CCN(C)CC1)C1=NC2=C(N(C)N=C2CC)C(=O)N1";
    MoleculeInfo moleculeInfo = manipulator.getMoleculeInfo(manipulator.getMolecule(smiles, null));
    testResult = moleculeInfo.getMolecularFormula();

  }

  public void convertSMILES2MolFile() throws CTKException, Exception {
    String smiles = "[*]N1CC[C@H]1C([*])=O |r,$_R1;;;;;;_R2;$|";
    testResult = manipulator.convert(smiles, StType.SMILES);

  }

  public void convertMolFile2SMILES() throws CTKException, IOException {
    String molFile = "\n" +
        "  Marvin  12081516212D\n" +
        "\n" +
        " 22 24  0  0  1  0            999 V2000\n" +
        "   -0.6534    1.5387    0.0000 H   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "   -0.3984    0.7541    0.0000 C   0  0  2  0  0  0  0  0  0  0  0  0\n" +
        "   -1.1831    1.0091    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "   -1.1831    1.8341    0.0000 C   0  0  2  0  0  0  0  0  0  0  0  0\n" +
        "   -1.8505    2.3190    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "   -2.6042    1.9834    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "   -3.2716    2.4683    0.0000 R#  0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "   -0.3984    2.0890    0.0000 C   0  0  1  0  0  0  0  0  0  0  0  0\n" +
        "   -0.1435    2.8736    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    0.6635    3.0451    0.0000 R#  0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    0.0865    1.4216    0.0000 C   0  0  1  0  0  0  0  0  0  0  0  0\n" +
        "    0.9115    1.4216    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "   -0.1435   -0.0305    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "   -0.6284   -0.6979    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "   -0.1435   -1.3654    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    0.6411   -1.1104    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    0.6411   -0.2854    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    1.3556    0.1271    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    2.0701   -0.2854    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    2.0701   -1.1104    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    1.3556   -1.5229    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "    1.3556   -2.3479    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n" +
        "  2  1  1  1  0  0  0\n" +
        "  2  3  1  0  0  0  0\n" +
        "  3  4  1  0  0  0  0\n" +
        "  4  5  1  6  0  0  0\n" +
        "  5  6  1  0  0  0  0\n" +
        "  6  7  1  0  0  0  0\n" +
        "  4  8  1  0  0  0  0\n" +
        "  8  9  1  1  0  0  0\n" +
        "  9 10  1  0  0  0  0\n" +
        "  8 11  1  0  0  0  0\n" +
        "  2 11  1  0  0  0  0\n" +
        " 11 12  1  1  0  0  0\n" +
        " 13 14  1  0  0  0  0\n" +
        " 14 15  2  0  0  0  0\n" +
        " 15 16  1  0  0  0  0\n" +
        " 16 17  2  0  0  0  0\n" +
        " 13 17  1  0  0  0  0\n" +
        " 17 18  1  0  0  0  0\n" +
        " 18 19  2  0  0  0  0\n" +
        " 19 20  1  0  0  0  0\n" +
        " 20 21  2  0  0  0  0\n" +
        " 16 21  1  0  0  0  0\n" +
        " 21 22  1  0  0  0  0\n" +
        "  2 13  1  0  0  0  0\n" +
        "M  RGP  2   7   1  10   2\n" +
        "M  END";

    testResult = manipulator.convert(molFile, StType.MOLFILE);
    testResult = manipulator.canonicalize(testResult);
  }

  public void canonicalizeTest() throws CTKSmilesException, CTKException {
    String smiles = "CCOc1ccc(cc1-c1nc2c(CC)nn(C)c2c(=O)[nH]1)S(=O)(=O)N1CCN(C)CC1";
    testResult = manipulator.canonicalize(smiles);

  }

  public void renderMolTest() throws CTKException, IOException {
    String molFile = "\n" + "  ACCLDraw11131512172D\n" + "\n" + " 32 35  0  0  0  0  0  0  0  0999 V2000\n"
        + "    7.6862   -7.0367    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    6.6485   -6.4506    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    5.6213   -4.6864    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    6.6485   -5.2695    0.0000 N   0  0  3  0  0  0  0  0  0  0  0  0\n"
        + "    7.6653   -4.6687    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    8.7132   -5.2548    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    8.7132   -6.4417    0.0000 N   0  0  3  0  0  0  0  0  0  0  0  0\n"
        + "   10.3235   -5.9976    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    9.1573   -8.0519    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "    9.7404   -7.0246    0.0000 S   0  0  3  0  0  0  0  0  0  0  0  0\n"
        + "   15.8922   -9.3677    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   14.8676   -8.7801    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   13.8465   -9.3736    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   12.8167   -8.7860    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   11.7999   -9.3781    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   10.7675   -8.7950    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   10.7675   -7.6078    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   11.7895   -7.0036    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   12.8167   -7.5988    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   15.8892   -7.0083    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   14.8640   -7.5949    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   13.8388   -7.0068    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   13.8388   -5.8189    0.0000 N   0  0  3  0  0  0  0  0  0  0  0  0\n"
        + "   14.8640   -4.0379    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   14.8640   -5.2190    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   15.8892   -5.8189    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   17.3854   -4.3283    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   17.0205   -5.4515    0.0000 N   0  0  3  0  0  0  0  0  0  0  0  0\n"
        + "   17.7197   -6.4137    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   17.0205   -7.3760    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   17.3853   -8.4994    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"
        + "   18.5407   -8.7449    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n" + "  2  1  1  0  0  0  0\n"
        + "  4  2  1  0  0  0  0\n" + "  4  3  1  0  0  0  0\n" + "  5  4  1  0  0  0  0\n"
        + "  6  5  1  0  0  0  0\n" + "  7  6  1  0  0  0  0\n" + "  7  1  1  0  0  0  0\n"
        + " 10  7  1  0  0  0  0\n" + " 10  8  2  0  0  0  0\n" + " 10  9  2  0  0  0  0\n"
        + " 17 10  1  0  0  0  0\n" + " 12 11  1  0  0  0  0\n" + " 13 12  1  0  0  0  0\n"
        + " 14 13  1  0  0  0  0\n" + " 15 14  1  0  0  0  0\n" + " 16 15  2  0  0  0  0\n"
        + " 17 16  1  0  0  0  0\n" + " 18 17  2  0  0  0  0\n" + " 19 18  1  0  0  0  0\n"
        + " 19 14  2  0  0  0  0\n" + " 22 19  1  0  0  0  0\n" + " 21 20  1  0  0  0  0\n"
        + " 22 21  2  0  0  0  0\n" + " 23 22  1  0  0  0  0\n" + " 25 23  1  0  0  0  0\n"
        + " 25 24  2  0  0  0  0\n" + " 26 25  1  0  0  0  0\n" + " 26 20  2  0  0  0  0\n"
        + " 28 26  1  0  0  0  0\n" + " 28 27  1  0  0  0  0\n" + " 29 28  1  0  0  0  0\n"
        + " 30 29  2  0  0  0  0\n" + " 30 20  1  0  0  0  0\n" + " 31 30  1  0  0  0  0\n"
        + " 32 31  1  0  0  0  0\n" + "M  END";
    byte[] result = manipulator.renderMol(molFile, OutputType.PNG, 1000, 1000, (int) Long.parseLong("D3D3D3", 16));
    try (FileOutputStream out = new FileOutputStream("test-output" + File.separator + "TestBild.png")) {
      out.write(result);
    }
  }

  public void renderSequenceTest() throws NumberFormatException, CTKException, IOException {
    String sequence = "CGT";
    byte[] result =
        manipulator.renderSequence(sequence, OutputType.PNG, 1000, 1000, (int) Long.parseLong("D3D3D3", 16));
    try (FileOutputStream out = new FileOutputStream("test-output" + File.separator + "Sequence.png")) {
      out.write(result);
    }
  }

  public void createGroupMolFile() throws CTKException, IOException {
    String smiles = "[*]N1CC[C@H]1C([*])=O |r,$_R2;;;;;;_R1;$|";
    testResult = manipulator.convertMolecule(manipulator.getMolecule(smiles, null), StType.MOLFILE);
  }

  public void adeninRiboseMerge() throws IOException, CTKException {

    String ribose = "[H][C@@]1([*])O[C@H](CO[*])[C@@H](O[*])[C@H]1O |$;;_R1;;;;;_R3;;;_R2;;$|";

    String adenin = "[*]n1cnc2c1ncnc2N |r,$_R1;;;;;;;;;;;;$|";

    String riboseR1 = "[*][H] |$_R3;$|";
    String riboseR2 = "[*][H] |$_R2;$|";
    String riboseR3 = "O[*] |$;_R1$|";
    String adeninR1 = "[*][H] |$_R1;$|";
    AttachmentList groupsAdenin = new AttachmentList();
    AttachmentList groupsRibose = new AttachmentList();
    groupsAdenin.add(new Attachment("R1-H", "R1", "H", adeninR1));
    groupsRibose.add(new Attachment("R3-H", "R3", "H", riboseR1));
    groupsRibose.add(new Attachment("R2-H", "R2", "H", riboseR2));
    groupsRibose.add(new Attachment("R1-OH", "R1", "OH", riboseR3));

    AbstractMolecule adeninMolecule = manipulator.getMolecule(adenin, groupsAdenin);

    AbstractMolecule riboseMolecule = manipulator.getMolecule(ribose, groupsRibose);

    AbstractMolecule molecule =
        manipulator.merge(riboseMolecule, riboseMolecule.getRGroupAtom(1, true), adeninMolecule, adeninMolecule.getRGroupAtom(1, true));

    molecule.generateCoordinates();
    testResult = manipulator.convertMolecule(molecule, StType.MOLFILE);

  }

  public void mergeTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
      CTKException {
    String molecule1 = "[*]N[C@@H](CC([*])=O)C([*])=O |$_R1;;;;;_R3;;;_R2;$|";
    String molecule2 = "[*]N[C@@H](CCC([*])=O)C([*])=O |$_R1;;;;;;_R3;;;_R2;$|";
    String mol1Gruppe1 = "[*][H] |$_R1;$|";
    String mol1Gruppe2 = "O[*] |$_R2;$|";
    String mol1Gruppe3 = "O[*] |$;_R3$|";
    String mol2Gruppe1 = "[*][H] |$_R1;$|";
    String mol2Gruppe2 = "O[*] |$_R2;$|";
    String mol2Gruppe3 = "O[*] |$;_R3$|";

    AttachmentList mol1Attachments = new AttachmentList();
    mol1Attachments.add(new Attachment("R3-OH", "R3", "OH", mol1Gruppe3));
    mol1Attachments.add(new Attachment("R1-H", "R1", "H", mol1Gruppe1));
    mol1Attachments.add(new Attachment("R2-OH", "R2", "OH", mol1Gruppe2));

    AttachmentList mol2Attachments = new AttachmentList();
    mol2Attachments.add(new Attachment("R3-OH", "R3", "OH", mol2Gruppe3));
    mol2Attachments.add(new Attachment("R1-H", "R1", "H", mol2Gruppe1));
    mol2Attachments.add(new Attachment("R2-OH", "R2", "OH", mol2Gruppe2));
    AbstractMolecule absMol1 = manipulator.getMolecule(molecule1, mol1Attachments);
    AbstractMolecule absMol2 = manipulator.getMolecule(molecule2, mol2Attachments);
    AbstractMolecule mergedMolecule =
        manipulator.merge(absMol1, absMol1.getRGroupAtom(2, true), absMol2, absMol2.getRGroupAtom(1, true));

    testResult = manipulator.convertMolecule(mergedMolecule, StType.MOLFILE);

  }

  public void aaMerge() throws IOException, CTKException {
    String smiles = "[*]NCC([*])=O |$_R1;;;;_R2;$|";
    String gr1 = "[*][H] |$_R1;$|";
    String gr2 = "O[*] |$;_R2$|";
    AttachmentList list1 = new AttachmentList();
    list1.add(new Attachment("R1-H", "R1", "H", gr1));
    list1.add(new Attachment("R2-OH", "R2", "OH", gr2));
    AttachmentList list2 = new AttachmentList();
    list2.add(new Attachment("R1-H", "R1", "H", gr1));
    list2.add(new Attachment("R2-OH", "R2", "OH", gr2));
    AttachmentList list3 = new AttachmentList();
    list3.add(new Attachment("R1-H", "R1", "H", gr1));
    list3.add(new Attachment("R2-OH", "R2", "OH", gr2));
// AbstractChemistryManipulator manipulator = getManipulator();
    AbstractMolecule molecule1 = manipulator.getMolecule(smiles, list1);
    AbstractMolecule molecule2 = manipulator.getMolecule(smiles, list2);
    AbstractMolecule molecule3 = manipulator.getMolecule(smiles, list3);
    molecule1 =
        manipulator.merge(molecule1, molecule1.getRGroupAtom(2, true), molecule2, molecule2.getRGroupAtom(1, true));

    molecule1 =
        manipulator.merge(molecule1, molecule1.getRGroupAtom(2, true), molecule3, molecule3.getRGroupAtom(1, true));
    molecule1.generateCoordinates();
    String result = manipulator.convertMolecule(molecule1, StType.MOLFILE);
    LOG.debug(result);

  }

  public void getRibose() throws IOException, CTKException {
    LOG.debug("manipulator=" + manipulator);
    String ribose = "O[C@H]1[C@H]([*])O[C@H](CO[*])[C@H]1O[*] |$;;;_R3;;;;;_R1;;;_R2$|";

    String riboseR1 = "[*][H] |$_R1;$|";
    String riboseR2 = "[*][H] |$_R2;$|";
    String riboseR3 = "O[*] |$;_R3$|";

    AttachmentList groupsRibose = new AttachmentList();
    groupsRibose.add(new Attachment("R3-OH", "R3", "OH", riboseR3));
    groupsRibose.add(new Attachment("R1-H", "R1", "H", riboseR1));
    groupsRibose.add(new Attachment("R2-H", "R2", "H", riboseR2));
// AbstractChemistryManipulator manipulator = getManipulator();
    AbstractMolecule molecule = manipulator.getMolecule(ribose, groupsRibose);
    Map<AbstractMolecule, Map<IAtomBase, IAtomBase>> groupsToMerge = new HashMap<>();
    for (Attachment attachment : molecule.getAttachments()) {
      int groupId = AbstractMolecule.getIdFromLabel(attachment.getLabel());
      String smiles = attachment.getSmiles();
      LOG.debug(smiles);
      AbstractMolecule rMol = manipulator.getMolecule(smiles, null);
      Map<IAtomBase, IAtomBase> atoms = new HashMap<>();
      atoms.put(molecule.getRGroupAtom(groupId, true), rMol.getRGroupAtom(groupId, true));
      groupsToMerge.put(rMol, atoms);
    }
    for (AbstractMolecule mol : groupsToMerge.keySet()) {
      for (IAtomBase atom : groupsToMerge.get(mol).keySet()) {
        molecule =
            manipulator.merge(molecule, atom, mol, groupsToMerge.get(mol).get(atom));
      }
    }

    testResult = manipulator.convertMolecule(molecule, StType.SMILES);

  }

  public void mergePhosphatRiboseTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
      CTKException {

    String ribose = "O[C@H]1[C@H]([*])O[C@H](CO[*])[C@H]1O[*] |$;;;_R3;;;;;_R1;;;_R2$|";
    String phosphat = "OP([*])([*])=O |$;;_R1;_R2;$|";
    String riboseR1 = "[*][H] |$_R1;$|";
    String riboseR2 = "[*][H] |$_R2;$|";
    String riboseR3 = "O[*] |$;_R3$|";
    String phosphatR1 = "O[*] |$;_R1$|";
    String phosphatR2 = "O[*] |$;_R2$|";
    AttachmentList groupsRibose = new AttachmentList();
    groupsRibose.add(new Attachment("R3-OH", "R3", "OH", riboseR3));
    groupsRibose.add(new Attachment("R1-H", "R1", "H", riboseR1));
    groupsRibose.add(new Attachment("R2-H", "R2", "H", riboseR2));

    AttachmentList groupsPhosphat = new AttachmentList();
    groupsPhosphat.add(new Attachment("R1-OH", "R1", "OH", phosphatR1));
    groupsPhosphat.add(new Attachment("R2-OH", "R2", "OH", phosphatR2));

    // AbstractChemistryManipulator manipulator = ManipulatorFactory.buildManipulator(ManipulatorType.MARVIN);
    AbstractMolecule riboseMolecule = manipulator.getMolecule(ribose, groupsRibose);
    AbstractMolecule phosphatMolecule = manipulator.getMolecule(phosphat, groupsPhosphat);
    AbstractMolecule molecule =
        manipulator.merge(riboseMolecule, riboseMolecule.getRGroupAtom(2, true), phosphatMolecule, phosphatMolecule.getRGroupAtom(1, true));

    testResult = manipulator.convertMolecule(molecule, StType.MOLFILE);
  }

  public void mergeSelfCycle() throws CTKException, IOException {
    String a = "C[C@H](N[*])C([*])=O |$;;;_R1;;_R2;$|";
    String aR1 = "[*][H] |$_R1;$|";
    String aR2 = "O[*] |$;_R2$|";
    AttachmentList groupsA = new AttachmentList();
    groupsA.add(new Attachment("R1-H", "R1", "H", aR1));
    groupsA.add(new Attachment("R2-OH", "R2", "OH", aR2));
    AbstractMolecule a1 = manipulator.getMolecule(a, groupsA);
    IAtomBase a1group1 = a1.getRGroupAtom(1, true);
    IAtomBase a1group2 = a1.getRGroupAtom(2, true);
    AbstractMolecule a2 = manipulator.getMolecule(a, groupsA.cloneList());
    IAtomBase a2group1 = a2.getRGroupAtom(1, true);
    IAtomBase a2group2 = a2.getRGroupAtom(2, true);
    AbstractMolecule a3 = manipulator.getMolecule(a, groupsA.cloneList());
    IAtomBase a3group1 = a3.getRGroupAtom(1, true);
    IAtomBase a3group2 = a3.getRGroupAtom(2, true);
    AbstractMolecule molecule = manipulator.merge(a1, a1group2, a2, a2group1);
    molecule = manipulator.merge(molecule, a2group2, a3, a3group1);
    molecule = manipulator.merge(molecule, a3group2, molecule, a1group1);
    molecule.generateCoordinates();
    testResult = manipulator.convertMolecule(molecule, StType.MOLFILE);

  }

  public void merge2Ribose() throws IOException, CTKException {
    String ribose = "O[C@H]1[C@H]([*])O[C@H](CO[*])[C@H]1O[*] |$;;;_R3;;;;;_R1;;;_R2$|";

    String riboseR1 = "[*][H] |$_R1;$|";
    String riboseR2 = "[*][H] |$_R2;$|";
    String riboseR3 = "O[*] |$;_R3$|";

    AttachmentList groupsRibose = new AttachmentList();

    groupsRibose.add(new Attachment("R1-H", "R1", "H", riboseR1));
    groupsRibose.add(new Attachment("R2-H", "R2", "H", riboseR2));
    groupsRibose.add(new Attachment("R3-OH", "R3", "OH", riboseR3));

    AbstractMolecule ribose1 = manipulator.getMolecule(ribose, groupsRibose);
    // ribose1.generateCoordinates();
    AbstractMolecule ribose2 = manipulator.getMolecule(ribose, groupsRibose.cloneList());
    // ribose2.generateCoordinates();
    AbstractMolecule molecule =
        manipulator.merge(ribose1, ribose1.getRGroupAtom(3, true), ribose2, ribose2.getRGroupAtom(3, true));

    testResult = manipulator.convertMolecule(molecule, StType.MOLFILE);

  }
}
